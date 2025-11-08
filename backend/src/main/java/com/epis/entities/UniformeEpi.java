package com.epis.entities;

import java.util.UUID;

public class UniformeEpi {

    private Epi epi;
    private Long quantidade;

    public UniformeEpi(){}

    public UniformeEpi(Epi epi, Long quantidade) {
        this.epi = epi;
        this.quantidade = quantidade;
    }

    public Epi getEpi() {
        return epi;
    }

    public void setEpi(Epi epi) {
        this.epi = epi;
    }

    public Long getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Long quantidade) {
        this.quantidade = quantidade;
    }

    @Override
    public String toString() {
        return "UniformeEpi{" +
                "epi=" + epi +
                ", quantidade=" + quantidade +
                '}';
    }
}
