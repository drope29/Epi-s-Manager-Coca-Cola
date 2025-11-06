package com.epis.entities;

import com.epis.enums.StatusEnum;

import com.epis.utils.DateAttributeConverter;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbConvertedBy;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.util.Date;
import java.util.UUID;

@DynamoDbBean
public class Movimentacao {

    private UUID movimentacaoId;
    private Funcionario funcionario;
    private Epi epi;
    private Date dataEntrega;
    private Date dataProximaEntrega;
    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    public Movimentacao(){}

    public Movimentacao(UUID movimentacaoId, Funcionario funcionario, Epi epi, Date dataEntrega, Date dataProximaEntrega, StatusEnum status) {
        this.movimentacaoId = UUID.randomUUID();
        this.funcionario = funcionario;
        this.epi = epi;
        this.dataEntrega = dataEntrega;
        this.dataProximaEntrega = dataProximaEntrega;
        this.status = status;
    }

    @DynamoDbPartitionKey
    @DynamoDbAttribute("movimentacaoId")
    public UUID getMovimentacaoId() {
        return movimentacaoId;
    }

    public void setMovimentacaoId(UUID movimentacaoId) {
        this.movimentacaoId = movimentacaoId;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public Epi getEpi() {
        return epi;
    }

    public void setEpi(Epi epi) {
        this.epi = epi;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @DynamoDbConvertedBy(DateAttributeConverter.class)
    public Date getDataEntrega() {
        return dataEntrega;
    }

    public void setDataEntrega(Date dataEntrega) {
        this.dataEntrega = dataEntrega;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @DynamoDbConvertedBy(DateAttributeConverter.class)
    public Date getDataProximaEntrega() {
        return dataProximaEntrega;
    }

    public void setDataProximaEntrega(Date dataProximaEntrega) {
        this.dataProximaEntrega = dataProximaEntrega;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Movimentacao{" +
                "movimentacaoId=" + movimentacaoId +
                ", funcionario=" + funcionario +
                ", epi=" + epi +
                ", dataEntrega=" + dataEntrega +
                ", dataProximaEntrega=" + dataProximaEntrega +
                ", status=" + status +
                '}';
    }
}
