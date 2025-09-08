package com.coreplatform.pricing.application.service;

import com.coreplatform.pricing.application.dto.PriceResponseDTO;
import com.coreplatform.pricing.domain.model.Price;
import com.coreplatform.pricing.domain.repository.PriceRepository;
import com.coreplatform.pricing.infrastructure.web.exception.PriceNotFoundException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class PriceServiceTest {

    private final PriceRepository repo = mock(PriceRepository.class);
    private final PriceService service = new PriceService(repo);

    @Test
    void returnsDtoWhenPriceIsFound() {
        Price price = Price.builder()
                .productId(35455L)
                .brandId(1L)
                .priceList(2)
                .startDate(LocalDateTime.parse("2020-06-14T15:00:00"))
                .endDate(LocalDateTime.parse("2020-06-14T18:30:00"))
                .price(new BigDecimal("25.45"))
                .curr("EUR")
                .build();

        when(repo.findApplicablePrice(eq(35455L), eq(1L), any()))
                .thenReturn(Optional.of(price));

        PriceResponseDTO dto = service.getApplicablePrice(
                35455L, 1L, LocalDateTime.parse("2020-06-14T16:00:00"));

        assertEquals(35455L, dto.getProductId());
        assertEquals(1L, dto.getBrandId());
        assertEquals(2, dto.getPriceList());
        assertEquals(new BigDecimal("25.45"), dto.getPrice());
        assertEquals("EUR", dto.getCurrency());
    }

    @Test
    void throwsWhenPriceNotFound() {
        when(repo.findApplicablePrice(any(), any(), any()))
                .thenReturn(Optional.empty());

        assertThrows(PriceNotFoundException.class, () ->
                service.getApplicablePrice(
                        35455L, 1L, LocalDateTime.parse("2020-01-01T00:00:00"))
        );
    }
}
