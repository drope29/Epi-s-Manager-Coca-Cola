package com.epis.services;

import com.epis.dtos.UniformeCreateDto;
import com.epis.dtos.UniformePorFuncaoDto;
import com.epis.dtos.UniformeUpdateDto;
import com.epis.entities.Epi;
import com.epis.entities.Funcao;
import com.epis.entities.Uniforme;
import com.epis.mapper.UniformeMapper;
import com.epis.repositories.UniformeRepoitory;
import com.epis.services.exception.UniformeNaoEncontradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UniformeService {

   /* @Autowired
    private UniformeRepoitory repository;

    @Autowired
    private UniformeMapper mapper;

    public List<Uniforme> getAll() {
        return repository.findAll();
    }

    public Uniforme getById(Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new UniformeNaoEncontradoException("Uniforme não encontrado com id " + id));

    }

    public Uniforme insert(UniformeCreateDto dto) {

        Uniforme uniforme = mapper.toUniforme(dto);

        return repository.save(uniforme);
    }

    public Uniforme update(Long id, UniformeUpdateDto dto) {

        Uniforme entity = getById(id);

        mapper.toUniforme(dto, entity);

        return repository.save(entity);

    }

    public void delete(Long id) {

        Uniforme uniforme = getById(id);

        repository.delete(uniforme);

    }

    public UniformePorFuncaoDto getUniformesPorFuncao(Long funcaoId) {

        List<Uniforme> uniformes = repository.buscarEpisPorFuncao(funcaoId);

        if (uniformes.isEmpty()) {
            return null;
        }

        Funcao funcao = uniformes.get(0).getFuncao();
        List<Epi> epis = uniformes.stream()
                .map(Uniforme::getEpi)
                .toList();

        return new UniformePorFuncaoDto(epis, funcao);

    }*/
}
