package com.outforce.coupon.infrastructure.adapter.in.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ErrorDetailsTest {

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Test
    void messageConstructorShouldPopulateOnlyMessage() {
        ErrorDetails errorDetails = new ErrorDetails("Validation failed");

        assertEquals("Validation failed", errorDetails.getMessage());
    }

    @Test
    void partialConstructorShouldPopulateBaseFields() {
        Instant timestamp = Instant.parse("2026-05-09T15:10:00Z");

        ErrorDetails errorDetails = new ErrorDetails(timestamp, 400, "Validation failed", "/coupon");

        assertEquals(timestamp, errorDetails.getTimestamp());
        assertEquals(400, errorDetails.getStatus());
        assertEquals("Validation failed", errorDetails.getMessage());
        assertEquals("/coupon", errorDetails.getPath());
    }

    @Test
    void allArgsConstructorShouldPopulateFieldErrors() {
        Instant timestamp = Instant.parse("2026-05-09T15:10:00Z");
        List<FieldErrorDetails> fieldErrors = List.of(new FieldErrorDetails("code", "Code is required"));

        ErrorDetails errorDetails = new ErrorDetails(timestamp, 400, "Validation failed", "/coupon", fieldErrors);

        assertEquals(timestamp, errorDetails.getTimestamp());
        assertEquals(400, errorDetails.getStatus());
        assertEquals("Validation failed", errorDetails.getMessage());
        assertEquals("/coupon", errorDetails.getPath());
        assertEquals(fieldErrors, errorDetails.getFieldErrors());
    }

    @Test
    void serializationShouldOmitFieldErrorsWhenNull() throws Exception {
        ErrorDetails errorDetails = new ErrorDetails(
                Instant.parse("2026-05-09T15:10:00Z"),
                400,
                "Validation failed",
                "/coupon"
        );

        String json = objectMapper.writeValueAsString(errorDetails);

        assertFalse(json.contains("fieldErrors"));
    }

    @Test
    void serializationShouldIncludeFieldErrorsWhenPresent() throws Exception {
        ErrorDetails errorDetails = new ErrorDetails(
                Instant.parse("2026-05-09T15:10:00Z"),
                400,
                "Validation failed",
                "/coupon",
                List.of(new FieldErrorDetails("code", "Code is required"))
        );

        String json = objectMapper.writeValueAsString(errorDetails);

        assertTrue(json.contains("fieldErrors"));
        assertTrue(json.contains("\"field\":\"code\""));
    }
}