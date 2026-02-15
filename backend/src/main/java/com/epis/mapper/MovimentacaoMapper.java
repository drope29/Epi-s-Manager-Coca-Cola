package com.epis.mapper;

import com.epis.dtos.movimentacao.MovimentacaoCreateDto;
import com.epis.dtos.movimentacao.MovimentacaoResponseDto;
import com.epis.dtos.movimentacao.MovimentacaoUpdateDto;
import com.epis.entities.Epi;
import com.epis.entities.Funcionario;
import com.epis.entities.Movimentacao;
import com.epis.enums.StatusEnum;
import com.epis.services.EpiService;
import com.epis.services.FuncionarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class MovimentacaoMapper {

    @Autowired
    private FuncionarioService funcionarioService;

    @Autowired
    private EpiService epiService;

    public Movimentacao toMovimentacao(MovimentacaoCreateDto dto){

        Movimentacao movimentacao = new Movimentacao();

        funcionarioService.getById(dto.getFuncionario());

        epiService.getById(dto.getEpi());

        movimentacao.setMovimentacaoId(UUID.randomUUID());
        movimentacao.setFuncionarioId(dto.getFuncionario());
        movimentacao.setEpiId(dto.getEpi());
        movimentacao.setDataEntrega(dto.getDataEntrega());
        movimentacao.setDataProximaEntrega(dto.getDataProximaEntrega());
        movimentacao.setStatus(StatusEnum.valueOf(dto.getStatus().toUpperCase()));
        movimentacao.setCadastroAtivo("1");

        return movimentacao;

    }

    public void toMovimentacao(MovimentacaoUpdateDto dto, Movimentacao entity){

        if (dto.getFuncionario() != null) {
            funcionarioService.getById(dto.getFuncionario());
            entity.setFuncionarioId(dto.getFuncionario());
        }

        if (dto.getEpi() != null) {
            epiService.getById(dto.getEpi());
            entity.setEpiId(dto.getEpi());
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

    public MovimentacaoResponseDto toMovimentacaoResponseDto(Movimentacao movimentacao) {

        MovimentacaoResponseDto movimentacaoResponseDto = new MovimentacaoResponseDto(movimentacao);

        Funcionario funcionario = funcionarioService.getById(movimentacao.getFuncionarioId());
        Epi epi = epiService.getById(movimentacao.getEpiId());

        movimentacaoResponseDto.setFuncionario(funcionario);
        movimentacaoResponseDto.setEpi(epi);

        return movimentacaoResponseDto;

    }

    public List<MovimentacaoResponseDto> toMovimentacaoResponseDtoList(List<Movimentacao> movimentacoes) {

        List<MovimentacaoResponseDto> movimentacaoResponseDtoList = new ArrayList<>();

        for (Movimentacao movimentacao: movimentacoes) {

            MovimentacaoResponseDto movimentacaoResponseDto = new MovimentacaoResponseDto(movimentacao);

            Funcionario funcionario = funcionarioService.getById(movimentacao.getFuncionarioId());
            Epi epi = epiService.getById(movimentacao.getEpiId());

            movimentacaoResponseDto.setFuncionario(funcionario);
            movimentacaoResponseDto.setEpi(epi);

            movimentacaoResponseDtoList.add(movimentacaoResponseDto);

        }

        return movimentacaoResponseDtoList;

    }

}
