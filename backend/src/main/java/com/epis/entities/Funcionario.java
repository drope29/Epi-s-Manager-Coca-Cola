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

    public Funcionario(){}

    public Funcionario(String RE, String nome, String funcao) {
        this.RE = RE;
        this.nome = nome;
        this.funcao = funcao;
    }

    public Funcionario(Long id, String RE, String nome, String funcao) {
        this.id = id;
        this.RE = RE;
        this.nome = nome;
        this.funcao = funcao;
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

    @Override
    public String toString() {
        return "Funcionarios{" +
                "id=" + id +
                ", RE=" + RE +
                ", nome='" + nome + '\'' +
                ", funcao='" + funcao + '\'' +
                '}';
    }
}
