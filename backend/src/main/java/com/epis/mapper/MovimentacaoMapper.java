package com.epis.mapper;

import com.epis.dtos.movimentacao.MovimentacaoCreateDto;
import com.epis.dtos.movimentacao.MovimentacaoUpdateDto;
import com.epis.entities.Epi;
import com.epis.entities.Funcionario;
import com.epis.entities.Movimentacao;
import com.epis.enums.StatusEnum;
import com.epis.services.EpiService;
import com.epis.services.FuncionarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class MovimentacaoMapper {

    @Autowired
    private FuncionarioService funcionarioService;

    @Autowired
    private EpiService epiService;

    public Movimentacao toMovimentacao(MovimentacaoCreateDto dto){

        Movimentacao movimentacao = new Movimentacao();

        Funcionario funcionario = funcionarioService.getById(dto.getFuncionario());

        Epi epi = epiService.getById(dto.getEpi());

        movimentacao.setMovimentacaoId(UUID.randomUUID());
        movimentacao.setFuncionario(funcionario);
        movimentacao.setEpi(epi);
        movimentacao.setDataEntrega(dto.getDataEntrega());
        movimentacao.setDataProximaEntrega(dto.getDataProximaEntrega());
        movimentacao.setStatus(StatusEnum.valueOf(dto.getStatus().toUpperCase()));

        return movimentacao;

    }

    public void toMovimentacao(MovimentacaoUpdateDto dto, Movimentacao entity){

        if (dto.getFuncionario() != null) {
            entity.setFuncionario(funcionarioService.getById(dto.getFuncionario()));
        }

        if (dto.getEpi() != null) {
            entity.setEpi(epiService.getById(dto.getEpi()));
        }

        if (dto.getDataEntrega() != null) {
            entity.setDataEntrega(dto.getDataEntrega());
        }

        if (dto.getDataProximaEntrega() != null) {
            entity.setDataProximaEntrega(dto.getDataProximaEntrega());
        }

        if (dto.getStatus() != null) {
            entity.setStatus(StatusEnum.valueOf(dto.getStatus().toUpperCase()));
        }

    }

}
