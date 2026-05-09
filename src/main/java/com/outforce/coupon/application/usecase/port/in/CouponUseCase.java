package com.outforce.coupon.application.usecase.port.in;

import com.outforce.coupon.application.usecase.command.CreateCouponCommand;
import com.outforce.coupon.domain.entity.Coupon;

import java.util.UUID;

public interface CouponUseCase {
    Coupon create(CreateCouponCommand coupon);

    Coupon findById(UUID uuid);

    void delete(UUID uuid);
}
