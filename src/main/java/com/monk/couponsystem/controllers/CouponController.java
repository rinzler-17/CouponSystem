package com.monk.couponsystem.controllers;

import com.monk.couponsystem.models.ApplicableCoupon;
import com.monk.couponsystem.models.Coupon;
import com.monk.couponsystem.models.Cart;
import com.monk.couponsystem.services.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/coupons")
public class CouponController {

    @Autowired
    private CouponService couponService;

    // TODO: add error handling

    // Create coupon
    @PostMapping
    public ResponseEntity<?> createCoupon(@RequestBody Coupon coupon) {
        return couponService.createCoupon(coupon);
    }

    // Get all coupons
    @GetMapping
    public ResponseEntity<List<Coupon>> getAllCoupons() {
        return ResponseEntity.ok(couponService.getAllCoupons());
    }

    // Get coupon by id
    @GetMapping("/{id}")
    public ResponseEntity<Coupon> getCouponById(@PathVariable Long id) {
        Optional<Coupon> coupon = couponService.getCouponById(id);
        return coupon.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Update coupon by id
    @PutMapping("/{id}")
    public ResponseEntity<Coupon> updateCoupon(@PathVariable Long id, @RequestBody Coupon updatedCoupon) {
        return ResponseEntity.ok(couponService.updateCoupon(id, updatedCoupon));
    }

    // Delete coupon by id
    @DeleteMapping("/{id}")
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
