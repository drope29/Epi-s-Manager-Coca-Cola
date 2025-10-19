package com.epis.dtos;

import java.util.Date;

public class MovimentacaoUpdateDto {

    private Long funcionario;
    private Long epi;
    private Date dataEntrega;
    private Date dataProximaEntrega;
    private String status;

    public MovimentacaoUpdateDto(){}

    public MovimentacaoUpdateDto(Long funcionario, Long epi, Date dataEntrega, Date dataProximaEntrega, String status) {
        this.funcionario = funcionario;
        this.epi = epi;
        this.dataEntrega = dataEntrega;
        this.dataProximaEntrega = dataProximaEntrega;
        this.status = status;
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
                ", funcionario=" + funcionario +
                ", epi=" + epi +
                ", dataEntrega=" + dataEntrega +
                ", dataProximaEntrega=" + dataProximaEntrega +
                ", status=" + status +
                '}';
    }

}
