package com.outforce.coupon.infrastructure.adapter.out.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "coupon_tb")
public class CouponEntity {
    @Id
    private UUID id;

    @Column(nullable = false, length = 6)
    private String code;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal discountValue;

    @Column(nullable = false)
    private Instant expirationDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CouponStatusEntity status;

    @Column(nullable = false)
    private boolean published;

    @Column(nullable = false)
    private boolean redeemed;

    @Column
    private Instant deletedAt;
}
