package com.epis.services;

import com.epis.entities.Movimentacao;
import com.epis.repositories.MovimentacaoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovimentacaoService {

    @Autowired
    private MovimentacaoRepository repository;


    public Movimentacao insert(Movimentacao movimentacao) { return repository.save(movimentacao); }

    public List<Movimentacao> getAll() {
        return repository.findAll();
    }

    public Movimentacao getById(Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Movimentação não encontrada com id " + id));
    }

    public Movimentacao update(Long id, Movimentacao movimentacao) {

        Movimentacao entity = getById(id);

        Optional.ofNullable(movimentacao.getFuncionario()).ifPresent(entity::setFuncionario);
        Optional.ofNullable(movimentacao.getEpi()).ifPresent(entity::setEpi);
        Optional.ofNullable(movimentacao.getDataEntrega()).ifPresent(entity::setDataEntrega);
        Optional.ofNullable(movimentacao.getDataProximaEntrega()).ifPresent(entity::setDataProximaEntrega);
        Optional.ofNullable(movimentacao.getStatus()).ifPresent(entity::setStatus);

        return repository.save(entity);

    }

    public void delete(Long id) {

        Movimentacao movimentacao = getById(id);

        repository.delete(movimentacao);

    }
}
