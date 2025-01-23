package com.monk.couponsystem.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Cart {

    private List<CartItem> items;

    public Double getTotalAmount() {
        double total = 0.0;
        for (CartItem item : items) {
            total += item.getPrice() * item.getQuantity();
        }
        return total;
    }

    public void addItem(CartItem item) {
        items.add(item);
    }
}
