package com.monk.couponsystem.coupons;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CouponType {
    String type(); // This will hold the type value
}

