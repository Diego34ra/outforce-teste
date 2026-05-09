package com.outforce.coupon.infrastructure.adapter.in.controller;

import com.outforce.coupon.application.usecase.command.CreateCouponCommand;
import com.outforce.coupon.application.usecase.port.in.CouponUseCase;
import com.outforce.coupon.infrastructure.adapter.in.controller.request.CouponRequest;
import com.outforce.coupon.infrastructure.adapter.in.controller.response.CouponResponse;
import com.outforce.coupon.infrastructure.adapter.in.exception.ErrorDetails;
import com.outforce.coupon.infrastructure.config.MyModelMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("coupon")
@Tag(name = "Coupon", description = "Operations for managing coupons")
public class CouponController {

    private final CouponUseCase couponUseCase;
    private final MyModelMapper mapper;

    public CouponController(CouponUseCase couponUseCase, MyModelMapper mapper) {
        this.couponUseCase = couponUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    @Operation(
            summary = "Create coupon",
            description = "Creates a coupon, sanitizes the code before persistence, and returns the created coupon."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Coupon created successfully",
                    content = @Content(schema = @Schema(implementation = CouponResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request or business rule violation",
                    content = @Content(schema = @Schema(implementation = ErrorDetails.class))
            )
    })
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
    @Operation(
            summary = "Find coupon by id",
            description = "Returns a coupon by its unique identifier."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Coupon found successfully",
                    content = @Content(schema = @Schema(implementation = CouponResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Coupon not found",
                    content = @Content(schema = @Schema(implementation = ErrorDetails.class))
            )
    })
    public ResponseEntity<CouponResponse> findById(@PathVariable UUID id) {
        CouponResponse couponResponse = mapper.mapTo(couponUseCase.findById(id),CouponResponse.class);
        return ResponseEntity.status(HttpStatus.OK).body(couponResponse);
    }

    @DeleteMapping("{id}")
    @Operation(
            summary = "Delete coupon",
            description = "Performs a soft delete on a coupon. A deleted coupon cannot be deleted again."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Coupon deleted successfully"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Coupon not found",
                    content = @Content(schema = @Schema(implementation = ErrorDetails.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Coupon has already been deleted",
                    content = @Content(schema = @Schema(implementation = ErrorDetails.class))
            )
    })
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        couponUseCase.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
