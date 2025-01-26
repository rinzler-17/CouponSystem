package com.monk.couponsystem.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.monk.couponsystem.coupons.AbstractCoupon;
import com.monk.couponsystem.coupons.CouponType;
import jakarta.annotation.PostConstruct;
import org.reflections.Reflections;
import org.springframework.stereotype.Component;

/*
This registry keeps a map from coupon type to its corresponding Coupon implementation.
 */
@Component
public class CouponRegistry {

    private static final Map<String, Class<? extends AbstractCoupon>> couponTypeMap = new HashMap<>();

    @PostConstruct
    public void initialize() {
        try {
            registerAnnotatedCouponTypes();
        } catch (Exception e) {
            throw new RuntimeException("Failed to register coupons on startup", e);
        }
    }

    // Automatically scans packages and registers couponType to the subtype extending CouponDetails
    // Class must be annotated with CouponType annotation
    @SuppressWarnings("unchecked")
    public void registerAnnotatedCouponTypes() throws Exception {
        Reflections reflections = new Reflections("");
        Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(CouponType.class);

        for (Class<?> clazz : annotatedClasses) {
            if (AbstractCoupon.class.isAssignableFrom(clazz)) {
                // Get the annotation
                CouponType annotation = clazz.getAnnotation(CouponType.class);
                couponTypeMap.put(annotation.type(), (Class<? extends AbstractCoupon>) clazz);
            }
        }
    }

    public Class<? extends AbstractCoupon> getCouponTypeClass(String type) {
        return couponTypeMap.get(type);
    }
}