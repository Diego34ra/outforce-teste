package com.outforce.coupon.infrastructure.adapter.in.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorDetails {
    private Instant timestamp;

    private Integer status;

    private String message;

    private String path;

    private List<FieldErrorDetails> fieldErrors;

    public ErrorDetails(String message) {
        this.message = message;
    }

    public ErrorDetails(Instant timestamp, Integer status, String message, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.message = message;
        this.path = path;
    }
}
