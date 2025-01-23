package com.monk.couponsystem.coupons;

import com.monk.couponsystem.models.Cart;
import com.monk.couponsystem.models.CartItem;
import com.monk.couponsystem.models.Product;
import com.monk.couponsystem.services.ProductService;
import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Setter
@Getter
@CouponType(type = "bxgy")
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
    public boolean isApplicable(Cart cart, ProductService productService) {

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
    public Double getDiscountAmount(Cart cart, ProductService productService) {
        populatePrices(cart, productService);
        long repetition=0;
        Map<Long, Integer> cartItemQuantity = cart.getItems().stream().collect(Collectors.toMap(CartItem::getProductId,
                CartItem::getQuantity));
        for (Product product : buyProducts) {
            repetition += cartItemQuantity.getOrDefault(product.getProductId(), 0)/product.getQuantity();
            if (repetition>repetitionLimit) {
                repetition = repetitionLimit;
                break;
            }
        }
        if (repetition==0) {
            return 0.0;
        }

        // consider product with highest quantity in the cart for free discount
        // if none of the get products are present, just use the product with the maximum price in the discount
        cart.sortByQuantity();
        Map<Long, Long> freeProductQuantity = getProducts.stream().collect(Collectors.toMap(Product::getProductId, Product::getQuantity));
        for (CartItem item : cart.getItems()) {
            if (freeProductQuantity.containsKey(item.getProductId())) {
                return item.getPrice() * freeProductQuantity.get(item.getProductId()) * repetition;
            }
        }

        // get prices of all possible free products
        for (Product product : getProducts) {
            product.setPrice(productService.getProductById(product.getProductId()).get().getPrice());
        }
        Optional<Product> mostExpProduct = getProducts.stream().max(Comparator.comparingDouble(Product::getPrice));

        if (mostExpProduct.isPresent()) {
            return mostExpProduct.get().getPrice() * mostExpProduct.get().getQuantity() * repetition;
        }
        return 0.0;
    }
}
