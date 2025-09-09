    package com.coreplatform.pricing.infrastructure.persistence;

    import com.coreplatform.pricing.domain.model.Price;
    import com.coreplatform.pricing.domain.repository.PriceRepository;
    import lombok.RequiredArgsConstructor;
    import org.springframework.stereotype.Component;

    import java.time.LocalDateTime;
    import java.util.Optional;

    @Component
    @RequiredArgsConstructor
    public class PriceRepositoryJpaAdapter implements PriceRepository {
        private final JpaPriceRepository jpa;

        @Override
        public Optional<Price> findApplicablePrice(Long productId, Long brandId, LocalDateTime applicationDate) {
            return jpa.findTopByProductIdAndBrandIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByPriorityDesc(
                            productId, brandId, applicationDate, applicationDate)
                    .map(e -> Price.builder()
                            .brandId(e.getBrandId()).startDate(e.getStartDate()).endDate(e.getEndDate())
                            .priceList(e.getPriceList()).productId(e.getProductId())
                            .priority(e.getPriority()).price(e.getPrice()).curr(e.getCurr())
                            .build());
        }
    }
