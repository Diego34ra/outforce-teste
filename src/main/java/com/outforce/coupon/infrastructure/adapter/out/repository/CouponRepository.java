package com.outforce.coupon.infrastructure.adapter.out.repository;

import com.outforce.coupon.infrastructure.adapter.out.entity.CouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CouponRepository extends JpaRepository<CouponEntity, UUID> {
}
