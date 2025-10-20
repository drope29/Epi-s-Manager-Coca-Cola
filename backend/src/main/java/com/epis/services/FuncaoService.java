package com.epis.services;

import com.epis.entities.Funcao;
import com.epis.repositories.FuncaoRepository;
import com.epis.services.exception.FuncaoNaoEncontradaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FuncaoService {

    @Autowired
    private FuncaoRepository repository;

    public void uploadFuncoes(List<Funcao> funcaos){
        repository.saveAll(funcaos);
    }

    public List<Funcao> getAll() {
        return repository.findAll();
    }

    public Funcao getById(Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new FuncaoNaoEncontradaException("Funcao não encontrada com id " + id));

    }

    public Funcao insert(Funcao funcao) { return repository.save(funcao); }

    public Funcao update(Long id, Funcao funcao) {

        Funcao entity = getById(id);

        Optional.ofNullable(funcao.getNome()).ifPresent(entity::setNome);

        return repository.save(entity);

    }

    public void delete(Long id) {

        Funcao funcao = getById(id);

        repository.delete(funcao);

    }
    
}
