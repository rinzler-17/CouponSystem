package com.monk.couponsystem.controllers;

import com.monk.couponsystem.models.ApplicableCoupon;
import com.monk.couponsystem.models.CouponEntity;
import com.monk.couponsystem.models.Cart;
import com.monk.couponsystem.services.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CouponController {

    @Autowired
    private CouponService couponService;

    // Create coupon
    @PostMapping("/coupons")
    public CouponEntity createCoupon(@RequestBody CouponEntity couponEntity) {
        return couponService.createCoupon(couponEntity);
    }

    // Get all coupons
    @GetMapping("/coupons")
    public List<CouponEntity> getAllCoupons() {
        return couponService.getAllCoupons();
    }

    // Get coupon by id
    @GetMapping("/coupons/{id}")
    public CouponEntity getCouponById(@PathVariable Long id) {
        return couponService.getCouponById(id);
    }

    // Update coupon by id
    @PutMapping("/coupons/{id}")
    public CouponEntity updateCoupon(@PathVariable Long id, @RequestBody CouponEntity updatedCouponEntity) {
        return couponService.updateCoupon(id, updatedCouponEntity);
    }

    // Delete coupon by id
    @DeleteMapping("/coupons/{id}")
    public void deleteCoupon(@PathVariable Long id) {
        couponService.deleteCoupon(id);
    }


    // Get applicable coupons
    @PostMapping("/applicable-coupons")
    public List<ApplicableCoupon> getApplicableCoupons(@RequestBody Cart cart) {
        return couponService.getApplicableCoupons(cart);
    }

    // Apply coupon to cart
    @PostMapping("/apply-coupon/{id}")
    public Cart applyCouponToCart(@PathVariable Long id, @RequestBody Cart cart) {
        return couponService.applyCouponToCart(id, cart);
    }
}
