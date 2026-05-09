package com.outforce.coupon.application.usecase;

import com.outforce.coupon.application.usecase.command.CreateCouponCommand;
import com.outforce.coupon.application.usecase.port.out.CouponRepositoryPort;
import com.outforce.coupon.domain.entity.Coupon;
import com.outforce.coupon.domain.enums.CouponStatus;
import com.outforce.coupon.domain.exception.BusinessRuleViolationException;
import com.outforce.coupon.domain.exception.CouponNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class CouponUseCaseImplTest {

    private static final Instant FUTURE_EXPIRATION_DATE = Instant.parse("2026-05-10T12:00:00Z");

    @Mock
    private CouponRepositoryPort couponRepositoryPort;

    @InjectMocks
    private CouponUseCaseImpl couponUseCase;

    @Test
    void createShouldBuildCouponAndSaveIt() {
        CreateCouponCommand command = new CreateCouponCommand(
                "ABC-123",
                "Black Friday coupon",
                BigDecimal.valueOf(0.8),
                FUTURE_EXPIRATION_DATE,
                true
        );
        when(couponRepositoryPort.save(any(Coupon.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Coupon createdCoupon = couponUseCase.create(command);

        ArgumentCaptor<Coupon> couponCaptor = ArgumentCaptor.forClass(Coupon.class);
        verify(couponRepositoryPort).save(couponCaptor.capture());
        Coupon savedCoupon = couponCaptor.getValue();

        assertNotNull(savedCoupon.getId());
        assertEquals("ABC123", savedCoupon.getCode());
        assertEquals(CouponStatus.ACTIVE, savedCoupon.getStatus());
        assertTrue(savedCoupon.isPublished());
        assertFalse(savedCoupon.isRedeemed());
        assertSame(savedCoupon, createdCoupon);
    }

    @Test
    void findByIdShouldReturnCouponWhenItExists() {
        UUID couponId = UUID.randomUUID();
        Coupon coupon = new Coupon();
        coupon.setId(couponId);
        when(couponRepositoryPort.findById(couponId)).thenReturn(Optional.of(coupon));

        Coupon foundCoupon = couponUseCase.findById(couponId);

        assertSame(coupon, foundCoupon);
    }

    @Test
    void findByIdShouldThrowWhenCouponDoesNotExist() {
        UUID couponId = UUID.randomUUID();
        when(couponRepositoryPort.findById(couponId)).thenReturn(Optional.empty());

        CouponNotFoundException exception = assertThrows(
                CouponNotFoundException.class,
                () -> couponUseCase.findById(couponId)
        );

        assertEquals("Coupon not found", exception.getMessage());
    }

    @Test
    void deleteShouldMarkCouponAsDeletedAndSaveIt() {
        UUID couponId = UUID.randomUUID();
        Coupon coupon = Coupon.create(
                "ABC123",
                "Coupon to delete",
                BigDecimal.valueOf(0.8),
                FUTURE_EXPIRATION_DATE,
                false
        );
        when(couponRepositoryPort.findById(couponId)).thenReturn(Optional.of(coupon));
        when(couponRepositoryPort.save(any(Coupon.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        couponUseCase.delete(couponId);

        verify(couponRepositoryPort).save(coupon);
        assertEquals(CouponStatus.DELETED, coupon.getStatus());
        assertNotNull(coupon.getDeletedAt());
    }

    @Test
    void deleteShouldThrowWhenCouponDoesNotExist() {
        UUID couponId = UUID.randomUUID();
        when(couponRepositoryPort.findById(couponId)).thenReturn(Optional.empty());

        CouponNotFoundException exception = assertThrows(
                CouponNotFoundException.class,
                () -> couponUseCase.delete(couponId)
        );

        assertEquals("Coupon not found", exception.getMessage());
        verify(couponRepositoryPort, never()).save(any(Coupon.class));
    }

    @Test
    void deleteShouldThrowWhenCouponHasAlreadyBeenDeleted() {
        UUID couponId = UUID.randomUUID();
        Coupon coupon = Coupon.create(
                "ABC123",
                "Already deleted coupon",
                BigDecimal.valueOf(0.8),
                FUTURE_EXPIRATION_DATE,
                false
        );
        coupon.delete();
        when(couponRepositoryPort.findById(couponId)).thenReturn(Optional.of(coupon));

        BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,
                () -> couponUseCase.delete(couponId)
        );

        assertEquals("Coupon has already been deleted", exception.getMessage());
        verify(couponRepositoryPort, never()).save(any(Coupon.class));
    }
}
