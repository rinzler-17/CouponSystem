package com.monk.couponsystem.exceptions;

import lombok.AllArgsConstructor;

/*
This exception is thrown when coupon is invalid.
 */
@AllArgsConstructor
public class CouponInvalidException extends RuntimeException {
    String cause;
    public String getMessage() {
        return "coupon is invalid: " + cause;
    }
}
