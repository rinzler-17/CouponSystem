package com.monk.couponsystem.coupons;

import com.monk.couponsystem.exceptions.NotFoundException;
import com.monk.couponsystem.models.Cart;
import com.monk.couponsystem.models.CartItem;
import com.monk.couponsystem.models.Product;
import com.monk.couponsystem.services.ProductService;
import lombok.Getter;
import lombok.Setter;

/*
This is the abstract class defining abstract methods for implementing a discount coupon. All coupon types and
their subsequent implementations must extend this class.
 */
@Setter
@Getter
public abstract class AbstractCoupon {
    protected Long id;

    // validates coupon with the current inventory
    public void validate(ProductService productService) {
        // empty STUB
    }

    // checks if coupon is applicable on a cart
    public abstract boolean isApplicable(Cart cart, ProductService productService);

    // computes the discount amount
    public abstract Double getDiscountAmount(Cart cart, ProductService productService);

    // applies the coupon discount and updates the cart
    public abstract void applyCouponDiscount(Cart cart, ProductService productService);

    // populate item prices from product catalog
    public void populatePrices(Cart cart, ProductService productService) {
        for (CartItem item: cart.getItems()) {
            try {
                Product product = productService.getProductById(item.getProductId());
                item.setPrice(product.getPrice());
            } catch (NotFoundException exp) {
                throw new RuntimeException(String.format("item %d not found in product catalog", item.getProductId()));
            }
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
