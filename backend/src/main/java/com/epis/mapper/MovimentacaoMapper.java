package com.epis.mapper;

import org.springframework.stereotype.Component;

@Component
public class MovimentacaoMapper {
/*
    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private EpiRepository epiRepository;

    public Movimentacao toMovimentacao(MovimentacaoCreateDto dto){
        Movimentacao movimentacao = new Movimentacao();

        Funcionario funcionario = funcionarioRepository.findById(dto.getFuncionario())
                .orElseThrow(() -> new FuncionarioNaoEncontradoException("Funcionario não Encontrado"));

        Epi epi = epiRepository.findById(dto.getEpi())
                .orElseThrow(() -> new EpiNaoEncontradoException("Epi não Encontrado"));

        movimentacao.setFuncionario(funcionario);
        movimentacao.setEpi(epi);
        movimentacao.setDataEntrega(dto.getDataEntrega());
        movimentacao.setDataProximaEntrega(dto.getDataProximaEntrega());
        movimentacao.setStatus(StatusEnum.valueOf(dto.getStatus().toUpperCase()));

        return movimentacao;
    }

    public void toMovimentacao(MovimentacaoUpdateDto dto, Movimentacao entity){
        if (dto.getFuncionario() != null) {
            Funcionario funcionario = funcionarioRepository.findById(dto.getFuncionario())
                    .orElseThrow(() -> new FuncionarioNaoEncontradoException("Funcionario não Encontrado"));
            entity.setFuncionario(funcionario);
        }

        if (dto.getEpi() != null) {
            Epi epi = epiRepository.findById(dto.getEpi())
                    .orElseThrow(() -> new EpiNaoEncontradoException("Epi não Encontrado"));
            entity.setEpi(epi);
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

    }*/

}
