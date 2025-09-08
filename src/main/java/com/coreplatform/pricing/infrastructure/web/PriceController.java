package com.coreplatform.pricing.infrastructure.web;

import com.coreplatform.pricing.application.dto.PriceResponseDTO;
import com.coreplatform.pricing.application.port.in.PriceQueryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/prices")
@RequiredArgsConstructor
public class PriceController {
    private final PriceQueryUseCase useCase;

    @GetMapping
    public PriceResponseDTO getPrice(
            @RequestParam Long productId,
            @RequestParam Long brandId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime applicationDate) {
        return useCase.getApplicablePrice(productId, brandId, applicationDate);
    }
}
