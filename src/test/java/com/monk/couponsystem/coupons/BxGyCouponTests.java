package com.monk.couponsystem.coupons;

import com.monk.couponsystem.models.Cart;
import com.monk.couponsystem.models.CartItem;
import com.monk.couponsystem.models.Product;
import com.monk.couponsystem.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class BxGyCouponTest {

    @Mock
    private ProductService productService;

    List<CartItem> items;

    private final BxGy bxgyCoupon = new BxGy();

    @BeforeEach
    public void setup() {
        items = new ArrayList<>();
        List<Product> buyProducts = new ArrayList<>();
        buyProducts.add(Product.builder().productId(1L).quantity(2L).build());
        buyProducts.add(Product.builder().productId(3L).quantity(3L).build());
        bxgyCoupon.setBuyProducts(buyProducts);

        List<Product> getProducts = new ArrayList<>();
        getProducts.add(Product.builder().productId(2L).quantity(2L).build());
        bxgyCoupon.setGetProducts(getProducts);
        bxgyCoupon.setRepetitionLimit(3);

        Mockito.when(productService.getProductById(1L)).thenReturn(Optional.of(Product.builder()
                .productId(1L)
                .quantity(20L)
                .price(25.0).build()));

        Mockito.when(productService.getProductById(2L)).thenReturn(Optional.of(Product.builder()
                .productId(2L)
                .quantity(40L)
                .price(100.0).build()));

        Mockito.when(productService.getProductById(3L)).thenReturn(Optional.of(Product.builder()
                .productId(3L)
                .quantity(10L)
                .price(50.0).build()));
    }

    @Test
    public void isValidTest() {
        assertThat(bxgyCoupon.isValid(productService)).isTrue();
    }

    @Test
    public void isNotValidTest() {
        bxgyCoupon.getBuyProducts().add(Product.builder().productId(5L).quantity(2L).build());
        assertThat(bxgyCoupon.isValid(productService)).isFalse();
    }

    @Test
    public void isApplicableTest() {
        items.add(CartItem.builder().productId(1L).quantity(4L).build());
        items.add(CartItem.builder().productId(3L).quantity(6L).build());
        Cart cart = Cart.builder().items(items).build();
        assertThat(bxgyCoupon.isApplicable(cart, productService)).isTrue();
    }

    @Test
    public void isNotApplicableTest() {
        items.add(CartItem.builder().productId(1L).quantity(1L).build());
        Cart cart = Cart.builder().items(items).build();
        assertThat(bxgyCoupon.isApplicable(cart, productService)).isFalse();
    }

    @Test
    public void getDiscountAmountTest() {
        items.add(CartItem.builder().productId(1L).quantity(4L).build());
        items.add(CartItem.builder().productId(3L).quantity(6L).build());
        Cart cart = Cart.builder().items(items).build();
        assertThat(bxgyCoupon.getDiscountAmount(cart, productService)).isEqualTo(600);
    }

    @Test
    public void applyCouponDiscountTest() {
        items.add(CartItem.builder().productId(1L).quantity(4L).build());
        items.add(CartItem.builder().productId(3L).quantity(6L).build());
        Cart cart = Cart.builder().items(items).build();
        bxgyCoupon.applyCouponDiscount(cart, productService);

        assertThat(cart.getTotalDiscount()).isEqualTo(600);
        assertThat(cart.getTotalPrice()).isEqualTo(1000);
        assertThat(cart.getFinalPrice()).isEqualTo(400);
        assertThat(cart.getItems().size()).isEqualTo(3);
    }
}

