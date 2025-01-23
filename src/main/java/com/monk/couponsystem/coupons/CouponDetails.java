package com.monk.couponsystem.coupons;

import com.monk.couponsystem.models.Cart;
import com.monk.couponsystem.services.ProductService;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class CouponDetails {
    protected Long id;

    // validates coupon with the current inventory
    public abstract boolean isValid(ProductService productService);

    // checks if coupon is applicable on a cart
    public abstract boolean isApplicable(Cart cart);

    // computes the discount amount
    public abstract Double getDiscountAmount(Cart cart);

    // returns the coupon type
    public String getType() {
        Class<? extends CouponDetails> clazz = this.getClass();
        if (clazz.isAnnotationPresent(CouponType.class)) {
            CouponType annotation = clazz.getAnnotation(CouponType.class);
            return annotation.type();
        }
        return "";
    }
}
