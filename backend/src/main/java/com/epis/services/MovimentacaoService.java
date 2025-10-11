package com.epis.services;

import com.epis.entities.Movimentacao;
import com.epis.repositories.MovimentacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovimentacaoService {

    @Autowired
    private MovimentacaoRepository repository;


    public Movimentacao insert(Movimentacao movimentacao) {
        return repository.save(movimentacao);
    }

    public List<Movimentacao> getAll() {
        return repository.findAll();
    }
}
