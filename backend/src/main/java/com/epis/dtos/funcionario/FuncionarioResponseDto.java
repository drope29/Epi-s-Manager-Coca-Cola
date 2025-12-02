package com.epis.dtos.funcionario;

import com.epis.entities.Funcao;
import com.epis.entities.Funcionario;

import java.util.Date;
import java.util.UUID;

public class FuncionarioResponseDto {

    private UUID funcionarioId;
    private String RE;
    private String nome;
    private Funcao funcao;
    private String unidade;
    private String turno;
    private String genero;
    private Date dataAdmissao;
    private String setor;

    public FuncionarioResponseDto(Funcionario funcionario) {
        this.funcionarioId = funcionario.getFuncionarioId();
        this.RE = funcionario.getRE();
        this.nome = funcionario.getNome();
        this.unidade = funcionario.getUnidade();
        this.turno = String.valueOf(funcionario.getTurno());
        this.genero = String.valueOf(funcionario.getGenero());
        this.dataAdmissao = funcionario.getDataAdmissao();
        this.setor = funcionario.getSetor();
    }

    public FuncionarioResponseDto(UUID funcionarioId, String RE, String nome, Funcao funcao, String unidade, String turno, String genero, Date dataAdmissao, String setor) {
        this.funcionarioId = funcionarioId;
        this.RE = RE;
        this.nome = nome;
        this.funcao = funcao;
        this.unidade = unidade;
        this.turno = turno;
        this.genero = genero;
        this.dataAdmissao = dataAdmissao;
        this.setor = setor;
    }

    public UUID getFuncionarioId() {
        return funcionarioId;
    }

    public void setFuncionarioId(UUID funcionarioId) {
        this.funcionarioId = funcionarioId;
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

    public Funcao getFuncao() {
        return funcao;
    }

    public void setFuncao(Funcao funcao) {
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

    public Date getDataAdmissao() {
        return dataAdmissao;
    }

    public void setDataAdmissao(Date dataAdmissao) {
        this.dataAdmissao = dataAdmissao;
    }

    public String getSetor() {
        return setor;
    }

    public void setSetor(String setor) {
        this.setor = setor;
    }

    @Override
    public String toString() {
        return "FuncionarioResponseDto{" +
                "RE='" + RE + '\'' +
                ", nome='" + nome + '\'' +
                ", funcao=" + funcao +
                ", unidade='" + unidade + '\'' +
                ", turno='" + turno + '\'' +
                ", genero='" + genero + '\'' +
                ", dataAdmissao=" + dataAdmissao +
                ", setor='" + setor + '\'' +
                '}';
    }
}
