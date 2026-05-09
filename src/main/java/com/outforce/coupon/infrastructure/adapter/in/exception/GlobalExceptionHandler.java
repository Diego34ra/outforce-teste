package com.outforce.coupon.infrastructure.adapter.in.exception;

import com.outforce.coupon.domain.exception.BusinessRuleViolationException;
import com.outforce.coupon.domain.exception.CouponNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {
    private final HttpServletRequest request;

    public GlobalExceptionHandler(HttpServletRequest request) {
        this.request = request;
    }

    @ExceptionHandler(BusinessRuleViolationException.class)
    public ResponseEntity<ErrorDetails> handleBusinessRuleViolation(BusinessRuleViolationException ex){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorDetails errorResponse = new ErrorDetails(Instant.now(), status.value(), ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(errorResponse);
    }

    @ExceptionHandler(CouponNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleNotFound(CouponNotFoundException ex){
        HttpStatus status = HttpStatus.NOT_FOUND;
        ErrorDetails errorResponse = new ErrorDetails(Instant.now(), status.value(), ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetails> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        List<FieldErrorDetails> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> new FieldErrorDetails(fieldError.getField(), fieldError.getDefaultMessage()))
                .toList();

        ErrorDetails errorResponse = new ErrorDetails(
                Instant.now(),
                status.value(),
                "Validation failed",
                request.getRequestURI(),
                fieldErrors
        );
        return ResponseEntity.status(status).body(errorResponse);
    }
}
