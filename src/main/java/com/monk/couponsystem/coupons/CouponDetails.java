package com.monk.couponsystem.coupons;

import com.monk.couponsystem.models.Cart;
import com.monk.couponsystem.services.ProductService;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class CouponDetails {
    protected Long id;

    public abstract boolean isValid(ProductService productService);

    public abstract boolean isApplicable(Cart cart);

    public abstract Double getDiscountAmount(Cart cart);

    public abstract CouponType getType();

}
