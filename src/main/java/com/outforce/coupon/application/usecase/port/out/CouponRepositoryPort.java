package com.outforce.coupon.application.usecase.port.out;

import com.outforce.coupon.domain.entity.Coupon;

import java.util.Optional;
import java.util.UUID;

public interface CouponRepositoryPort {
    Coupon save(Coupon coupon);

    Optional<Coupon> findById(UUID id);

}
