package com.epis.entities;

import com.epis.enums.StatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "tb_movimentacao")
public class Movimentacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

   /* @ManyToOne
    @JoinColumn(name = "funcionario_id")
    private Funcionario funcionario;

    @ManyToOne
    @JoinColumn(name = "epi_id")
    private Epi epi;

    @Column
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date dataEntrega;

    @Column
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date dataProximaEntrega;

    @Column
    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    public Movimentacao(){}

    public Movimentacao(Long id, Funcionario funcionario, Epi epi, Date dataEntrega, Date dataProximaEntrega, StatusEnum status) {
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
        return "Movimentacao{" +
                "id=" + id +
                ", funcionario=" + funcionario +
                ", epi=" + epi +
                ", dataEntrega=" + dataEntrega +
                ", dataProximaEntrega=" + dataProximaEntrega +
                ", status=" + status +
                '}';
    }*/
}
