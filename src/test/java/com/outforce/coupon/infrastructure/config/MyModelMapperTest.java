package com.outforce.coupon.infrastructure.config;

import com.outforce.coupon.infrastructure.adapter.in.controller.response.CouponResponse;
import com.outforce.coupon.infrastructure.adapter.out.entity.CouponEntity;
import com.outforce.coupon.infrastructure.adapter.out.entity.CouponStatusEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class MyModelMapperTest {

    private static final Instant EXPIRATION_DATE = Instant.parse("2026-05-10T12:00:00Z");

    private final MyModelMapper myModelMapper = new MyModelMapper();

    @Test
    void mapToShouldMapCouponEntityToCouponResponse() {
        CouponEntity couponEntity = buildCouponEntity(UUID.randomUUID(), "ABC123");

        CouponResponse response = myModelMapper.mapTo(couponEntity, CouponResponse.class);

        assertNotNull(response);
        assertEquals(couponEntity.getId().toString(), response.getId());
        assertEquals(couponEntity.getCode(), response.getCode());
        assertEquals(couponEntity.getDescription(), response.getDescription());
        assertEquals(couponEntity.getDiscountValue(), response.getDiscountValue());
        assertEquals(couponEntity.getExpirationDate(), response.getExpirationDate());
        assertEquals(couponEntity.getStatus().name(), response.getStatus());
        assertEquals(couponEntity.isPublished(), response.isPublished());
        assertEquals(couponEntity.isRedeemed(), response.isRedeemed());
    }

    @Test
    void toListShouldMapCouponEntityListToCouponResponseList() {
        CouponEntity firstCoupon = buildCouponEntity(UUID.randomUUID(), "ABC123");
        CouponEntity secondCoupon = buildCouponEntity(UUID.randomUUID(), "XYZ789");

        List<CouponResponse> responses = myModelMapper.toList(
                List.of(firstCoupon, secondCoupon),
                CouponResponse.class
        );

        assertEquals(2, responses.size());
        assertEquals(firstCoupon.getId().toString(), responses.get(0).getId());
        assertEquals(firstCoupon.getCode(), responses.get(0).getCode());
        assertEquals(secondCoupon.getId().toString(), responses.get(1).getId());
        assertEquals(secondCoupon.getCode(), responses.get(1).getCode());
    }

    private CouponEntity buildCouponEntity(UUID id, String code) {
        CouponEntity couponEntity = new CouponEntity();
        couponEntity.setId(id);
        couponEntity.setCode(code);
        couponEntity.setDescription("Black Friday coupon");
        couponEntity.setDiscountValue(BigDecimal.valueOf(10.50));
        couponEntity.setExpirationDate(EXPIRATION_DATE);
        couponEntity.setStatus(CouponStatusEntity.ACTIVE);
        couponEntity.setPublished(true);
        couponEntity.setRedeemed(false);
        return couponEntity;
    }
}
