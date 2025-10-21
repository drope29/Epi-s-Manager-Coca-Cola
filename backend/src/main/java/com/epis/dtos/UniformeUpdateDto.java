package com.epis.dtos;

public class UniformeUpdateDto {

    private Long funcao;
    private Long epi;
    private Integer quantidade;

    public UniformeUpdateDto() {}

    public UniformeUpdateDto(Long funcao, Long epi, Integer quantidade) {
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

    @Override
    public String toString() {
        return "UniformeUpdateDto{" +
                "funcao=" + funcao +
                ", epi=" + epi +
                ", quantidade=" + quantidade +
                '}';
    }
}
