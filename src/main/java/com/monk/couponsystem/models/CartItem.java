package com.monk.couponsystem.models;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
public class CartItem {
    private Long productId;
    private Long quantity;
    private Double price;
    private Double totalDiscount;
}
