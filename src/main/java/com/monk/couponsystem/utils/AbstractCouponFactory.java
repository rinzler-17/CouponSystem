package com.monk.couponsystem.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monk.couponsystem.coupons.*;
import com.monk.couponsystem.models.CouponEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/*
This factory class helps in resolving a coupon type to a specific Coupon implementation.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AbstractCouponFactory {
    private final ObjectMapper objectMapper;
    private final CouponRegistry couponRegistry;

    public AbstractCoupon getCoupon(CouponEntity couponEntity) {
        AbstractCoupon coupon;
        JsonNode details = couponEntity.getDetails();

            try {
                Class<? extends AbstractCoupon> clazz = couponRegistry.getCouponTypeClass(couponEntity.getType());
                if (clazz == null) {
                    throw new RuntimeException(String.format("Unknown coupon type: %s", couponEntity.getType()));
                }
                coupon = objectMapper.treeToValue(details, clazz);
            } catch (Exception e) {
                throw new RuntimeException(String.format("Failed to parse details for coupon: %s", e));
            }
            coupon.setId(couponEntity.getId());
        return coupon;
    }
}
