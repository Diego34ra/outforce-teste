package com.outforce.coupon.infrastructure.adapter.in.controller.response;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class CouponResponseTest {

    @Test
    void allArgsConstructorShouldPopulateFields() {
        Instant expirationDate = Instant.parse("2026-11-04T17:14:45.180Z");

        CouponResponse response = new CouponResponse(
                "cef9d1e3-aae5-4ab6-a297-358c6032b1e7",
                "ABC123",
                "Black Friday coupon",
                BigDecimal.valueOf(0.8),
                expirationDate,
                "ACTIVE",
                true,
                false
        );

        assertEquals("cef9d1e3-aae5-4ab6-a297-358c6032b1e7", response.getId());
        assertEquals("ABC123", response.getCode());
        assertEquals("Black Friday coupon", response.getDescription());
        assertEquals(BigDecimal.valueOf(0.8), response.getDiscountValue());
        assertEquals(expirationDate, response.getExpirationDate());
        assertEquals("ACTIVE", response.getStatus());
        assertTrue(response.isPublished());
        assertFalse(response.isRedeemed());
    }

    @Test
    void settersShouldUpdateFields() {
        Instant expirationDate = Instant.parse("2026-11-04T17:14:45.180Z");
        CouponResponse response = new CouponResponse();

        response.setId("598c5a85-46d6-4c69-8513-6ecfb9b5d7e2");
        response.setCode("XYZ789");
        response.setDescription("Cyber Monday coupon");
        response.setDiscountValue(BigDecimal.valueOf(1.2));
        response.setExpirationDate(expirationDate);
        response.setStatus("DELETED");
        response.setPublished(false);
        response.setRedeemed(true);

        assertEquals("598c5a85-46d6-4c69-8513-6ecfb9b5d7e2", response.getId());
        assertEquals("XYZ789", response.getCode());
        assertEquals("Cyber Monday coupon", response.getDescription());
        assertEquals(BigDecimal.valueOf(1.2), response.getDiscountValue());
        assertEquals(expirationDate, response.getExpirationDate());
        assertEquals("DELETED", response.getStatus());
        assertFalse(response.isPublished());
        assertTrue(response.isRedeemed());
    }
}