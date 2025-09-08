package com.coreplatform.pricing.infrastructure.web;

import com.coreplatform.pricing.application.dto.PriceResponseDTO;
import com.coreplatform.pricing.application.port.in.PriceQueryUseCase;
import com.coreplatform.pricing.infrastructure.web.exception.PriceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PriceController.class)
class PriceControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    PriceQueryUseCase useCase;

    @Test
    void returns200AndBodyWhenServiceReturnsDto() throws Exception {
        PriceResponseDTO dto = PriceResponseDTO.builder()
                .productId(35455L)
                .brandId(1L)
                .priceList(1)
                .startDate(LocalDateTime.parse("2020-06-14T00:00:00"))
                .endDate(LocalDateTime.parse("2020-12-31T23:59:59"))
                .price(new BigDecimal("35.50"))
                .currency("EUR")
                .build();

        when(useCase.getApplicablePrice(35455L, 1L, LocalDateTime.parse("2020-06-14T10:00:00")))
                .thenReturn(dto);

        mvc.perform(get("/prices")
                        .param("productId", "35455")
                        .param("brandId", "1")
                        .param("applicationDate", "2020-06-14T10:00:00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId", is(35455)))
                .andExpect(jsonPath("$.brandId", is(1)))
                .andExpect(jsonPath("$.priceList", is(1)))
                .andExpect(jsonPath("$.price", is(35.50)))
                .andExpect(jsonPath("$.currency", is("EUR")))
                .andExpect(jsonPath("$.startDate", is("2020-06-14T00:00:00")))
                .andExpect(jsonPath("$.endDate", is("2020-12-31T23:59:59")));
    }

    @Test
    void returns404WhenServiceThrowsPriceNotFound() throws Exception {
        when(useCase.getApplicablePrice(any(), any(), any()))
                .thenThrow(new PriceNotFoundException(35455L, 1L, LocalDateTime.parse("2019-01-01T00:00:00")));

        mvc.perform(get("/prices")
                        .param("productId", "35455")
                        .param("brandId", "1")
                        .param("applicationDate", "2019-01-01T00:00:00"))
                .andExpect(status().isNotFound());
    }

    @Test
    void returns400WhenInvalidDateFormat() throws Exception {
        mvc.perform(get("/prices")
                        .param("productId", "35455")
                        .param("brandId", "1")
                        .param("applicationDate", "fecha-mala"))
                .andExpect(status().isBadRequest());
    }
}
