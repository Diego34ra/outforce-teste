package com.outforce.coupon.infrastructure.adapter.out;

import com.outforce.coupon.domain.entity.Coupon;
import com.outforce.coupon.domain.enums.CouponStatus;
import com.outforce.coupon.infrastructure.adapter.out.entity.CouponEntity;
import com.outforce.coupon.infrastructure.adapter.out.entity.CouponStatusEntity;
import com.outforce.coupon.infrastructure.adapter.out.repository.CouponRepository;
import com.outforce.coupon.infrastructure.config.MyModelMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CouponRepositoryAdapterTest {

    private static final Instant FUTURE_EXPIRATION_DATE = Instant.parse("2026-05-10T12:00:00Z");

    @Mock
    private CouponRepository couponRepository;

    @Spy
    private MyModelMapper mapper = new MyModelMapper();

    @InjectMocks
    private CouponRepositoryAdapter couponRepositoryAdapter;

    @Test
    void saveShouldMapCouponToEntityAndReturnMappedDomainObject() {
        UUID couponId = UUID.randomUUID();
        Coupon coupon = buildCoupon(couponId);
        CouponEntity savedEntity = buildCouponEntity(couponId);
        when(couponRepository.save(any(CouponEntity.class))).thenReturn(savedEntity);

        Coupon savedCoupon = couponRepositoryAdapter.save(coupon);

        ArgumentCaptor<CouponEntity> entityCaptor = ArgumentCaptor.forClass(CouponEntity.class);
        verify(couponRepository).save(entityCaptor.capture());
        CouponEntity persistedEntity = entityCaptor.getValue();

        assertEquals(coupon.getId(), persistedEntity.getId());
        assertEquals(coupon.getCode(), persistedEntity.getCode());
        assertEquals(coupon.getDescription(), persistedEntity.getDescription());
        assertEquals(coupon.getDiscountValue(), persistedEntity.getDiscountValue());
        assertEquals(coupon.getExpirationDate(), persistedEntity.getExpirationDate());
        assertEquals(CouponStatusEntity.ACTIVE, persistedEntity.getStatus());
        assertEquals(coupon.isPublished(), persistedEntity.isPublished());
        assertEquals(coupon.isRedeemed(), persistedEntity.isRedeemed());
        assertNotNull(savedCoupon);
        assertEquals(savedEntity.getId(), savedCoupon.getId());
        assertEquals(savedEntity.getCode(), savedCoupon.getCode());
        assertEquals(savedEntity.getDescription(), savedCoupon.getDescription());
        assertEquals(savedEntity.getDiscountValue(), savedCoupon.getDiscountValue());
        assertEquals(savedEntity.getExpirationDate(), savedCoupon.getExpirationDate());
        assertEquals(CouponStatus.ACTIVE, savedCoupon.getStatus());
        assertEquals(savedEntity.isPublished(), savedCoupon.isPublished());
        assertEquals(savedEntity.isRedeemed(), savedCoupon.isRedeemed());
    }

    @Test
    void findByIdShouldReturnMappedCouponWhenEntityExists() {
        UUID couponId = UUID.randomUUID();
        CouponEntity couponEntity = buildCouponEntity(couponId);
        when(couponRepository.findById(couponId)).thenReturn(Optional.of(couponEntity));

        Optional<Coupon> foundCoupon = couponRepositoryAdapter.findById(couponId);

        assertTrue(foundCoupon.isPresent());
        assertEquals(couponEntity.getId(), foundCoupon.get().getId());
        assertEquals(couponEntity.getCode(), foundCoupon.get().getCode());
        assertEquals(couponEntity.getDescription(), foundCoupon.get().getDescription());
        assertEquals(couponEntity.getDiscountValue(), foundCoupon.get().getDiscountValue());
        assertEquals(couponEntity.getExpirationDate(), foundCoupon.get().getExpirationDate());
        assertEquals(CouponStatus.ACTIVE, foundCoupon.get().getStatus());
        assertEquals(couponEntity.isPublished(), foundCoupon.get().isPublished());
        assertEquals(couponEntity.isRedeemed(), foundCoupon.get().isRedeemed());
    }

    @Test
    void findByIdShouldReturnEmptyWhenEntityDoesNotExist() {
        UUID couponId = UUID.randomUUID();
        when(couponRepository.findById(couponId)).thenReturn(Optional.empty());

        Optional<Coupon> foundCoupon = couponRepositoryAdapter.findById(couponId);

        assertTrue(foundCoupon.isEmpty());
    }

    private Coupon buildCoupon(UUID couponId) {
        Coupon coupon = new Coupon();
        coupon.setId(couponId);
        coupon.setCode("ABC123");
        coupon.setDescription("Black Friday coupon");
        coupon.setDiscountValue(BigDecimal.valueOf(10.50));
        coupon.setExpirationDate(FUTURE_EXPIRATION_DATE);
        coupon.setStatus(CouponStatus.ACTIVE);
        coupon.setPublished(true);
        coupon.setRedeemed(false);
        return coupon;
    }

    private CouponEntity buildCouponEntity(UUID couponId) {
        CouponEntity couponEntity = new CouponEntity();
        couponEntity.setId(couponId);
        couponEntity.setCode("ABC123");
        couponEntity.setDescription("Black Friday coupon");
        couponEntity.setDiscountValue(BigDecimal.valueOf(10.50));
        couponEntity.setExpirationDate(FUTURE_EXPIRATION_DATE);
        couponEntity.setStatus(CouponStatusEntity.ACTIVE);
        couponEntity.setPublished(true);
        couponEntity.setRedeemed(false);
        return couponEntity;
    }
}
