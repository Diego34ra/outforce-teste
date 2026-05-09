package com.outforce.coupon.application.usecase;

import com.outforce.coupon.application.usecase.command.CreateCouponCommand;
import com.outforce.coupon.application.usecase.port.in.CouponUseCase;
import com.outforce.coupon.application.usecase.port.out.CouponRepositoryPort;
import com.outforce.coupon.domain.entity.Coupon;

public class CouponUseCaseImpl implements CouponUseCase {

    private final CouponRepositoryPort couponRepositoryPort;

    public CouponUseCaseImpl(CouponRepositoryPort couponRepositoryPort) {
        this.couponRepositoryPort = couponRepositoryPort;
    }

    @Override
    public Coupon create(CreateCouponCommand couponCommand) {
        Coupon coupon = Coupon.create(
                couponCommand.code(),
                couponCommand.description(),
                couponCommand.discountValue(),
                couponCommand.expirationDate(),
                couponCommand.published()
        );
        return couponRepositoryPort.save(coupon);
    }
}
