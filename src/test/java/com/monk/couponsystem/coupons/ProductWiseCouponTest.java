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
public class ProductWiseCouponTest {

    @Mock
    private ProductService productService;

    List<CartItem> items;

    private final ProductWise productWiseCoupon = new ProductWise();

    @BeforeEach
    public void setup() {
        items = new ArrayList<>();
        productWiseCoupon.setDiscount(20.0);
        productWiseCoupon.setProductId(1L);

        Mockito.when(productService.getProductById(1L)).thenReturn(Optional.of(Product.builder()
                .productId(1L)
                .quantity(20L)
                .price(25.0).build()));

        Mockito.when(productService.getProductById(2L)).thenReturn(Optional.of(Product.builder()
                .productId(2L)
                .quantity(40L)
                .price(100.0).build()));
    }

    @Test
    public void isValidTest() {
        assertThat(productWiseCoupon.isValid(productService)).isTrue();
    }

    @Test
    public void isApplicableTest() {
        items.add(CartItem.builder().productId(1L).quantity(2L).build());
        Cart cart = Cart.builder().items(items).build();
        assertThat(productWiseCoupon.isApplicable(cart, productService)).isTrue();
    }

    @Test
    public void isNotApplicableTest() {
        items.add(CartItem.builder().productId(2L).quantity(2L).build());
        Cart cart = Cart.builder().items(items).build();
        assertThat(productWiseCoupon.isApplicable(cart, productService)).isFalse();
    }

    @Test
    public void getDiscountAmountTest() {
        items.add(CartItem.builder().productId(1L).quantity(2L).build());
        Cart cart = Cart.builder().items(items).build();
        assertThat(productWiseCoupon.getDiscountAmount(cart, productService)).isEqualTo(10);
    }

    @Test
    public void applyCouponDiscountTest() {
        items.add(CartItem.builder().productId(1L).quantity(2L).build());
        Cart cart = Cart.builder().items(items).build();
        productWiseCoupon.applyCouponDiscount(cart, productService);

        assertThat(cart.getTotalDiscount()).isEqualTo(10);
        assertThat(cart.getTotalPrice()).isEqualTo(50);
        assertThat(cart.getFinalPrice()).isEqualTo(40);
    }
}
