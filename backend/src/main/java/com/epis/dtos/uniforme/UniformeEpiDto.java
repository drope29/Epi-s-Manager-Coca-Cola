package com.epis.dtos.uniforme;

import java.util.UUID;

public class UniformeEpiDto {

    private UUID epi;
    private Long quantidade;

    public UniformeEpiDto() {}

    public UniformeEpiDto(UUID epi, Long quantidade) {
        this.epi = epi;
        this.quantidade = quantidade;
    }

    public UUID getEpi() {
        return epi;
    }

    public void setEpi(UUID epi) {
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
        return "UniformeEpiDto{" +
                "epi=" + epi +
                ", quantidade=" + quantidade +
                '}';
    }
}
