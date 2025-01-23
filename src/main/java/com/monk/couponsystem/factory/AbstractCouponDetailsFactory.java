package com.monk.couponsystem.factory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monk.couponsystem.coupons.BxGy;
import com.monk.couponsystem.coupons.CartWise;
import com.monk.couponsystem.coupons.CouponDetails;
import com.monk.couponsystem.coupons.ProductWise;
import com.monk.couponsystem.models.Coupon;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class AbstractCouponDetailsFactory {
    private final ObjectMapper objectMapper;

    public AbstractCouponDetailsFactory(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    public CouponDetails getCouponDetails(Coupon coupon) {
        CouponDetails couponDetails;
        JsonNode details = coupon.getDetails();

            try {
                couponDetails = switch (coupon.getType()) {
                    case CART_WISE -> objectMapper.treeToValue(details, CartWise.class);
                    case PRODUCT_WISE -> objectMapper.treeToValue(details, ProductWise.class);
                    case BXGY -> objectMapper.treeToValue(details, BxGy.class);
                    default -> throw new IllegalArgumentException("Unknown coupon type: " + coupon.getType());
                };
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse details for coupon: " + coupon.getId(), e);
            }
            couponDetails.setId(coupon.getId());
        return couponDetails;
    }
}
