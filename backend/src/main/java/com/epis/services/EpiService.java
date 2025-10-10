package com.epis.services;

import com.epis.entities.Epi;
import com.epis.entities.Funcionario;
import com.epis.repositories.EpiRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EpiService {

    @Autowired
    private EpiRepository repository;

    public void uploadEpi(List<Epi> epi){
        repository.saveAll(epi);
    }

    public List<Epi> getAll() {
        return repository.findAll();
    }

    public Epi getById(Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Epi não encontrado com id " + id));

    }

    public Epi insert(Epi epi) {

        return repository.save(epi);

    }

    public Epi update(Long id, Epi epi) {

        Epi entity = getById(id);

        Optional.ofNullable(epi.getCodigoAutenticacao()).ifPresent(entity::setCodigoAutenticacao);
        Optional.ofNullable(epi.getCodigoCompra()).ifPresent(entity::setCodigoCompra);
        Optional.ofNullable(epi.getDescricao()).ifPresent(entity::setDescricao);
        Optional.ofNullable(epi.getDataValidade()).ifPresent(entity::setDataValidade);

        return repository.save(entity);

    }

    public void delete(Long id) {

        Epi epi = getById(id);

        repository.delete(epi);

    }

}
