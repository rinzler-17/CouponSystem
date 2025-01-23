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
public class ProductWise extends CouponDetails {
    private Double discount;
    private Long productId;

    @Override
    public boolean isValid(ProductService productService) {
        return productService.getProductById(productId).isPresent();
    }

    @Override
    public boolean isApplicable(Cart cart) {
        for (CartItem item : cart.getItems()) {
            if (Objects.equals(item.getProductId(), productId) && item.getQuantity()>0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Double getDiscountAmount(Cart cart) {
        for (CartItem item: cart.getItems()) {
            if (Objects.equals(item.getProductId(), productId)) {
                return item.getQuantity()* item.getPrice()* (discount /100.0);
            }
        }
        return 0.0;
    }

    @Override
    public CouponType getType() {
        return CouponType.PRODUCT_WISE;
    }
}

