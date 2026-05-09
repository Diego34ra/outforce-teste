package com.outforce.coupon.domain.entity;

import com.outforce.coupon.domain.enums.CouponStatus;
import com.outforce.coupon.domain.exception.BusinessRuleViolationException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

class CouponTest {

    private static final Instant FUTURE_EXPIRATION_DATE = Instant.parse("2026-05-10T12:00:00Z");

    @Test
    void createShouldSanitizeCodeAndSetDefaultState() {
        Coupon coupon = Coupon.create(
                "ABC-123",
                "Black Friday coupon",
                BigDecimal.valueOf(0.8),
                FUTURE_EXPIRATION_DATE,
                false
        );

        assertNotNull(coupon.getId());
        assertEquals("ABC123", coupon.getCode());
        assertEquals(CouponStatus.ACTIVE, coupon.getStatus());
        assertFalse(coupon.isPublished());
        assertFalse(coupon.isRedeemed());
        assertEquals("Black Friday coupon", coupon.getDescription());
    }

    @Test
    void createShouldAllowPublishedCoupon() {
        Coupon coupon = Coupon.create(
                "abc123",
                "Published coupon",
                BigDecimal.valueOf(1.5),
                FUTURE_EXPIRATION_DATE,
                true
        );

        assertTrue(coupon.isPublished());
    }

    @Test
    void createShouldRejectCodeWithInvalidSanitizedLength() {
        String invalidCode = "AB-12";
        String description = "Invalid code coupon";
        BigDecimal discountValue = BigDecimal.valueOf(0.8);

        BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,
                () -> Coupon.create(invalidCode, description, discountValue, FUTURE_EXPIRATION_DATE, false)
        );

        assertEquals("Coupon code must contain 6 alphanumeric characters", exception.getMessage());
    }

    @Test
    void createShouldRejectNullCode() {
        String description = "Coupon without code";
        BigDecimal discountValue = BigDecimal.valueOf(0.8);

        BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,
                () -> Coupon.create(null, description, discountValue, FUTURE_EXPIRATION_DATE, false)
        );

        assertEquals("Coupon code is required", exception.getMessage());
    }

    @Test
    void createShouldRejectBlankCode() {
        String blankCode = "   ";
        String description = "Coupon with blank code";
        BigDecimal discountValue = BigDecimal.valueOf(0.8);

        BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,
                () -> Coupon.create(blankCode, description, discountValue, FUTURE_EXPIRATION_DATE, false)
        );

        assertEquals("Coupon code is required", exception.getMessage());
    }

    @Test
    void createShouldRejectNullDescription() {
        BigDecimal discountValue = BigDecimal.valueOf(0.8);

        BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,
                () -> Coupon.create("ABC123", null, discountValue, FUTURE_EXPIRATION_DATE, false)
        );

        assertEquals("Coupon description is required", exception.getMessage());
    }

    @Test
    void createShouldRejectBlankDescription() {
        String blankDescription = "   ";
        BigDecimal discountValue = BigDecimal.valueOf(0.8);

        BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,
                () -> Coupon.create("ABC123", blankDescription, discountValue, FUTURE_EXPIRATION_DATE, false)
        );

        assertEquals("Coupon description is required", exception.getMessage());
    }

    @Test
    void createShouldRejectNullDiscountValue() {
        String description = "Coupon without discount";

        BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,
                () -> Coupon.create("ABC123", description, null, FUTURE_EXPIRATION_DATE, false)
        );

        assertEquals("Discount value is required", exception.getMessage());
    }

    @Test
    void createShouldRejectDiscountBelowMinimum() {
        BigDecimal discountValue = BigDecimal.valueOf(0.4);
        String description = "Invalid discount coupon";

        BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,
                () -> Coupon.create("ABC123", description, discountValue, FUTURE_EXPIRATION_DATE, false)
        );

        assertEquals("Discount value must be greater than or equal to 0.5", exception.getMessage());
    }

    @Test
    void createShouldRejectNullExpirationDate() {
        String description = "Coupon without expiration date";
        BigDecimal discountValue = BigDecimal.valueOf(0.8);

        BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,
                () -> Coupon.create("ABC123", description, discountValue, null, false)
        );

        assertEquals("Expiration date is required", exception.getMessage());
    }

    @Test
    void createShouldRejectExpirationDateInThePast() {
        Instant fixedNow = Instant.parse("2026-05-09T12:00:00Z");
        Clock fixedClock = Clock.fixed(fixedNow, ZoneOffset.UTC);
        BigDecimal discountValue = BigDecimal.valueOf(0.8);
        Instant pastExpirationDate = fixedNow.minusSeconds(1);

        BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,
                () -> Coupon.create("ABC123", "Expired coupon", discountValue, pastExpirationDate, false, fixedClock)
        );

        assertEquals("Expiration date cannot be in the past", exception.getMessage());
    }

    @Test
    void deleteShouldMarkCouponAsDeleted() {
        Coupon coupon = Coupon.create(
                "ABC123",
                "Coupon to delete",
                BigDecimal.valueOf(0.8),
                FUTURE_EXPIRATION_DATE,
                false
        );

        coupon.delete();

        assertEquals(CouponStatus.DELETED, coupon.getStatus());
        assertNotNull(coupon.getDeletedAt());
    }

    @Test
    void deleteShouldRejectAlreadyDeletedCoupon() {
        Coupon coupon = Coupon.create(
                "ABC123",
                "Coupon to delete twice",
                BigDecimal.valueOf(0.8),
                FUTURE_EXPIRATION_DATE,
                false
        );
        coupon.delete();

        BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,
                coupon::delete
        );

        assertEquals("Coupon has already been deleted", exception.getMessage());
    }
}