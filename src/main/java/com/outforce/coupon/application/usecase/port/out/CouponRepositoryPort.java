package com.outforce.coupon.application.usecase.port.out;

import com.outforce.coupon.domain.entity.Coupon;

public interface CouponRepositoryPort {
    Coupon save(Coupon coupon);
}
