package com.epis.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class FuncionarioCreateDto {

    @NotBlank(message = "O campo RE é obrigatorio")
    private String RE;

    @NotBlank(message = "O campo nome é obrigatorio")
    private String nome;

    @NotBlank(message = "O campo funcao é obrigatorio")
    private String funcao;

    public FuncionarioCreateDto(){}

    public FuncionarioCreateDto(String RE, String nome, String funcao) {
        this.RE = RE;
        this.nome = nome;
        this.funcao = funcao;
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
        return "FuncionarioCreateDto{" +
                "RE='" + RE + '\'' +
                ", nome='" + nome + '\'' +
                ", funcao='" + funcao + '\'' +
                '}';
    }
}
