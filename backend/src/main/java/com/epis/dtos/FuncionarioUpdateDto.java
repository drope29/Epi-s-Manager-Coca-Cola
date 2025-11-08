package com.epis.dtos;

import java.util.UUID;
import java.util.Date;

public class FuncionarioUpdateDto {

    private String RE;
    private String nome;
    private UUID funcao;
    private String unidade;
    private String turno;
    private String genero;
    private Date dataAdmissao;
    private String setor;

    public FuncionarioUpdateDto(){}

    public FuncionarioUpdateDto(String RE, String nome, UUID funcao, String unidade, String turno, String genero, Date dataAdmissao, String setor) {
        this.RE = RE;
        this.nome = nome;
        this.funcao = funcao;
        this.unidade = unidade;
        this.turno = turno;
        this.genero = genero;
        this.dataAdmissao = dataAdmissao;
        this.setor = setor;
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

    public UUID getFuncao() {
        return funcao;
    }

    public void setFuncao(UUID funcao) {
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

    public Date getDataAdmissao() { return dataAdmissao; }

    public void setDataAdmissao(Date dataAdmissao) { this.dataAdmissao = dataAdmissao; }

    public String getSetor() { return setor; }

    public void setSetor(String setor) { this.setor = setor; }

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
