package com.monk.couponsystem.coupons;

import com.monk.couponsystem.models.Cart;
import com.monk.couponsystem.models.CartItem;
import com.monk.couponsystem.models.Product;
import com.monk.couponsystem.services.ProductService;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.Optional;

@Setter
@Getter
@CouponType(type = "product-wise")
public class ProductWise extends CouponDetails {
    private Double discount;
    private Long productId;

    @Override
    public boolean isValid(ProductService productService) {
        return productService.getProductById(productId).isPresent();
    }

    @Override
    public boolean isApplicable(Cart cart, ProductService productService) {
        for (CartItem item : cart.getItems()) {
            if (Objects.equals(item.getProductId(), productId) && item.getQuantity()>0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Double getDiscountAmount(Cart cart, ProductService productService) {
        populatePrices(cart, productService);
        for (CartItem item: cart.getItems()) {
            if (Objects.equals(item.getProductId(), productId)) {
                return item.getQuantity()* item.getPrice()* (discount /100.0);
            }
        }
        return 0.0;
    }

    @Override
    public void applyCouponDiscount(Cart cart, ProductService productService) {
        populatePrices(cart, productService);
        cart.getTotalPrice();

        for (CartItem item: cart.getItems()) {
            if (Objects.equals(item.getProductId(), productId)) {
                cart.setTotalDiscount(item.getQuantity()* item.getPrice()* (discount /100.0));
                item.setTotalDiscount(cart.getTotalDiscount());
                break;
            }
        }
        cart.setFinalPrice(cart.getTotalPrice()- cart.getTotalDiscount());
    }
}

