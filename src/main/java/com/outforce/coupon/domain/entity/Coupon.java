package com.outforce.coupon.domain.entity;

import com.outforce.coupon.domain.enums.CouponStatus;
import com.outforce.coupon.domain.exception.BusinessRuleViolationException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Coupon {
    private static final BigDecimal MINIMUM_DISCOUNT = BigDecimal.valueOf(0.5);

    private UUID id;

    private String code;

    private String description;

    private BigDecimal discountValue;

    private Instant expirationDate;

    private CouponStatus status;

    private boolean published;

    private boolean redeemed;

    private Instant deletedAt;

    private Coupon(
            UUID id,
            String code,
            String description,
            BigDecimal discountValue,
            Instant expirationDate,
            boolean published
    ) {
        this.id = Objects.requireNonNull(id);
        this.code = sanitizeAndValidateCode(code);
        this.description = validateDescription(description);
        this.discountValue = validateDiscountValue(discountValue);
        this.expirationDate = expirationDate;
        this.status = CouponStatus.ACTIVE;
        this.published = published;
        this.redeemed = false;
    }

    public static Coupon create(
            String code,
            String description,
            BigDecimal discountValue,
            Instant expirationDate,
            boolean published
    ) {
        return create(code, description, discountValue, expirationDate, published, Clock.systemUTC());
    }

    static Coupon create(
            String code,
            String description,
            BigDecimal discountValue,
            Instant expirationDate,
            boolean published,
            Clock clock
    ) {
        return new Coupon(
                UUID.randomUUID(),
                code,
                description,
                discountValue,
                validateExpirationDate(expirationDate, clock),
                published
        );
    }

    public void delete() {
        if (status == CouponStatus.DELETED) {
            throw new BusinessRuleViolationException("Coupon has already been deleted");
        }
        status = CouponStatus.DELETED;
        deletedAt = Instant.now();
    }

    private static String sanitizeAndValidateCode(String rawCode) {
        if (rawCode == null || rawCode.isBlank()) {
            throw new BusinessRuleViolationException("Coupon code is required");
        }

        String sanitizedCode = rawCode.replaceAll("[^A-Za-z0-9]", "").toUpperCase();
        if (sanitizedCode.length() != 6) {
            throw new BusinessRuleViolationException("Coupon code must contain 6 alphanumeric characters");
        }
        return sanitizedCode;
    }

    private static String validateDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new BusinessRuleViolationException("Coupon description is required");
        }
        return description;
    }

    private static BigDecimal validateDiscountValue(BigDecimal discountValue) {
        if (discountValue == null) {
            throw new BusinessRuleViolationException("Discount value is required");
        }
        if (discountValue.compareTo(MINIMUM_DISCOUNT) < 0) {
            throw new BusinessRuleViolationException("Discount value must be greater than or equal to " + MINIMUM_DISCOUNT);
        }
        return discountValue;
    }

    private static Instant validateExpirationDate(Instant expirationDate, Clock clock) {
        if (expirationDate == null) {
            throw new BusinessRuleViolationException("Expiration date is required");
        }
        if (expirationDate.isBefore(Instant.now(clock))) {
            throw new BusinessRuleViolationException("Expiration date cannot be in the past");
        }
        return expirationDate;
    }
}
