package com.epis.dtos;

import com.epis.enums.StatusEnum;
import java.util.Date;

public class MovimentacaoCreateDto {

    private Long id;
    private Long funcionario;
    private Long epi;
    private Date dataEntrega;
    private Date dataProximaEntrega;
    private String status;

    public MovimentacaoCreateDto(){}

    public MovimentacaoCreateDto(Long id, Long funcionario, Long epi, Date dataEntrega, Date dataProximaEntrega, String status) {
        this.id = id;
        this.funcionario = funcionario;
        this.epi = epi;
        this.dataEntrega = dataEntrega;
        this.dataProximaEntrega = dataProximaEntrega;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Long funcionario) {
        this.funcionario = funcionario;
    }

    public Long getEpi() {
        return epi;
    }

    public void setEpi(Long epi) {
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
                "id=" + id +
                ", funcionario=" + funcionario +
                ", epi=" + epi +
                ", dataEntrega=" + dataEntrega +
                ", dataProximaEntrega=" + dataProximaEntrega +
                ", status=" + status +
                '}';
    }
}
