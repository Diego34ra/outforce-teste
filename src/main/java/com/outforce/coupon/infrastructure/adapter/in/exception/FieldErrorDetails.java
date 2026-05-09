package com.outforce.coupon.infrastructure.adapter.in.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FieldErrorDetails {
    private String field;

    private String message;
}
