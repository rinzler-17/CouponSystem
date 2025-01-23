package com.monk.couponsystem.models;

import com.fasterxml.jackson.databind.JsonNode;
import com.monk.couponsystem.coupons.CouponType;
import com.monk.couponsystem.models.converters.JsonNodeConverter;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private CouponType type;

    @Convert(converter = JsonNodeConverter.class) // Using the custom converter for JsonNode
    @Column(columnDefinition = "TEXT")
    //@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private JsonNode details;
}

