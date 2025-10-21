package com.epis.services;

import com.epis.dtos.UniformeCreateDto;
import com.epis.dtos.UniformeUpdateDto;
import com.epis.entities.Uniforme;
import com.epis.mapper.UniformeMapper;
import com.epis.repositories.UniformeRepoitory;
import com.epis.services.exception.UniformeNaoEncontradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UniformeService {

    @Autowired
    private UniformeRepoitory repoitory;

    @Autowired
    private UniformeMapper mapper;

    public List<Uniforme> getAll() {
        return repoitory.findAll();
    }

    public Uniforme getById(Long id) {

        return repoitory.findById(id)
                .orElseThrow(() -> new UniformeNaoEncontradoException("Uniforme não encontrado com id " + id));

    }

    public Uniforme insert(UniformeCreateDto dto) {

        Uniforme uniforme = mapper.toUniforme(dto);

        return repoitory.save(uniforme);
    }

    public Uniforme update(Long id, UniformeUpdateDto dto) {

        Uniforme entity = getById(id);

        mapper.toUniforme(dto, entity);

        return repoitory.save(entity);

    }

    public void delete(Long id) {

        Uniforme uniforme = getById(id);

        repoitory.delete(uniforme);

    }
}
