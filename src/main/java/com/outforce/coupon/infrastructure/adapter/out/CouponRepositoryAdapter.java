package com.outforce.coupon.infrastructure.adapter.out;

import com.outforce.coupon.application.usecase.port.out.CouponRepositoryPort;
import com.outforce.coupon.domain.entity.Coupon;
import com.outforce.coupon.infrastructure.adapter.out.entity.CouponEntity;
import com.outforce.coupon.infrastructure.adapter.out.repository.CouponRepository;
import com.outforce.coupon.infrastructure.config.MyModelMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class CouponRepositoryAdapter implements CouponRepositoryPort {

    private final CouponRepository couponRepository;

    private final MyModelMapper mapper;

    public CouponRepositoryAdapter(CouponRepository couponRepository, MyModelMapper mapper) {
        this.couponRepository = couponRepository;
        this.mapper = mapper;
    }

    @Override
    public Coupon save(Coupon coupon) {
        CouponEntity couponEntity = mapper.mapTo(coupon, CouponEntity.class);
        return mapper.mapTo(couponRepository.save(couponEntity),Coupon.class);
    }

    @Override
    public Optional<Coupon> findById(UUID id) {
        return couponRepository.findById(id)
                .map(couponEntity -> mapper.mapTo(couponEntity, Coupon.class));
    }
}
