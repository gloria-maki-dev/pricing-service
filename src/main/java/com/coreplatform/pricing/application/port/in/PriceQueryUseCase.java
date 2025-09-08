package com.coreplatform.pricing.application.port.in;

import com.coreplatform.pricing.application.dto.PriceResponseDTO;
import java.time.LocalDateTime;

public interface PriceQueryUseCase {
    PriceResponseDTO getApplicablePrice(Long productId, Long brandId, LocalDateTime applicationDate);
}
