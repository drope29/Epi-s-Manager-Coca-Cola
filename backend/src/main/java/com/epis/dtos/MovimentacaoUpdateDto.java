package com.epis.dtos;

import java.util.Date;
import java.util.UUID;

public class MovimentacaoUpdateDto {

    private UUID funcionario;
    private UUID epi;
    private Date dataEntrega;
    private Date dataProximaEntrega;
    private String status;

    public MovimentacaoUpdateDto(){}

    public MovimentacaoUpdateDto(UUID funcionario, UUID epi, Date dataEntrega, Date dataProximaEntrega, String status) {
        this.funcionario = funcionario;
        this.epi = epi;
        this.dataEntrega = dataEntrega;
        this.dataProximaEntrega = dataProximaEntrega;
        this.status = status;
    }

    public UUID getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(UUID funcionario) {
        this.funcionario = funcionario;
    }

    public UUID getEpi() {
        return epi;
    }

    public void setEpi(UUID epi) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Movimentacao{" +
                ", funcionario=" + funcionario +
                ", epi=" + epi +
                ", dataEntrega=" + dataEntrega +
                ", dataProximaEntrega=" + dataProximaEntrega +
                ", status=" + status +
                '}';
    }

}
