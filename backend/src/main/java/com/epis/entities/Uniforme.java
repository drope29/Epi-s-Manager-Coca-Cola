package com.epis.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_uniforme")
public class Uniforme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

  /*  @ManyToOne
    @JoinColumn(name = "funcao_id")
    private Funcao funcao;*/

   /* @ManyToOne
    @JoinColumn(name = "epi_id")
    private Epi epi;*/

    @Column
    private Integer quantidade;

    public Uniforme (){}

   /* public Uniforme(Long id, Funcao funcao, Epi epi, Integer quantidade) {
        this.id = id;
        this.funcao = funcao;
        this.epi = epi;
        this.quantidade = quantidade;
    }*/

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

   /* public Funcao getFuncao() {
        return funcao;
    }

    public void setFuncao(Funcao funcao) {
        this.funcao = funcao;
    }*/

   /* public Epi getEpi() {
        return epi;
    }

    public void setEpi(Epi epi) {
        this.epi = epi;
    }*/

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    @Override
    public String toString() {
        return "Uniforme{" +
                "id=" + id +
               // ", funcao=" + funcao +
               // ", epi=" + epi +
                ", quantidade=" + quantidade +
                '}';
    }
}
