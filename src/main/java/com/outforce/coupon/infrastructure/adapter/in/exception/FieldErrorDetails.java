package com.outforce.coupon.infrastructure.adapter.in.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Validation error details for a specific field")
public class FieldErrorDetails {
    @Schema(
            description = "Field name that failed validation."
    )
    private String field;

    @Schema(
            description = "Validation error message for the field."
    )
    private String message;
}
