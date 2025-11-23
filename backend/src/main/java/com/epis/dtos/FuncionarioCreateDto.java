package com.epis.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.UUID;

public class FuncionarioCreateDto {

    @NotBlank(message = "O campo RE é obrigatorio")
    private String RE;

    @NotBlank(message = "O campo nome é obrigatorio")
    private String nome;

    @NotNull(message = "O campo funcao é obrigatorio")
    private UUID funcao;

    @NotBlank(message = "O campo unidade é obrigatorio")
    private String unidade;

    @NotBlank(message = "O campo turno é obrigatorio")
    private String turno;

    @NotBlank(message = "O campo genero é obrigatorio")
    private String genero;

    @NotNull(message = "O campo data admissão é obrigatorio")
    private Date dataAdmissao;

    @NotBlank(message = "O campo setor é obrigatorio")
    private String setor;

    public FuncionarioCreateDto(){}

    public FuncionarioCreateDto(String RE, String nome, UUID funcao, String unidade, String turno, String genero, Date dataAdmissao, String setor) {
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
        return "FuncionarioCreateDto{" +
                "RE='" + RE + '\'' +
                ", nome='" + nome + '\'' +
                ", funcao='" + funcao + '\'' +
                ", unidade='" + unidade + '\'' +
                ", turno='" + turno + '\'' +
                ", genero='" + genero + '\'' +
                '}';
    }
}
