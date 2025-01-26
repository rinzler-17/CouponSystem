package com.monk.couponsystem.services;

import com.monk.couponsystem.coupons.AbstractCoupon;
import com.monk.couponsystem.utils.AbstractCouponFactory;
import com.monk.couponsystem.models.*;
import com.monk.couponsystem.repositories.CouponRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponService {

    @Autowired
    private final CouponRepository couponRepository;

    @Autowired
    private final ProductService productService;

    @Autowired
    private final AbstractCouponFactory couponFactory;

    // TODO: anti pattern: service class should not return response entity, handle exception here
    public ResponseEntity<?> createCoupon(CouponEntity couponEntity) {
        AbstractCoupon coupon = couponFactory.getCoupon(couponEntity);
        if (!coupon.isValid(productService)) {
            return ResponseEntity.badRequest().body("coupon is invalid");
        }
        return ResponseEntity.ok(couponRepository.save(couponEntity));
    }

    public List<CouponEntity> getAllCoupons() {
        return couponRepository.findAll();
    }

    public Optional<CouponEntity> getCouponById(Long id) {
        return couponRepository.findById(id);
    }

    public CouponEntity updateCoupon(Long id, CouponEntity updatedCouponEntity) {
        Optional<CouponEntity> existingCoupon = couponRepository.findById(id);
        if (existingCoupon.isEmpty()) {
            throw new RuntimeException("Coupon not found");
        }
        updatedCouponEntity.setId(id);
        return couponRepository.save(updatedCouponEntity);
    }

    public void deleteCoupon(Long id) {
        couponRepository.deleteById(id); // Delete coupon by ID
    }

    public List<ApplicableCoupon> getApplicableCoupons(Cart cart) {

        //log.info(cart.toString());
        List<ApplicableCoupon> applicableCoupons = new ArrayList<>();
        List<CouponEntity> couponEntities = couponRepository.findAll();
        if (couponEntities.isEmpty()) {
            return applicableCoupons;
        }

        for (CouponEntity couponEntity : couponEntities) {
            AbstractCoupon coupon = couponFactory.getCoupon(couponEntity);
                if (coupon.isApplicable(cart, productService)) {
                    applicableCoupons.add(ApplicableCoupon.builder()
                            .id(coupon.getId())
                            .type(coupon.getType())
                            .discount(coupon.getDiscountAmount(cart, productService))
                            .build());
                }
        }
        return applicableCoupons;
    }

    public Cart applyCouponToCart(Long id, Cart cart) {
        Optional<CouponEntity> couponEntity = couponRepository.findById(id);
        if (couponEntity.isEmpty()) {
            throw new RuntimeException("Coupon not found");
        }

        AbstractCoupon coupon = couponFactory.getCoupon(couponEntity.get());
        coupon.applyCouponDiscount(cart, productService);

        // Initialize null values to 0
        for (CartItem item: cart.getItems()) {
            if (item.getTotalDiscount()==null) {
                item.setTotalDiscount(0.0);
            }
        }
        return cart;
    }
}
