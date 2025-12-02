package com.epis.dtos.epi;

import jakarta.validation.constraints.NotBlank;

import java.util.Date;

public class EpiCreateDto {

    @NotBlank(message = "O campo código compra é obrigatório")
    private String codigoCompra;

    private String codigoAutenticacao;

    @NotBlank(message = "O campo descrição é obrigatório")
    private String descricao;

    private Date dataValidade;

    public EpiCreateDto(){}

    public EpiCreateDto(String codigoCompra, String codigoAutenticacao, String descricao, Date dataValidade) {
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
        return "EpiCreateDto{" +
                "codigoCompra='" + codigoCompra + '\'' +
                ", codigoAutenticacao='" + codigoAutenticacao + '\'' +
                ", descricao='" + descricao + '\'' +
                ", dataValidade=" + dataValidade +
                '}';
    }
}
