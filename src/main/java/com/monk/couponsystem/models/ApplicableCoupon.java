package com.monk.couponsystem.models;

import com.monk.couponsystem.coupons.CouponType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApplicableCoupon {
    private Long id;
    private CouponType type;
    private Double discount;
}
