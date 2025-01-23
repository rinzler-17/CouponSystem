package com.monk.couponsystem.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.monk.couponsystem.coupons.CouponDetails;
import com.monk.couponsystem.coupons.CouponType;
import jakarta.annotation.PostConstruct;
import org.reflections.Reflections;
import org.springframework.stereotype.Component;

@Component
public class CouponDetailsRegistry {

    private static final Map<String, Class<? extends CouponDetails>> couponTypeMap = new HashMap<>();

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
    public static void registerAnnotatedCouponTypes() throws Exception {
        Reflections reflections = new Reflections("");
        Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(CouponType.class);

        for (Class<?> clazz : annotatedClasses) {
            if (CouponDetails.class.isAssignableFrom(clazz)) {
                // Get the annotation
                CouponType annotation = clazz.getAnnotation(CouponType.class);
                couponTypeMap.put(annotation.type(), (Class<? extends CouponDetails>) clazz);
            }
        }
    }

    public static Class<? extends CouponDetails> getCouponTypeClass(String type) {
        return couponTypeMap.get(type);
    }
}