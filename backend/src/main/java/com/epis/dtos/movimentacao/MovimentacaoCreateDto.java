package com.epis.dtos.movimentacao;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.UUID;

public class MovimentacaoCreateDto {

    @NotNull(message = "O campo funcionario é obrigatorio")
    private UUID funcionario;

    @NotNull(message = "O campo epi é obrigatorio")
    private UUID epi;

    @NotBlank(message = "O campo data entrega é obrigatorio")
    private Date dataEntrega;

    @NotBlank(message = "O campo próxima entrega é obrigatorio")
    private Date dataProximaEntrega;

    @NotBlank(message = "O campo status é obrigatorio")
    private String status;

    public MovimentacaoCreateDto(){}

    public MovimentacaoCreateDto(UUID funcionario, UUID epi, Date dataEntrega, Date dataProximaEntrega, String status) {
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
