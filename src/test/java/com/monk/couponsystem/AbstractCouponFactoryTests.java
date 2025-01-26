package com.monk.couponsystem;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monk.couponsystem.coupons.AbstractCoupon;
import com.monk.couponsystem.coupons.BxGy;
import com.monk.couponsystem.coupons.CartWise;
import com.monk.couponsystem.coupons.ProductWise;
import com.monk.couponsystem.models.CouponEntity;
import com.monk.couponsystem.utils.AbstractCouponFactory;
import com.monk.couponsystem.utils.CouponRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AbstractCouponFactoryTests {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CouponRegistry couponRegistry = new CouponRegistry();
    private final AbstractCouponFactory couponFactory = new AbstractCouponFactory(objectMapper, couponRegistry);

    @BeforeEach
    public void initialize() {
        couponRegistry.initialize();
    }

    @Test
    public void cartWiseCouponTest() throws JsonProcessingException {
        String jsonString = "{\"threshold\": 10, \"discount\": 20}";
        JsonNode jsonNode = objectMapper.readTree(jsonString);
        CouponEntity couponEntity = CouponEntity.builder()
                .id(1L).type("cart-wise").details(jsonNode).build();
        AbstractCoupon coupon = couponFactory.getCoupon(couponEntity);
        assertThat(coupon).isInstanceOf(CartWise.class);
    }

    @Test
    public void productWiseCouponTest() throws JsonProcessingException {
        String jsonString = "{\"productId\": 10, \"discount\": 20}";
        JsonNode jsonNode = objectMapper.readTree(jsonString);
        CouponEntity couponEntity = CouponEntity.builder()
                .id(1L).type("product-wise").details(jsonNode).build();
        AbstractCoupon coupon = couponFactory.getCoupon(couponEntity);
        assertThat(coupon).isInstanceOf(ProductWise.class);
    }

    @Test
    public void bxgyCouponTest() throws JsonProcessingException {
        String jsonString = "{\"buyProducts\": [{\"productId\": 1, \"quantity\": 2}]," +
                "\"getProducts\": [{\"productId\": 2, \"quantity\": 1}]," +
                "\"repetitionLimit\": 2}";
        JsonNode jsonNode = objectMapper.readTree(jsonString);
        CouponEntity couponEntity = CouponEntity.builder()
                .id(1L).type("bxgy").details(jsonNode).build();
        AbstractCoupon coupon = couponFactory.getCoupon(couponEntity);
        assertThat(coupon).isInstanceOf(BxGy.class);
    }
}
