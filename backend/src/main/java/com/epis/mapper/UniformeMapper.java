package com.epis.mapper;

import com.epis.dtos.UniformeCreateDto;
import com.epis.dtos.UniformeUpdateDto;
import com.epis.entities.Epi;
import com.epis.entities.Funcao;
import com.epis.entities.Uniforme;
import com.epis.repositories.EpiRepository;
import com.epis.repositories.FuncaoRepository;
import com.epis.services.exception.EpiNaoEncontradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UniformeMapper {

    @Autowired
    private FuncaoRepository funcaoRepository;

    @Autowired
    private EpiRepository epiRepository;

    public Uniforme toUniforme(UniformeCreateDto dto) {

        Uniforme uniforme = new Uniforme();

        Epi epi = epiRepository.findById(dto.getEpi())
                .orElseThrow(() -> new EpiNaoEncontradoException("Epi não encontrado com id " + dto.getEpi()));

        Funcao funcao = funcaoRepository.findById(dto.getFuncao())
                .orElseThrow(() -> new EpiNaoEncontradoException("Função não encontrada com id " + dto.getFuncao()));

        uniforme.setEpi(epi);
        uniforme.setFuncao(funcao);
        uniforme.setQuantidade(dto.getQuantidade());

        return uniforme;
    }

    public void toUniforme(UniformeUpdateDto dto, Uniforme uniforme) {

        if (dto.getEpi() != null) {

            uniforme.setEpi(epiRepository.findById(dto.getEpi())
                    .orElseThrow(() -> new EpiNaoEncontradoException("Epi não encontrado com id " + dto.getEpi())));

        }

        if (dto.getFuncao() != null) {

            uniforme.setFuncao(funcaoRepository.findById(dto.getFuncao())
                    .orElseThrow(() -> new EpiNaoEncontradoException("Função não encontrada com id " + dto.getFuncao())));

        }

        if (dto.getQuantidade() != null) {

            uniforme.setQuantidade(dto.getQuantidade());

        }

    }

}
