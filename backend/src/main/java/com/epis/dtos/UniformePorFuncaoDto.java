package com.epis.dtos;

import com.epis.entities.Epi;
import com.epis.entities.Funcao;

import java.util.List;

public class UniformePorFuncaoDto {

    private Funcao funcao;
    private List<Epi> epis;

    public UniformePorFuncaoDto() {}

    public UniformePorFuncaoDto(List<Epi> epis, Funcao funcao) {
        this.epis = epis;
        this.funcao = funcao;
    }

    public Funcao getFuncao() {
        return funcao;
    }

    public void setFuncao(Funcao funcao) {
        this.funcao = funcao;
    }

    public List<Epi> getEpis() {
        return epis;
    }

    public void setEpis(List<Epi> epis) {
        this.epis = epis;
    }
}
