package com.epis.mapper;

import com.epis.dtos.FuncionarioCreateDto;
import com.epis.dtos.FuncionarioUpdateDto;
import com.epis.entities.Funcionario;
import com.epis.enums.GeneroEnum;
import com.epis.enums.TurnoEnum;
import org.springframework.stereotype.Component;

@Component
public class FuncionarioMapper {

    public Funcionario toFuncionario(FuncionarioCreateDto dto) {
        Funcionario funcionario = new Funcionario();

        funcionario.setNome(dto.getNome());
        funcionario.setFuncao(dto.getFuncao());
        funcionario.setRE(dto.getRE());
        funcionario.setUnidade(dto.getUnidade());
        funcionario.setTurno(TurnoEnum.valueOf(dto.getTurno().toUpperCase()));
        funcionario.setGenero(GeneroEnum.valueOf(dto.getGenero().toUpperCase()));

        return funcionario;
    }

    public void toFuncionario(FuncionarioUpdateDto dto, Funcionario funcionario) {

        if (dto.getNome() != null) {
            funcionario.setNome(dto.getNome());
        }

        if (dto.getFuncao() != null) {
            funcionario.setFuncao(dto.getFuncao());
        }

        if (dto.getRE() != null) {
            funcionario.setRE(dto.getRE());
        }

        if (dto.getUnidade() != null) {
            funcionario.setUnidade(dto.getUnidade());
        }

        if (dto.getTurno() != null) {
            funcionario.setTurno(TurnoEnum.valueOf(dto.getTurno().toUpperCase()));
        }

        if (dto.getGenero() != null) {
            funcionario.setGenero(GeneroEnum.valueOf(dto.getGenero().toUpperCase()));
        }

    }

}
