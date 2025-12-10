package com.epis.dtos.movimentacao;

import com.epis.entities.Epi;
import com.epis.entities.Funcionario;
import com.epis.entities.Movimentacao;
import com.epis.enums.StatusEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.util.Date;
import java.util.UUID;

public class MovimentacaoResponseDto {

    private UUID movimentacaoId;
    private Funcionario funcionario;
    private Epi epi;
    private Date dataEntrega;
    private Date dataProximaEntrega;
    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    public MovimentacaoResponseDto(Movimentacao movimentacao) {
        this.movimentacaoId = movimentacao.getMovimentacaoId();
        this.dataEntrega = movimentacao.getDataEntrega();
        this.dataProximaEntrega = movimentacao.getDataProximaEntrega();
        this.status = movimentacao.getStatus();
    }

    public MovimentacaoResponseDto(UUID movimentacaoId, Funcionario funcionario, Epi epi, Date dataEntrega, Date dataProximaEntrega, StatusEnum status) {
        this.movimentacaoId = movimentacaoId;
        this.funcionario = funcionario;
        this.epi = epi;
        this.dataEntrega = dataEntrega;
        this.dataProximaEntrega = dataProximaEntrega;
        this.status = status;
    }

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

    public Date getDataEntrega() {
        return dataEntrega;
    }

    public void setDataEntrega(Date dataEntrega) {
        this.dataEntrega = dataEntrega;
    }

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
        return "MovimentacaoResponseDto{" +
                "status=" + status +
                ", dataProximaEntrega=" + dataProximaEntrega +
                ", dataEntrega=" + dataEntrega +
                ", epi=" + epi +
                ", funcionario=" + funcionario +
                ", movimentacaoId=" + movimentacaoId +
                '}';
    }
}
