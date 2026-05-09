package com.outforce.coupon.infrastructure.adapter.in.controller;

import com.outforce.coupon.application.usecase.command.CreateCouponCommand;
import com.outforce.coupon.application.usecase.port.in.CouponUseCase;
import com.outforce.coupon.infrastructure.adapter.in.controller.request.CouponRequest;
import com.outforce.coupon.infrastructure.adapter.in.controller.response.CouponResponse;
import com.outforce.coupon.infrastructure.config.MyModelMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("coupon")
public class CouponController {

    private final CouponUseCase couponUseCase;
    private final MyModelMapper mapper;

    public CouponController(CouponUseCase couponUseCase, MyModelMapper mapper) {
        this.couponUseCase = couponUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<CouponResponse> create(@Valid @RequestBody CouponRequest couponRequest){
        CreateCouponCommand couponCommand = new CreateCouponCommand(
                couponRequest.getCode(),
                couponRequest.getDescription(),
                couponRequest.getDiscountValue(),
                couponRequest.getExpirationDate(),
                couponRequest.isPublished()
        );
        CouponResponse couponResponse = mapper.mapTo(couponUseCase.create(couponCommand), CouponResponse.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(couponResponse);
    }

    @GetMapping("{id}")
    public ResponseEntity<CouponResponse> findById(@PathVariable UUID id) {
        CouponResponse couponResponse = mapper.mapTo(couponUseCase.findById(id),CouponResponse.class);
        return ResponseEntity.status(HttpStatus.OK).body(couponResponse);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        couponUseCase.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
