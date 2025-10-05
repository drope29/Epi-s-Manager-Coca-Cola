package com.epis.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_funcionarios")
public class Funcionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String RE;

    @Column
    private String nome;

    @Column
    private String funcao;

    @Column
    private String unidade;

    @Column
    private String turno;

    @Column
    private String genero;

    public Funcionario(){}

    public Funcionario(String RE, String nome, String funcao) {
        this.RE = RE;
        this.nome = nome;
        this.funcao = funcao;
    }

    public Funcionario(Long id, String RE, String nome, String funcao, String unidade, String turno, String genero) {
        this.id = id;
        this.RE = RE;
        this.nome = nome;
        this.funcao = funcao;
        this.unidade = unidade;
        this.turno = turno;
        this.genero = genero;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        return "Funcionario{" +
                "id=" + id +
                ", RE='" + RE + '\'' +
                ", nome='" + nome + '\'' +
                ", funcao='" + funcao + '\'' +
                ", unidade='" + unidade + '\'' +
                ", turno='" + turno + '\'' +
                ", genero='" + genero + '\'' +
                '}';
    }
}
