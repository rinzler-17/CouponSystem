package com.monk.couponsystem.factory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monk.couponsystem.coupons.*;
import com.monk.couponsystem.models.Coupon;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AbstractCouponDetailsFactory {
    private final ObjectMapper objectMapper;

    public CouponDetails getCouponDetails(Coupon coupon) {
        CouponDetails couponDetails;
        JsonNode details = coupon.getDetails();

            try {
                Class<? extends CouponDetails> clazz = CouponDetailsRegistry.getCouponTypeClass(coupon.getType());
                if (clazz == null) {
                    throw new RuntimeException(String.format("Unknown coupon type: %s", coupon.getType()));
                }
                couponDetails = objectMapper.treeToValue(details, clazz);
            } catch (Exception e) {
                throw new RuntimeException(String.format("Failed to parse details for coupon: %s", e));
            }
            couponDetails.setId(coupon.getId());
        return couponDetails;
    }
}
