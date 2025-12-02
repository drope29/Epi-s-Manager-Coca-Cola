package com.epis.services;

import com.epis.dtos.movimentacao.MovimentacaoCreateDto;
import com.epis.dtos.movimentacao.MovimentacaoUpdateDto;
import com.epis.entities.Movimentacao;
import com.epis.mapper.MovimentacaoMapper;
import com.epis.services.exception.ErroBuscarDynamoException;
import com.epis.services.exception.ErroDeletarDynamoException;
import com.epis.services.exception.ErroInserirDynamoException;
import com.epis.services.exception.MovimentacaoNaoEncontradaException;
import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.Key;

import java.util.List;
import java.util.UUID;

@Service
public class MovimentacaoService {

    @Autowired
    private MovimentacaoMapper mapper;

    @Autowired
    private DynamoDbTemplate dynamoDbTemplate;

    public List<Movimentacao> getAll() {

        try {

            return dynamoDbTemplate
                    .scanAll(Movimentacao.class)
                    .items()
                    .stream()
                    .toList();

        } catch (Exception e) {

            throw new ErroBuscarDynamoException("Houve um erro ao buscar as movimentacoes. Erro: " + e.getMessage());

        }

    }

    public Movimentacao getById(UUID id) {

        try {

            var key = Key.builder().partitionValue(String.valueOf(id)).build();

            Movimentacao movimentacao = dynamoDbTemplate.load(key, Movimentacao.class);

            if (movimentacao == null)
                throw new MovimentacaoNaoEncontradaException("Movimentacao não encontrada com o id: " + id);

            return movimentacao;

        } catch (MovimentacaoNaoEncontradaException ex) {

            throw ex;

        } catch (Exception e) {

            throw new ErroBuscarDynamoException("Houve um erro ao buscar as movimentacoes. Erro: " + e.getMessage());

        }

    }

    public Movimentacao insert(MovimentacaoCreateDto dto) {

        try {

            Movimentacao movimentacao = mapper.toMovimentacao(dto);

            dynamoDbTemplate.save(movimentacao);

            return movimentacao;

        } catch (Exception e) {

            throw new ErroInserirDynamoException("Houve um erro as inserir a movimentação. Erro: " + e.getMessage());

        }

    }

    public Movimentacao update(UUID id, MovimentacaoUpdateDto dto) {

        try {

            var entity = getById(id);

            mapper.toMovimentacao(dto, entity);

            return dynamoDbTemplate.update(entity);

        } catch (Exception e) {

            throw new ErroInserirDynamoException("Houve um erro ao atualizar a movimentação. Erro: " + e.getMessage());
        }

    }

    public void delete(UUID id) {

        try {

            Movimentacao movimentacao = getById(id);

            dynamoDbTemplate.delete(movimentacao);

        } catch (Exception e) {

            throw new ErroDeletarDynamoException("Houve um erro ao buscar as movimentacoes. Erro: " + e.getMessage());

        }

    }

}