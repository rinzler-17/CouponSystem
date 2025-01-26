package com.monk.couponsystem.models;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;
import java.util.List;

/*
POJO representing a shopping cart.
 */
@Data
@Builder
public class Cart {

    private List<CartItem> items;
    @Builder.Default
    private Double totalPrice = 0.0;
    @Builder.Default
    private Double totalDiscount = 0.0;
    private Double finalPrice;

    public Double getTotalPrice() {
        totalPrice = 0.0;
        for (CartItem item : items) {
            totalPrice += item.getPrice() * item.getQuantity();
        }
        return totalPrice;
    }

    public void sortByQuantity() {
        items.sort(Comparator.comparingLong(CartItem::getQuantity).reversed());
    }
    public void addItem(CartItem item) {
        items.add(item);
    }
}
