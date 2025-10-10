package com.epis.dtos;

import jakarta.validation.constraints.NotBlank;

import java.util.Date;

public class EpiUpdateDto {

    private String codigoCompra;
    private String codigoAutenticacao;
    private String descricao;
    private Date dataValidade;

    public EpiUpdateDto(){}

    public EpiUpdateDto(String codigoCompra, String codigoAutenticacao, String descricao, Date dataValidade) {
        this.codigoCompra = codigoCompra;
        this.codigoAutenticacao = codigoAutenticacao;
        this.descricao = descricao;
        this.dataValidade = dataValidade;
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
        return "EpiUpdateDto{" +
                "codigoCompra='" + codigoCompra + '\'' +
                ", codigoAutenticacao='" + codigoAutenticacao + '\'' +
                ", descricao='" + descricao + '\'' +
                ", dataValidade=" + dataValidade +
                '}';
    }
}
