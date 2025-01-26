package com.monk.couponsystem.controllers;

import com.monk.couponsystem.models.ApplicableCoupon;
import com.monk.couponsystem.models.CouponEntity;
import com.monk.couponsystem.models.Cart;
import com.monk.couponsystem.services.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class CouponController {

    @Autowired
    private CouponService couponService;

    // TODO: add error handling

    // Create coupon
    @PostMapping("/coupons")
    public ResponseEntity<?> createCoupon(@RequestBody CouponEntity couponEntity) {
        return couponService.createCoupon(couponEntity);
    }

    // Get all coupons
    @GetMapping("/coupons")
    public ResponseEntity<List<CouponEntity>> getAllCoupons() {
        return ResponseEntity.ok(couponService.getAllCoupons());
    }

    // Get coupon by id
    @GetMapping("/coupons/{id}")
    public ResponseEntity<CouponEntity> getCouponById(@PathVariable Long id) {
        Optional<CouponEntity> coupon = couponService.getCouponById(id);
        return coupon.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Update coupon by id
    @PutMapping("/coupons/{id}")
    public ResponseEntity<CouponEntity> updateCoupon(@PathVariable Long id, @RequestBody CouponEntity updatedCouponEntity) {
        return ResponseEntity.ok(couponService.updateCoupon(id, updatedCouponEntity));
    }

    // Delete coupon by id
    @DeleteMapping("/coupons/{id}")
    public ResponseEntity<Void> deleteCoupon(@PathVariable Long id) {
        couponService.deleteCoupon(id);
        return ResponseEntity.ok().build();
    }


    // Get applicable coupons
    @PostMapping("/applicable-coupons")
    public ResponseEntity<List<ApplicableCoupon>> getApplicableCoupons(@RequestBody Cart cart) {
        return ResponseEntity.ok(couponService.getApplicableCoupons(cart));
    }

    // Apply coupon to cart
    @PostMapping("/apply-coupon/{id}")
    public ResponseEntity<Cart> applyCouponToCart(@PathVariable Long id, @RequestBody Cart cart) {
        return ResponseEntity.ok(couponService.applyCouponToCart(id, cart));
    }
}
