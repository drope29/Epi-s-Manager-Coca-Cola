package com.epis.entities;

import com.epis.utils.ListAttributeConverter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbConvertedBy;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.util.List;
import java.util.UUID;

@DynamoDbBean
public class Uniforme {

    private UUID uniformeId;
    private Funcao funcao;
    private List<UniformeEpi> uniformeEpis;

    public Uniforme (){}

    public Uniforme(UUID uniformeId, Funcao funcao, List<UniformeEpi> uniformeEpis) {
        this.uniformeId = uniformeId;
        this.funcao = funcao;
        this.uniformeEpis = uniformeEpis;
    }

    @DynamoDbPartitionKey
    @DynamoDbAttribute("uniformeId")
    public UUID getUniformeId() {
        return uniformeId;
    }

    public void setUniformeId(UUID uniformeId) {
        this.uniformeId = uniformeId;
    }

   public Funcao getFuncao() {
        return funcao;
    }

    public void setFuncao(Funcao funcao) {
        this.funcao = funcao;
    }

    @DynamoDbConvertedBy(ListAttributeConverter.class)
    public List<UniformeEpi> getUniformeEpis() {
        return uniformeEpis;
    }

    public void setUniformeEpis(List<UniformeEpi> uniformeEpis) {
        this.uniformeEpis = uniformeEpis;
    }

    @Override
    public String toString() {
        return "Uniforme{" +
                "uniformeId=" + uniformeId +
                ", funcao=" + funcao +
                ", uniformeEpis=" + uniformeEpis +
                '}';
    }
}
