package com.epis.entities;

import com.epis.enums.StatusEnum;

import com.epis.utils.DateAttributeConverter;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

import java.util.Date;
import java.util.UUID;

@DynamoDbBean
public class Movimentacao {

    private UUID movimentacaoId;
    private UUID funcionarioId;
    private UUID epiId;
    private Date dataEntrega;
    private Date dataProximaEntrega;
    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    public String cadastroAtivo;

    public Movimentacao(){}

    public Movimentacao(UUID movimentacaoId, UUID funcionarioId, UUID epiId, Date dataEntrega, Date dataProximaEntrega, StatusEnum status) {
        this.movimentacaoId = movimentacaoId;
        this.funcionarioId = funcionarioId;
        this.epiId = epiId;
        this.dataEntrega = dataEntrega;
        this.dataProximaEntrega = dataProximaEntrega;
        this.status = status;
        this.cadastroAtivo = "1";
    }

    @DynamoDbPartitionKey
    @DynamoDbAttribute("movimentacaoId")
    public UUID getMovimentacaoId() {
        return movimentacaoId;
    }

    public void setMovimentacaoId(UUID movimentacaoId) {
        this.movimentacaoId = movimentacaoId;
    }

    public UUID getFuncionarioId() { return funcionarioId; }

    public void setFuncionarioId(UUID funcionarioId) { this.funcionarioId = funcionarioId; }

    public UUID getEpiId() { return epiId; }

    public void setEpiId(UUID epiId) { this.epiId = epiId; }

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

    @DynamoDbSecondaryPartitionKey(indexNames = "movimentacao-ativo-index")
    @DynamoDbAttribute("cadastroAtivo")
    public String getCadastroAtivo() {
        return cadastroAtivo;
    }

    public void setCadastroAtivo(String cadastroAtivo) {
        this.cadastroAtivo = cadastroAtivo;
    }

    @Override
    public String toString() {
        return "Movimentacao{" +
                "movimentacaoId=" + movimentacaoId +
                ", funcionarioId=" + funcionarioId +
                ", epiId=" + epiId +
                ", dataEntrega=" + dataEntrega +
                ", dataProximaEntrega=" + dataProximaEntrega +
                ", status=" + status +
                '}';
    }
}
