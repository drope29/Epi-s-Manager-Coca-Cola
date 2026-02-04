package com.epis.dtos.uniforme;

import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public class UniformeCreateDto {

    @NotNull(message = "O campo função é obrigatório")
    private UUID funcao;

    @NotNull(message = "O campo função é obrigatório")
    private List<UniformeEpiDto> uniformeEpis;

    public UniformeCreateDto() {}

    public UniformeCreateDto(UUID funcao, List<UniformeEpiDto> uniformeEpis) {
        this.funcao = funcao;
        this.uniformeEpis = uniformeEpis;
    }

    public UUID getFuncao() {
        return funcao;
    }

    public void setFuncao(UUID funcao) {
        this.funcao = funcao;
    }

    public List<UniformeEpiDto> getUniformeEpis() {
        return uniformeEpis;
    }

    public void setUniformeEpis(List<UniformeEpiDto> uniformeEpis) {
        this.uniformeEpis = uniformeEpis;
    }

    @Override
    public String toString() {
        return "UniformeCreateDto{" +
                "funcao=" + funcao +
                ", uniformeEpis=" + uniformeEpis +
                '}';
    }
}
