package com.monk.couponsystem.exceptions;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CouponInvalidException extends RuntimeException {
    String cause;
    public String getMessage() {
        return "coupon is invalid: " + cause;
    }
}
