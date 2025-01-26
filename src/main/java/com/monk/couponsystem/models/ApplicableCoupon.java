package com.monk.couponsystem.models;

import lombok.Builder;
import lombok.Data;

/*
This represents a coupon when applied to a cart to compute discount it offers.
 */
@Data
@Builder
public class ApplicableCoupon {
    private Long id;
    private String type;
    private Double discount;
}
