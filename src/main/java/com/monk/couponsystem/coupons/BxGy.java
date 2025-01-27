package com.monk.couponsystem.coupons;

import com.monk.couponsystem.exceptions.CouponInvalidException;
import com.monk.couponsystem.exceptions.NotFoundException;
import com.monk.couponsystem.models.Cart;
import com.monk.couponsystem.models.CartItem;
import com.monk.couponsystem.models.Product;
import com.monk.couponsystem.services.ProductService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Setter
@Getter
@CouponType(type = "bxgy")
public class BxGy extends AbstractCoupon {
    private List<Product> buyProducts;
    private List<Product> getProducts;
    private Integer repetitionLimit;

    @Override
    public void validate(ProductService productService) {
        // for bxgy to be valid, the product IDs from both lists should exist in product catalog
        try {
            for (Product product : buyProducts) {
                productService.getProductById(product.getProductId());
            }
            for (Product product : getProducts) {
                productService.getProductById(product.getProductId());
            }
        } catch (NotFoundException exception) {
            throw new CouponInvalidException(exception.getMessage());
        }
    }

    @Override
    public boolean isApplicable(Cart cart, ProductService productService) {
        validate(productService);

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
        cart.sortbyQuantity(true);
        Map<Long, Long> freeProductQuantity = getProducts.stream().collect(Collectors.toMap(Product::getProductId, Product::getQuantity));
        for (CartItem item : cart.getItems()) {
            if (freeProductQuantity.containsKey(item.getProductId())) {
                Product catalogProduct = productService.getProductById(item.getProductId());
                return Optional.of(Product.builder()
                        .price(catalogProduct.getPrice())
                        .productId(item.getProductId())
                        .quantity(freeProductQuantity.get(item.getProductId()))
                        .build());
            }
        }

        // select the free product with the max price
        Optional<Product> result = Optional.of(Product.builder().build());
        Double maxPrice = 0.0;
        for (Product product : getProducts) {
            product.setPrice(productService.getProductById(product.getProductId()).getPrice());
            if (product.getPrice()>maxPrice) {
                result.get().setProductId(product.getProductId());
                result.get().setPrice(product.getPrice());
                result.get().setQuantity(product.getQuantity());
            }
        }
        return result;
    }

    @Override
    public Double getDiscountAmount(Cart cart, ProductService productService) {
        validate(productService);

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
        validate(productService);

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
