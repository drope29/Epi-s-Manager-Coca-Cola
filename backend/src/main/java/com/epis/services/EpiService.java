package com.epis.services;

import com.epis.dtos.EpiCreateDto;
import com.epis.dtos.EpiUpdateDto;
import com.epis.entities.Epi;
import com.epis.mapper.EpiMapper;
import com.epis.repositories.EpiRepository;
import com.epis.services.exception.EpiNaoEncontradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EpiService {

    @Autowired
    private EpiRepository repository;

    @Autowired
    private EpiMapper mapper;

    public void uploadEpi(List<Epi> epi){
        repository.saveAll(epi);
    }

    public List<Epi> getAll() {
        return repository.findAll();
    }

    public Epi getById(Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new EpiNaoEncontradoException("Epi não encontrado com id " + id));

    }

    public Epi insert(EpiCreateDto dto) {

        Epi epi = mapper.toEpi(dto);

        return repository.save(epi);

    }

    public Epi update(Long id, EpiUpdateDto dto) {

        Epi entity = getById(id);

        mapper.toEpi(dto, entity);

        return repository.save(entity);

    }

    public void delete(Long id) {

        Epi epi = getById(id);

        repository.delete(epi);

    }

}
