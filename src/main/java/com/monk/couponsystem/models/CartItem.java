package com.monk.couponsystem.models;

import lombok.Builder;
import lombok.Data;

/*
POJO for an item in the shopping cart.
 */
@Data
@Builder
public class CartItem {
    private Long productId;
    private Long quantity;
    private Double price;
    private Double totalDiscount;
}
