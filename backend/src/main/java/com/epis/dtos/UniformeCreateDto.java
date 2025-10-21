package com.epis.dtos;

import jakarta.validation.constraints.NotBlank;

public class UniformeCreateDto {

    @NotBlank(message = "O campo função é obrigatório")
    private Long funcao;

    @NotBlank(message = "O campo epi é obrigatório")
    private Long epi;

    @NotBlank(message = "O campo quantidade é obrigatório")
    private Integer quantidade;

    public UniformeCreateDto() {}

    public UniformeCreateDto(Long funcao, Long epi, Integer quantidade) {
        this.funcao = funcao;
        this.epi = epi;
        this.quantidade = quantidade;
    }

    public Long getFuncao() {
        return funcao;
    }

    public void setFuncao(Long funcao) {
        this.funcao = funcao;
    }

    public Long getEpi() {
        return epi;
    }

    public void setEpi(Long epi) {
        this.epi = epi;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
}
