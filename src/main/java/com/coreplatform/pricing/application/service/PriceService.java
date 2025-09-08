package com.coreplatform.pricing.application.service;

import com.coreplatform.pricing.application.dto.PriceResponseDTO;
import com.coreplatform.pricing.application.port.in.PriceQueryUseCase;
import com.coreplatform.pricing.domain.model.Price;
import com.coreplatform.pricing.domain.repository.PriceRepository;
import com.coreplatform.pricing.infrastructure.web.exception.PriceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PriceService implements PriceQueryUseCase {
    private final PriceRepository priceRepository;

    @Override
    public PriceResponseDTO getApplicablePrice(Long productId, Long brandId, LocalDateTime applicationDate) {
        Price p = priceRepository.findApplicablePrice(productId, brandId, applicationDate)
                .orElseThrow(() -> new PriceNotFoundException(productId, brandId, applicationDate));

        return PriceResponseDTO.builder()
                .productId(p.getProductId()).brandId(p.getBrandId())
                .priceList(p.getPriceList()).startDate(p.getStartDate()).endDate(p.getEndDate())
                .price(p.getPrice()).currency(p.getCurr())
                .build();
    }
}
