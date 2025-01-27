package com.monk.couponsystem.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

/*
Entity representing a product in the inventory.
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    private Long productId;
    private Double price;
    private Long quantity;
}
