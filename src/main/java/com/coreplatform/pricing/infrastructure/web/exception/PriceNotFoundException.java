package com.coreplatform.pricing.infrastructure.web.exception;

import java.time.LocalDateTime;

public class PriceNotFoundException extends RuntimeException {
    public PriceNotFoundException(Long productId, Long brandId, LocalDateTime date) {
        super("No price found for product " + productId + ", brand " + brandId + " at " + date);
    }
}
