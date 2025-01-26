package com.monk.couponsystem.coupons;

import com.monk.couponsystem.models.Cart;
import com.monk.couponsystem.models.CartItem;
import com.monk.couponsystem.models.Product;
import com.monk.couponsystem.services.ProductService;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

/*
This is the abstract class defining abstract methods for implementing a discount coupon. All coupon types and
their subsequent implementations must extend this class.
 */
@Setter
@Getter
public abstract class AbstractCoupon {
    protected Long id;

    // validates coupon with the current inventory
    public abstract boolean isValid(ProductService productService);

    // checks if coupon is applicable on a cart
    public abstract boolean isApplicable(Cart cart, ProductService productService);

    // computes the discount amount
    public abstract Double getDiscountAmount(Cart cart, ProductService productService);

    // applies the coupon discount and updates the cart
    public abstract void applyCouponDiscount(Cart cart, ProductService productService);

    // populate cart item prices from product inventory
    public void populatePrices(Cart cart, ProductService productService) {
        for (CartItem item: cart.getItems()) {
            Optional<Product> product = productService.getProductById(item.getProductId());
            if (product.isEmpty()) {
                // TODO: create response entity accordingly based on exception
                throw new RuntimeException(String.format("Cart item %d not found in product inventory", item.getProductId()));
            } else if (product.get().getQuantity() < item.getQuantity()) {
                throw new RuntimeException(String.format("Insufficient quantity for cart item %d in product inventory", item.getProductId()));
            }
            item.setPrice(product.get().getPrice());
        }
    }


    // returns the coupon type
    public String getType() {
        Class<? extends AbstractCoupon> clazz = this.getClass();
        if (clazz.isAnnotationPresent(CouponType.class)) {
            CouponType annotation = clazz.getAnnotation(CouponType.class);
            return annotation.type();
        }
        return "";
    }
}
