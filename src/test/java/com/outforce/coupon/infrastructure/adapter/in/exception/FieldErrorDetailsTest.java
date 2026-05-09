package com.outforce.coupon.infrastructure.adapter.in.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FieldErrorDetailsTest {

    @Test
    void allArgsConstructorShouldPopulateFields() {
        FieldErrorDetails details = new FieldErrorDetails("code", "Code is required");

        assertEquals("code", details.getField());
        assertEquals("Code is required", details.getMessage());
    }

    @Test
    void settersShouldUpdateFields() {
        FieldErrorDetails details = new FieldErrorDetails();

        details.setField("discountValue");
        details.setMessage("Discount value must be greater than or equal to 0.5");

        assertEquals("discountValue", details.getField());
        assertEquals("Discount value must be greater than or equal to 0.5", details.getMessage());
    }
}