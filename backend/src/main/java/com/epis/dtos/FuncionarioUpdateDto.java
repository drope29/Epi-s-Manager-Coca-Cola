package com.epis.dtos;

import jakarta.validation.constraints.NotBlank;

public class FuncionarioUpdateDto {

    private String RE;
    private String nome;
    private String funcao;
    private String unidade;
    private String turno;
    private String genero;

    public FuncionarioUpdateDto(){}

    public FuncionarioUpdateDto(String RE, String nome, String funcao, String unidade, String turno, String genero) {
        this.RE = RE;
        this.nome = nome;
        this.funcao = funcao;
        this.unidade = unidade;
        this.turno = turno;
        this.genero = genero;
    }

    public String getRE() {
        return RE;
    }

    public void setRE(String RE) {
        this.RE = RE;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFuncao() {
        return funcao;
    }

    public void setFuncao(String funcao) {
        this.funcao = funcao;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    @Override
    public String toString() {
        return "FuncionarioUpdateDto{" +
                "RE='" + RE + '\'' +
                ", nome='" + nome + '\'' +
                ", funcao='" + funcao + '\'' +
                ", unidade='" + unidade + '\'' +
                ", turno='" + turno + '\'' +
                ", genero='" + genero + '\'' +
                '}';
    }
}
