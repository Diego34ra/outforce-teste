package com.outforce.coupon.infrastructure.adapter.in.controller.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CouponRequestTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    static void tearDown() {
        validatorFactory.close();
    }

    @Test
    void validateShouldNotReturnViolationsForValidRequest() {
        CouponRequest request = new CouponRequest(
                "ABC-123",
                "Black Friday coupon",
                BigDecimal.valueOf(0.8),
                Instant.parse("2026-11-04T17:14:45.180Z"),
                false
        );

        Set<ConstraintViolation<CouponRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    void validateShouldReturnViolationsForNullRequiredFields() {
        CouponRequest request = new CouponRequest(null, null, null, null, false);

        Set<ConstraintViolation<CouponRequest>> violations = validator.validate(request);

        assertEquals(4, violations.size());
        assertTrue(containsViolation(violations, "code", "Code is required"));
        assertTrue(containsViolation(violations, "description", "Description is required"));
        assertTrue(containsViolation(violations, "discountValue", "Discount value is required"));
        assertTrue(containsViolation(violations, "expirationDate", "Expiration date is required"));
    }

    @Test
    void validateShouldReturnViolationsForBlankAndInvalidFields() {
        CouponRequest request = new CouponRequest(
                "   ",
                "   ",
                BigDecimal.valueOf(0.4),
                Instant.parse("2026-05-08T17:14:45.180Z"),
                false
        );

        Set<ConstraintViolation<CouponRequest>> violations = validator.validate(request);

        assertEquals(4, violations.size());
        assertTrue(containsViolation(violations, "code", "Code is required"));
        assertTrue(containsViolation(violations, "description", "Description is required"));
        assertTrue(containsViolation(violations, "discountValue", "Discount value must be greater than or equal to 0.5"));
        assertTrue(containsViolation(violations, "expirationDate", "Expiration date cannot be in the past"));
    }

    private boolean containsViolation(
            Set<ConstraintViolation<CouponRequest>> violations,
            String field,
            String message
    ) {
        return violations.stream()
                .anyMatch(violation ->
                        field.equals(violation.getPropertyPath().toString())
                                && message.equals(violation.getMessage()));
    }
}