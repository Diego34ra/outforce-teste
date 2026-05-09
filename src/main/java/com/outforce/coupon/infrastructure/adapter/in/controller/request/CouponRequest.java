package com.outforce.coupon.infrastructure.adapter.in.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request payload for coupon creation")
public class CouponRequest {
    @Schema(
            description = "Coupon code. Special characters are accepted in the request but removed before persistence, and the result must contain exactly 6 alphanumeric characters.",
            example = "ABC-123",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Code is required")
    private String code;

    @Schema(
            description = "Coupon description.",
            example = "Iure saepe amet. Excepturi saepe inventore nam doloremque voluptatem a.",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Description is required")
    private String description;

    @Schema(
            description = "Discount value. Minimum allowed value is 0.5.",
            example = "0.8",
            minimum = "0.5",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Discount value is required")
    @DecimalMin(value = "0.5", inclusive = true, message = "Discount value must be greater than or equal to 0.5")
    private BigDecimal discountValue;

    @Schema(
            description = "Coupon expiration date and time. It cannot be in the past at creation time.",
            example = "2026-11-04T17:14:45.180Z",
            format = "date-time",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Expiration date is required")
    @FutureOrPresent(message = "Expiration date cannot be in the past")
    private Instant expirationDate;

    @Schema(
            description = "Indicates whether the coupon is already published at creation time.",
            example = "false"
    )
    private boolean published;
}
