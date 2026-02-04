package com.epis.services;

import com.epis.entities.Funcao;
import com.epis.services.exception.ErroBuscarDynamoException;
import com.epis.services.exception.ErroInserirDynamoException;
import com.epis.services.exception.FuncaoNaoEncontradaException;
import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;

@Service
public class FuncaoService {

    @Autowired
    private DynamoDbTemplate dynamoDbTemplate;

    @Autowired
    private DynamoDbEnhancedClient enhancedClient;


    public void uploadFuncoes(List<Funcao> funcoes) {

        try {

            funcoes.forEach(dynamoDbTemplate::save);

        } catch (Exception e) {

            throw new ErroInserirDynamoException("Houve um erro ao inserir as funções. Erro: " + e.getMessage());

        }
    }

    public List<Funcao> getAll() {

        try {

            return dynamoDbTemplate
                    .scanAll(Funcao.class)
                    .items()
                    .stream()
                    .toList();

        } catch (Exception e) {

            throw new ErroBuscarDynamoException("Houve um erro ao buscar as funções. Erro: " + e.getMessage());

        }

    }

    public Funcao getById(UUID id) {

        try {

            var key = Key.builder().partitionValue(String.valueOf(id)).build();

            Funcao funcao = dynamoDbTemplate.load(key, Funcao.class);

            if (funcao == null)
                throw new FuncaoNaoEncontradaException("Função não encontrada com o id: " + id);

            return funcao;

        } catch (FuncaoNaoEncontradaException ex) {

            throw ex;

        } catch (Exception e) {

            throw new ErroBuscarDynamoException("Houve um erro ao buscar as funções. Erro: " + e.getMessage());

        }

    }

    public Funcao insert(Funcao funcao) {

        try {

            funcao.setId(UUID.randomUUID());

            dynamoDbTemplate.save(funcao);

        } catch (Exception e) {

            throw new ErroInserirDynamoException("Houve um erro ao inserir a função. Erro: " + e.getMessage());

        }

        return funcao;
    }

    public Funcao update(UUID id, Funcao funcao) {

        try {

            var entity = getById(id);

            Optional.ofNullable(funcao.getNome()).ifPresent(entity::setNome);

            return dynamoDbTemplate.update(entity);

        } catch (Exception e) {

            throw new ErroInserirDynamoException("Houve um erro ao inserir a função. Erro: " + e.getMessage());
        }

    }

    public void delete(UUID id) {

        try {

            Funcao funcao = getById(id);

            dynamoDbTemplate.delete(funcao);

        } catch (Exception e) {

            throw new ErroBuscarDynamoException("Houve um erro ao buscar as funções. Erro: " + e.getMessage());

        }

    }

    public Optional<Funcao> findFuncaoByNome(String nome) {

        DynamoDbTable<Funcao> table = enhancedClient.table("funcao",
                TableSchema.fromBean(Funcao.class));

        DynamoDbIndex<Funcao> index = table.index("funcao-nome-index");

        QueryEnhancedRequest request = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(
                        Key.builder().partitionValue(nome).build()
                ))
                .limit(1)
                .build();

        return StreamSupport.stream(index.query(request).spliterator(), false)
                .flatMap(page -> StreamSupport.stream(page.items().spliterator(), false))
                .findFirst();

    }

}
