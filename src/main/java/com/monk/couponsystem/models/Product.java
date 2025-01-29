package com.monk.couponsystem.models;

import com.fasterxml.jackson.annotation.JsonInclude;
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
    @JsonInclude(JsonInclude.Include.NON_NULL)             // Product quantity in catalog is assumed to be infinite
    private Long quantity;
}
