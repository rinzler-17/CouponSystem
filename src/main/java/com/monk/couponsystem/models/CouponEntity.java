package com.monk.couponsystem.models;

import com.fasterxml.jackson.databind.JsonNode;
import com.monk.couponsystem.models.converters.JsonNodeConverter;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

/*
Entity representing a coupon type.
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String type;

    @Convert(converter = JsonNodeConverter.class) // Using the custom converter for JsonNode
    @Column(columnDefinition = "TEXT")
    private JsonNode details;
}

