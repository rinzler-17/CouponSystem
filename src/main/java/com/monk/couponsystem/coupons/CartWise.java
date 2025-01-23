package com.monk.couponsystem.coupons;

import com.monk.couponsystem.models.Cart;
import com.monk.couponsystem.models.CartItem;
import com.monk.couponsystem.services.ProductService;
import lombok.Getter;
import lombok.Setter;

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
        return cart.getTotalPrice() > threshold;
    }

    @Override
    public Double getDiscountAmount(Cart cart, ProductService productService) {
        if (isApplicable(cart, productService)) {
            return cart.getTotalPrice() * (discount/100.0);
        }
        return 0.0;
    }

    @Override
    public void applyCouponDiscount(Cart cart, ProductService productService) {
        populatePrices(cart, productService);
        cart.setTotalDiscount(cart.getTotalPrice() * (discount/100.0));
        cart.setFinalPrice(cart.getTotalPrice() - cart.getTotalDiscount());
        for (CartItem item: cart.getItems()) {
            item.setTotalDiscount(item.getPrice() * item.getQuantity() * (discount/100.0));
        }
    }
}
