package com.monk.couponsystem.coupons;

import com.monk.couponsystem.models.Cart;
import com.monk.couponsystem.models.CartItem;
import com.monk.couponsystem.models.Product;
import com.monk.couponsystem.services.ProductService;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Setter
@Getter
public class BxGy extends CouponDetails {
    private List<Product> buyProducts;
    private List<Product> getProducts;
    private Integer repetitionLimit;

    @Override
    public boolean isValid(ProductService productService) {
        for (Product product : buyProducts) {
            if (productService.getProductById(product.getProductId()).isEmpty()) {
                return false;
            }
        }
        for (Product product : getProducts) {
            if (productService.getProductById(product.getProductId()).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isApplicable(Cart cart) {
        // Assuming cart has price information for all the products
        Map<Long, Integer> cartItemQuantity = cart.getItems().stream().collect(Collectors.toMap(CartItem::getProductId,
                CartItem::getQuantity));

        for (Product product : buyProducts) {
            // if any one of the buyProducts are having quantity greater than required, coupon is applicable
            if (cartItemQuantity.getOrDefault(product.getProductId(), 0)>=product.getQuantity()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Double getDiscountAmount(Cart cart) {
        // TODO
        return 0.0;
    }

    @Override
    public CouponType getType() {
        return CouponType.BXGY;
    }

}
