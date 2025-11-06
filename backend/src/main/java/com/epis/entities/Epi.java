package com.epis.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "tb_epi")
public class Epi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String codigoCompra;

    @Column
    private String codigoAutenticacao;

    @Column
    private String descricao;

    @Column
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date dataValidade;

    /*@OneToMany(mappedBy = "epi")
    private List<Movimentacao> EpiMovimentacao = new ArrayList<>();*/

    @OneToMany(mappedBy = "epi")
    private List<Uniforme> EpiUniforme = new ArrayList<>();

    public Epi(){}

    public Epi(String codigoCompra, String descricao) {
        this.codigoCompra = codigoCompra;
        this.descricao = descricao;
    }

    public Epi(Long id, String codigoCompra, String codigoAutenticacao, String descricao, Date dataValidade) {
        this.id = id;
        this.codigoCompra = codigoCompra;
        this.codigoAutenticacao = codigoAutenticacao;
        this.descricao = descricao;
        this.dataValidade = dataValidade;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Date getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(Date dataValidade) {
        this.dataValidade = dataValidade;
    }

    @Override
    public String toString() {
        return "Epi{" +
                "id=" + id +
                ", codigoCompra='" + codigoCompra + '\'' +
                ", codigoAutenticacao='" + codigoAutenticacao + '\'' +
                ", descricao='" + descricao + '\'' +
                ", dataValidade=" + dataValidade +
                '}';
    }
}
