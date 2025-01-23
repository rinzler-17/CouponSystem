package com.monk.couponsystem.coupons;

import com.monk.couponsystem.models.Cart;
import com.monk.couponsystem.models.CartItem;
import com.monk.couponsystem.models.Product;
import com.monk.couponsystem.services.ProductService;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Setter
@Getter
@CouponType(type = "cart-wise")
public class CartWise extends CouponDetails {
    private Double threshold;
    private Double discount;

    @Override
    public boolean isValid(ProductService productService) {
        return true;
    }

    @Override
    public boolean isApplicable(Cart cart, ProductService productService) {
        populatePrices(cart, productService);
        return cart.getTotalAmount() > threshold;
    }

    @Override
    public Double getDiscountAmount(Cart cart, ProductService productService) {
        if (isApplicable(cart, productService)) {
            return cart.getTotalAmount() * (discount/100.0);
        }
        return 0.0;
    }
}
