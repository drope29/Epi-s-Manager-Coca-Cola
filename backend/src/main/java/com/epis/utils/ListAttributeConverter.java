package com.epis.utils;

import com.epis.entities.UniformeEpi;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.enhanced.dynamodb.AttributeConverter;
import software.amazon.awssdk.enhanced.dynamodb.AttributeValueType;
import software.amazon.awssdk.enhanced.dynamodb.EnhancedType;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.io.IOException;
import java.util.List;


public class ListAttributeConverter implements AttributeConverter<List<UniformeEpi>> {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public AttributeValue transformFrom(List<UniformeEpi> input) {
        try {
            return AttributeValue.builder()
                    .s(mapper.writeValueAsString(input))
                    .build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao converter Lista<Epi> -> JSON", e);
        }
    }

    @Override
    public List<UniformeEpi> transformTo(AttributeValue input) {
        try {
            return mapper.readValue(input.s(), new TypeReference<>() {});
        } catch (IOException e) {
            throw new RuntimeException("Erro ao converter JSON -> Lista<Epi>", e);
        }
    }

    @Override
    public EnhancedType<List<UniformeEpi>> type() {
        return null;
    }

    @Override
    public AttributeValueType attributeValueType() {
        return AttributeValueType.S;
    }

}
