package com.monk.couponsystem.services;

import com.monk.couponsystem.coupons.CouponDetails;
import com.monk.couponsystem.utils.AbstractCouponDetailsFactory;
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
    private final AbstractCouponDetailsFactory couponDetailsFactory;

    // TODO: anti pattern: service class should not return response entity, handle exception here
    public ResponseEntity<?> createCoupon(Coupon coupon) {
        CouponDetails couponDetails = couponDetailsFactory.getCouponDetails(coupon);
        if (!couponDetails.isValid(productService)) {
            return ResponseEntity.badRequest().body("coupon is invalid");
        }
        return ResponseEntity.ok(couponRepository.save(coupon));
    }

    public List<Coupon> getAllCoupons() {
        return couponRepository.findAll();
    }

    public Optional<Coupon> getCouponById(Long id) {
        return couponRepository.findById(id);
    }

    public Coupon updateCoupon(Long id, Coupon updatedCoupon) {
        Optional<Coupon> existingCoupon = couponRepository.findById(id);
        if (existingCoupon.isEmpty()) {
            throw new RuntimeException("Coupon not found");
        }
        updatedCoupon.setId(id);
        return couponRepository.save(updatedCoupon);
    }

    public void deleteCoupon(Long id) {
        couponRepository.deleteById(id); // Delete coupon by ID
    }

    public List<ApplicableCoupon> getApplicableCoupons(Cart cart) {

        //log.info(cart.toString());
        List<ApplicableCoupon> applicableCoupons = new ArrayList<>();
        List<Coupon> coupons = couponRepository.findAll();
        if (coupons.isEmpty()) {
            return applicableCoupons;
        }

        for (Coupon coupon: coupons) {
            CouponDetails couponDetails = couponDetailsFactory.getCouponDetails(coupon);
                if (couponDetails.isApplicable(cart, productService)) {
                    applicableCoupons.add(ApplicableCoupon.builder()
                            .id(couponDetails.getId())
                            .type(couponDetails.getType())
                            .discount(couponDetails.getDiscountAmount(cart, productService))
                            .build());
                }
        }
        return applicableCoupons;
    }

    public Cart applyCouponToCart(Long id, Cart cart) {
        Optional<Coupon> coupon = couponRepository.findById(id);
        if (coupon.isEmpty()) {
            throw new RuntimeException("Coupon not found");
        }

        CouponDetails couponDetails = couponDetailsFactory.getCouponDetails(coupon.get());
        couponDetails.applyCouponDiscount(cart, productService);

        // Initialize null values to 0
        for (CartItem item: cart.getItems()) {
            if (item.getTotalDiscount()==null) {
                item.setTotalDiscount(0.0);
            }
        }
        return cart;
    }
}
