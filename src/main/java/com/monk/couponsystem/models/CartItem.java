package com.monk.couponsystem.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CartItem {
    private Long productId;
    private Integer quantity;
    private Double price;

}
