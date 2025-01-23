package com.monk.couponsystem.coupons;

import com.monk.couponsystem.models.Cart;
import com.monk.couponsystem.models.CartItem;
import com.monk.couponsystem.models.Product;
import com.monk.couponsystem.services.ProductService;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
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

        Map<Long, Long> cartItemQuantity = cart.getItems().stream().collect(Collectors.toMap(CartItem::getProductId,
                CartItem::getQuantity));

        for (Product product : buyProducts) {
            // if any one of the buyProducts are having quantity greater than required, coupon is applicable
            if (cartItemQuantity.getOrDefault(product.getProductId(), 0L)>=product.getQuantity()) {
                return true;
            }
        }
        return false;
    }

    public long getCouponRepetitionCount(Cart cart) {
        long repetition=0;
        Map<Long, Long> cartItemQuantity = cart.getItems().stream().collect(Collectors.toMap(CartItem::getProductId,
                CartItem::getQuantity));
        for (Product product : buyProducts) {
            repetition += cartItemQuantity.getOrDefault(product.getProductId(), 0L)/product.getQuantity();
            if (repetition>repetitionLimit) {
                repetition = repetitionLimit;
                break;
            }
        }
        return repetition;
    }

    public Optional<Product> selectProductForDiscount(Cart cart, ProductService productService) {
        populatePrices(cart, productService);

        // consider product with highest quantity in the cart for free discount
        // if no product from getproducts are present in the cart, just use the product with the maximum price for free discount
        cart.sortByQuantity();
        Map<Long, Long> freeProductQuantity = getProducts.stream().collect(Collectors.toMap(Product::getProductId, Product::getQuantity));
        for (CartItem item : cart.getItems()) {
            if (freeProductQuantity.containsKey(item.getProductId())) {
                return Optional.of(Product.builder()
                        .price(item.getPrice())
                        .productId(item.getProductId())
                        .quantity(freeProductQuantity.get(item.getProductId()))
                        .build());
                //return item.getPrice() * freeProductQuantity.get(item.getProductId()) * repetition;
            }
        }

        // get prices of all possible free products
        for (Product product : getProducts) {
            product.setPrice(productService.getProductById(product.getProductId()).get().getPrice());
        }
        return getProducts.stream().max(Comparator.comparingDouble(Product::getPrice));
            //    return mostExpProduct.get().getPrice() * mostExpProduct.get().getQuantity() * repetition;
    }

    @Override
    public Double getDiscountAmount(Cart cart, ProductService productService) {
        long repetition = getCouponRepetitionCount(cart);
        if (repetition==0) {
            return 0.0;
        }

        Optional<Product> freeProduct = selectProductForDiscount(cart, productService);
        if (freeProduct.isEmpty()) {
            return 0.0;
        }
        return freeProduct.get().getPrice() * freeProduct.get().getQuantity() * repetition;
    }

    @Override
    public void applyCouponDiscount(Cart cart, ProductService productService) {
        long repetition = getCouponRepetitionCount(cart);
        if (repetition==0) {
            return;
        }

        Optional<Product> freeProduct = selectProductForDiscount(cart, productService);
        if (freeProduct.isEmpty()) {
            return;
        }
        Double discount = repetition * freeProduct.get().getQuantity() * freeProduct.get().getPrice();
        cart.setTotalDiscount(discount);

        for (CartItem item: cart.getItems()) {
            if (Objects.equals(item.getProductId(), freeProduct.get().getProductId())) {
                item.setQuantity(item.getQuantity() + (repetition * freeProduct.get().getQuantity()));
                item.setTotalDiscount(discount);
                cart.setFinalPrice(cart.getTotalPrice()-discount);
                return;
            }
        }

        cart.addItem(CartItem.builder()
                .price(freeProduct.get().getPrice())
                .productId(freeProduct.get().getProductId())
                .quantity(repetition * freeProduct.get().getQuantity())
                .totalDiscount(discount).build());
        cart.setFinalPrice(cart.getTotalPrice()-discount);
    }
}
