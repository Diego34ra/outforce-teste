package com.outforce.coupon.infrastructure.config;

import com.outforce.coupon.application.usecase.CouponUseCaseImpl;
import com.outforce.coupon.infrastructure.adapter.out.CouponRepositoryAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CouponConfig {

    @Bean
    public CouponUseCaseImpl manageCouponUseCase(CouponRepositoryAdapter couponRepositoryAdapter){
        return new CouponUseCaseImpl(couponRepositoryAdapter);
    }
}
