package com.epis.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_epi")
public class Epi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String codigo;

    @Column
    private String descricao;

    public Epi(){}

    public Epi(String codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public Epi(Long id, String codigo, String descricao) {
        this.id = id;
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return "Epi{" +
                "codigo='" + codigo + '\'' +
                ", descricao='" + descricao + '\'' +
                '}';
    }
}
