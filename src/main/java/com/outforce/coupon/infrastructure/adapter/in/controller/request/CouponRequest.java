package com.outforce.coupon.infrastructure.adapter.in.controller.request;

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
public class CouponRequest {
    @NotBlank(message = "Code is required")
    private String code;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Discount value is required")
    @DecimalMin(value = "0.5", inclusive = true, message = "Discount value must be greater than or equal to 0.5")
    private BigDecimal discountValue;

    @NotNull(message = "Expiration date is required")
    @FutureOrPresent(message = "Expiration date cannot be in the past")
    private Instant expirationDate;

    private boolean published;
}
