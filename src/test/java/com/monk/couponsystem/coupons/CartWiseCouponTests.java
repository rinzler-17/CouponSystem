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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CartWiseCouponTests {

    @Mock
    private ProductService productService;

    private List<CartItem> items;
    private final CartWise cartWiseCoupon = new CartWise();

    @BeforeEach
    public void setup() {
        items = new ArrayList<>();
        cartWiseCoupon.setDiscount(20.0);
        cartWiseCoupon.setThreshold(100.0);

        Mockito.when(productService.getProductById(1L)).thenReturn(Product.builder()
                .productId(1L)
                .quantity(20L)
                .price(25.0).build());

        Mockito.when(productService.getProductById(2L)).thenReturn(Product.builder()
                .productId(2L)
                .quantity(40L)
                .price(100.0).build());
    }

    @Test
    public void isValidTest() {
        assertThatNoException().isThrownBy(() -> {
            cartWiseCoupon.validate(productService);
        });
    }

    @Test
    public void isApplicableTest() {
        items.add(CartItem.builder().productId(2L).quantity(2L).build());
        Cart cart = Cart.builder().items(items).build();
        assertThat(cartWiseCoupon.isApplicable(cart, productService)).isTrue();
    }

    @Test
    public void isNotApplicableTest() {
        items.add(CartItem.builder().productId(1L).quantity(2L).build());
        Cart cart = Cart.builder().items(items).build();
        assertThat(cartWiseCoupon.isApplicable(cart, productService)).isFalse();
    }

    @Test
    public void getDiscountAmountTest() {
        items.add(CartItem.builder().productId(2L).quantity(2L).build());
        Cart cart = Cart.builder().items(items).build();
        assertThat(cartWiseCoupon.getDiscountAmount(cart, productService)).isEqualTo(40);
    }

    @Test
    public void applyCouponDiscountTest() {
        items.add(CartItem.builder().productId(2L).quantity(2L).build());
        Cart cart = Cart.builder().items(items).build();
        cartWiseCoupon.applyCouponDiscount(cart, productService);

        assertThat(cart.getTotalDiscount()).isEqualTo(40);
        assertThat(cart.getTotalPrice()).isEqualTo(200);
        assertThat(cart.getFinalPrice()).isEqualTo(160);
    }
}
