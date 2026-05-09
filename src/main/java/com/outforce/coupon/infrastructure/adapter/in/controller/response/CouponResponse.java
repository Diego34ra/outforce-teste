package com.outforce.coupon.infrastructure.adapter.in.controller.response;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Coupon response payload")
public class CouponResponse {
    @Schema(
            description = "Unique coupon identifier.",
            example = "cef9d1e3-aae5-4ab6-a297-358c6032b1e7"
    )
    private String id;

    @Schema(
            description = "Coupon code with exactly 6 alphanumeric characters.",
            example = "ABC123"
    )
    private String code;

    @Schema(
            description = "Coupon description.",
            example = "Maiores repellat pariatur nisi nostrum autem magni voluptas facere nesciunt."
    )
    private String description;

    @Schema(
            description = "Discount value applied by the coupon.",
            example = "0.8"
    )
    private BigDecimal discountValue;

    @Schema(
            description = "Coupon expiration date and time.",
            example = "2025-11-04T17:36:46.577Z",
            format = "date-time"
    )
    private Instant expirationDate;

    @Schema(
            description = "Current coupon status.",
            example = "ACTIVE"
    )
    private String status;

    @Schema(
            description = "Indicates whether the coupon is published.",
            example = "false"
    )
    private boolean published;

    @Schema(
            description = "Indicates whether the coupon has already been redeemed.",
            example = "false"
    )
    private boolean redeemed;
}
