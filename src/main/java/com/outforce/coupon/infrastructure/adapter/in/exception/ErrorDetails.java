package com.outforce.coupon.infrastructure.adapter.in.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Standard error response")
public class ErrorDetails {
    @Schema(
            description = "Timestamp when the error occurred.",
            example = "2026-05-09T15:10:00Z"
    )
    private Instant timestamp;

    @Schema(
            description = "HTTP status code.",
            example = "400"
    )
    private Integer status;

    @Schema(
            description = "General error message.",
            example = "Validation failed"
    )
    private String message;

    @Schema(
            description = "Request path that caused the error."
    )
    private String path;

    @Schema(
            description = "Detailed validation errors by field. Present only when the request contains invalid fields."
    )
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
