package com.epis.mapper;

import com.epis.dtos.FuncionarioCreateDto;
import com.epis.dtos.FuncionarioUpdateDto;
import com.epis.entities.Funcionario;
import com.epis.enums.GeneroEnum;
import com.epis.enums.TurnoEnum;

import java.util.Optional;

public class FuncionarioMapper {

    public static Funcionario toFuncionario(FuncionarioCreateDto dto) {
        Funcionario funcionario = new Funcionario();

        funcionario.setNome(dto.getNome());
        funcionario.setFuncao(dto.getFuncao());
        funcionario.setRE(dto.getRE());
        funcionario.setUnidade(dto.getUnidade());
        funcionario.setTurno(TurnoEnum.valueOf(dto.getTurno().toUpperCase()));
        funcionario.setGenero(GeneroEnum.valueOf(dto.getGenero().toUpperCase()));

        return funcionario;
    }

    public static Funcionario toFuncionario(FuncionarioUpdateDto dto) {
        Funcionario funcionario = new Funcionario();
        
        funcionario.setNome(dto.getNome());
        funcionario.setFuncao(dto.getFuncao());
        funcionario.setRE(dto.getRE());
        funcionario.setUnidade(dto.getUnidade());

        if (dto.getTurno() != null) {
            funcionario.setTurno(TurnoEnum.valueOf(dto.getTurno().toUpperCase()));
        }

        if (dto.getGenero() != null) {
            funcionario.setGenero(GeneroEnum.valueOf(dto.getGenero().toUpperCase()));
        }

        return funcionario;
    }

}
