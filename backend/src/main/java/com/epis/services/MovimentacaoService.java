package com.epis.services;

import com.epis.dtos.MovimentacaoCreateDto;
import com.epis.dtos.MovimentacaoUpdateDto;
import com.epis.entities.Movimentacao;
import com.epis.mapper.MovimentacaoMapper;
import com.epis.repositories.MovimentacaoRepository;
import com.epis.services.exception.MovimentacaoNaoEncontradaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovimentacaoService {

    @Autowired
    private MovimentacaoRepository repository;

    @Autowired
    private MovimentacaoMapper mapper;

    public List<Movimentacao> getAll() {
        return repository.findAll();
    }

    public Movimentacao getById(Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new MovimentacaoNaoEncontradaException("Movimentação não encontrada com id " + id));

    }

    public Movimentacao insert(MovimentacaoCreateDto dto) {

        Movimentacao movimentacao = mapper.toMovimentacao(dto);

        return repository.save(movimentacao);

    }

    public Movimentacao update(Long id, MovimentacaoUpdateDto dto) {

        Movimentacao entity = getById(id);

        mapper.toMovimentacao(dto, entity);

        return repository.save(entity);
    }

    public void delete(Long id) {

        Movimentacao movimentacao = getById(id);

        repository.delete(movimentacao);

    }
}
