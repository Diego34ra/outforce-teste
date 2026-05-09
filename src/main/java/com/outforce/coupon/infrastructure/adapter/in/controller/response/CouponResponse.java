package com.outforce.coupon.infrastructure.adapter.in.controller.response;

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
public class CouponResponse {
    private String id;

    private String code;

    private String description;

    private BigDecimal discountValue;

    private Instant expirationDate;

    private String status;

    private boolean published;

    private boolean redeemed;
}
