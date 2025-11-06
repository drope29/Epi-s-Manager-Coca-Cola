package com.epis.entities;

import com.epis.utils.DateAttributeConverter;
import com.fasterxml.jackson.annotation.JsonFormat;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbConvertedBy;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.util.Date;
import java.util.UUID;

@DynamoDbBean
public class Epi {

    private UUID epiId;
    private String codigoCompra;
    private String codigoAutenticacao;
    private String descricao;
    private Date dataValidade;

    public Epi(){}

    public Epi(String codigoCompra, String descricao) {
        this.epiId = UUID.randomUUID();
        this.codigoCompra = codigoCompra;
        this.descricao = descricao;
    }

    public Epi(String codigoCompra, String codigoAutenticacao, String descricao, Date dataValidade) {
        this.epiId = UUID.randomUUID();
        this.codigoCompra = codigoCompra;
        this.codigoAutenticacao = codigoAutenticacao;
        this.descricao = descricao;
        this.dataValidade = dataValidade;
    }

    @DynamoDbPartitionKey
    @DynamoDbAttribute("epiId")
    public UUID getEpiId() {
        return epiId;
    }

    public void setEpiId(UUID epiId) {
        this.epiId = epiId;
    }

    public String getCodigoCompra() {
        return codigoCompra;
    }

    public void setCodigoCompra(String codigoCompra) {
        this.codigoCompra = codigoCompra;
    }

    public String getCodigoAutenticacao() {
        return codigoAutenticacao;
    }

    public void setCodigoAutenticacao(String codigoAutenticacao) {
        this.codigoAutenticacao = codigoAutenticacao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @DynamoDbConvertedBy(DateAttributeConverter.class)
    public Date getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(Date dataValidade) {
        this.dataValidade = dataValidade;
    }

    @Override
    public String toString() {
        return "Epi{" +
                "epiId=" + epiId +
                ", codigoCompra='" + codigoCompra + '\'' +
                ", codigoAutenticacao='" + codigoAutenticacao + '\'' +
                ", descricao='" + descricao + '\'' +
                ", dataValidade=" + dataValidade +
                '}';
    }
}
