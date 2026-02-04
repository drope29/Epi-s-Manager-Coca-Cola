package com.epis.dtos.uniforme;

import java.util.List;
import java.util.UUID;

public class UniformeUpdateDto {

    private UUID funcao;
    private List<UniformeEpiDto> uniformeEpis;

    public UniformeUpdateDto() {}

    public UniformeUpdateDto(UUID funcao, List<UniformeEpiDto> uniformeEpis) {
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
        return "UniformeUpdateDto{" +
                "funcao=" + funcao +
                ", uniformeEpis=" + uniformeEpis +
                '}';
    }
}
