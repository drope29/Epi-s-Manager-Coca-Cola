package com.epis.mapper;

import com.epis.dtos.FuncionarioCreateDto;
import com.epis.dtos.FuncionarioUpdateDto;
import com.epis.entities.Funcao;
import com.epis.entities.Funcionario;
import com.epis.enums.GeneroEnum;
import com.epis.enums.TurnoEnum;
import com.epis.services.FuncaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class FuncionarioMapper {

    @Autowired
    private FuncaoService funcaoService;

    public Funcionario toFuncionario(FuncionarioCreateDto dto) {

        Funcionario funcionario = new Funcionario();

        Funcao funcao = funcaoService.getById(dto.getFuncao());

        funcionario.setFuncionarioId(UUID.randomUUID());
        funcionario.setNome(dto.getNome());
        funcionario.setFuncaoId(funcao.getId());
        funcionario.setRE(dto.getRE());
        funcionario.setUnidade(dto.getUnidade());
        funcionario.setTurno(TurnoEnum.valueOf(dto.getTurno().toUpperCase()));
        funcionario.setGenero(GeneroEnum.valueOf(dto.getGenero().toUpperCase()));
        funcionario.setDataAdmissao(dto.getDataAdmissao());
        funcionario.setSetor(dto.getSetor());

        return funcionario;

    }

    public void toFuncionario(FuncionarioUpdateDto dto, Funcionario funcionario) {

        if (dto.getNome() != null) {
            funcionario.setNome(dto.getNome());
        }

        if (dto.getFuncao() != null) {

            Funcao funcao = funcaoService.getById(dto.getFuncao());

            funcionario.setFuncaoId(funcao.getId());
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

        if (dto.getDataAdmissao() != null) {
            funcionario.setDataAdmissao(dto.getDataAdmissao());
        }

        if (dto.getSetor() != null) {
            funcionario.setSetor(dto.getSetor());
        }

    }

}
