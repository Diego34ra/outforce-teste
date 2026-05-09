package com.outforce.coupon.infrastructure.adapter.in.exception;

import com.outforce.coupon.domain.exception.BusinessRuleViolationException;
import com.outforce.coupon.domain.exception.CouponNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @Mock
    private HttpServletRequest request;

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        when(request.getRequestURI()).thenReturn("/coupon");
        globalExceptionHandler = new GlobalExceptionHandler(request);
    }

    @Test
    void handleBusinessRuleViolationShouldReturnBadRequest() {
        BusinessRuleViolationException exception = new BusinessRuleViolationException("Coupon code is required");

        ResponseEntity<ErrorDetails> response = globalExceptionHandler.handleBusinessRuleViolation(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().getStatus());
        assertEquals("Coupon code is required", response.getBody().getMessage());
        assertEquals("/coupon", response.getBody().getPath());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void handleNotFoundShouldReturnNotFound() {
        CouponNotFoundException exception = new CouponNotFoundException("Coupon not found");

        ResponseEntity<ErrorDetails> response = globalExceptionHandler.handleNotFound(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().getStatus());
        assertEquals("Coupon not found", response.getBody().getMessage());
        assertEquals("/coupon", response.getBody().getPath());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void handleMethodArgumentNotValidShouldReturnFieldErrors() {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "couponRequest");
        bindingResult.addError(new FieldError("couponRequest", "code", "Code is required"));
        bindingResult.addError(new FieldError("couponRequest", "discountValue", "Discount value is required"));
        MethodParameter methodParameter = mock(MethodParameter.class);
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(methodParameter, bindingResult);

        ResponseEntity<ErrorDetails> response = globalExceptionHandler.handleMethodArgumentNotValid(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().getStatus());
        assertEquals("Validation failed", response.getBody().getMessage());
        assertEquals("/coupon", response.getBody().getPath());
        assertNotNull(response.getBody().getTimestamp());

        List<FieldErrorDetails> fieldErrors = response.getBody().getFieldErrors();
        assertEquals(2, fieldErrors.size());
        assertTrue(fieldErrors.stream().anyMatch(error ->
                "code".equals(error.getField()) && "Code is required".equals(error.getMessage())));
        assertTrue(fieldErrors.stream().anyMatch(error ->
                "discountValue".equals(error.getField()) && "Discount value is required".equals(error.getMessage())));
    }
}