package com.epis.mapper;

import com.epis.dtos.MovimentacaoCreateDto;
import com.epis.entities.Epi;
import com.epis.entities.Funcionario;
import com.epis.entities.Movimentacao;
import com.epis.enums.StatusEnum;
import com.epis.repositories.EpiRepository;
import com.epis.repositories.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MovimentacaoMapper {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private EpiRepository epiRepository;

    public Movimentacao toMovimentacao(MovimentacaoCreateDto dto){
        Movimentacao movimentacao = new Movimentacao();

        Funcionario funcionario = funcionarioRepository.findById(dto.getFuncionario())
                .orElseThrow(() -> new RuntimeException("Funcionario não Encontrado"));

        Epi epi = epiRepository.findById(dto.getEpi())
                .orElseThrow(() -> new RuntimeException("Epi não Encontrado"));

        movimentacao.setFuncionario(funcionario);
        movimentacao.setEpi(epi);
        movimentacao.setDataEntrega(dto.getDataEntrega());
        movimentacao.setDataProximaEntrega(dto.getDataProximaEntrega());
        movimentacao.setStatus(StatusEnum.valueOf(dto.getStatus().toUpperCase()));

        return movimentacao;
    }

}
