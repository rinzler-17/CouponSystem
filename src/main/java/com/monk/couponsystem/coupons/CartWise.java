package com.monk.couponsystem.coupons;

import com.monk.couponsystem.models.Cart;
import com.monk.couponsystem.services.ProductService;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CartWise extends CouponDetails {
    private Double threshold;
    private Double discount;

    @Override
    public boolean isValid(ProductService productService) {
        return true;
    }

    @Override
    public boolean isApplicable(Cart cart) {
        return cart.getTotalAmount() > threshold;
    }

    @Override
    public Double getDiscountAmount(Cart cart) {
        if (isApplicable(cart)) {
            return cart.getTotalAmount() * (discount/100.0);
        }
        return 0.0;
    }

    @Override
    public CouponType getType() {
        return CouponType.CART_WISE;
    }
}
