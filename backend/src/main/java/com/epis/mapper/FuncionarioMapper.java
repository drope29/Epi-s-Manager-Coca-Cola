package com.epis.mapper;

import com.epis.dtos.FuncionarioCreateDto;
import com.epis.dtos.FuncionarioUpdateDto;
import com.epis.entities.Funcionario;

public class FuncionarioMapper {

    public static Funcionario toFuncionario(FuncionarioCreateDto dto) {
        Funcionario funcionario = new Funcionario();

        funcionario.setNome(dto.getNome());
        funcionario.setFuncao(dto.getFuncao());
        funcionario.setRE(dto.getRE());

        return funcionario;
    }

    public static Funcionario toFuncionario(FuncionarioUpdateDto dto) {
        Funcionario funcionario = new Funcionario();

        funcionario.setNome(dto.getNome());
        funcionario.setFuncao(dto.getFuncao());
        funcionario.setRE(dto.getRE());

        return funcionario;
    }

}
