package com.epis.utils;

import software.amazon.awssdk.enhanced.dynamodb.AttributeConverter;
import software.amazon.awssdk.enhanced.dynamodb.AttributeValueType;
import software.amazon.awssdk.enhanced.dynamodb.EnhancedType;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateAttributeConverter implements AttributeConverter<Date> {

    private static final String PATTERN = "dd/MM/yyyy";
    private static final SimpleDateFormat formatter = new SimpleDateFormat(PATTERN);

    @Override
    public AttributeValue transformFrom(Date input) {
        return input == null ? AttributeValue.builder().nul(true).build()
                : AttributeValue.builder().s(formatter.format(input)).build();
    }

    @Override
    public Date transformTo(AttributeValue attributeValue) {
        try {
            return attributeValue.s() == null ? null : formatter.parse(attributeValue.s());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public EnhancedType<Date> type() {
        return null;
    }

    @Override
    public AttributeValueType attributeValueType() {
        return AttributeValueType.S;
    }
}
