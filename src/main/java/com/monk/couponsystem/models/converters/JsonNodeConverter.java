package com.monk.couponsystem.models.converters;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;

@Converter(autoApply = true)
public class JsonNodeConverter implements AttributeConverter<JsonNode, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(JsonNode jsonNode) {
        if (jsonNode == null) {
            return null;
        }
        return jsonNode.toString();
    }

    @Override
    public JsonNode convertToEntityAttribute(String json) {
        if (json == null) {
            return null;
        }
        try {
            return objectMapper.readTree(json);
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert String to JsonNode", e);
        }
    }
}
