package com.epis.services;

import com.epis.dtos.funcionario.FuncionarioCreateDto;
import com.epis.dtos.funcionario.FuncionarioUpdateDto;
import com.epis.entities.Funcionario;
import com.epis.mapper.FuncionarioMapper;
import com.epis.services.exception.*;
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
public class FuncionarioService {

    @Autowired
    private FuncionarioMapper mapper;

    @Autowired
    private DynamoDbTemplate dynamoDbTemplate;

    @Autowired
    private DynamoDbEnhancedClient enhancedClient;

    public void uploadFuncionarios(List<Funcionario> funcionarios){

        try {

            funcionarios.forEach(dynamoDbTemplate::save);

        } catch (Exception e) {

            throw new ErroInserirDynamoException("Houve um erro ao inserir os funcionarios. Erro: " + e.getMessage());

        }

    }

    public List<Funcionario> getAll() {

        try {

            return dynamoDbTemplate
                    .scanAll(Funcionario.class)
                    .items()
                    .stream()
                    .toList();

        } catch (Exception e) {

            throw new ErroBuscarDynamoException("Houve um erro ao buscar os funcionarios. Erro: " + e.getMessage());

        }

    }

    public Funcionario getById(UUID id) {

        try {

            var key = Key.builder().partitionValue(String.valueOf(id)).build();

            Funcionario funcionario = dynamoDbTemplate.load(key, Funcionario.class);

            if (funcionario == null)
                throw new FuncionarioNaoEncontradoException("Funcionario não encontrado com o id: " + id);

            return funcionario;

        } catch (FuncaoNaoEncontradaException ex) {

            throw ex;

        } catch (Exception e) {

            throw new ErroBuscarDynamoException("Houve um erro ao buscar os funcionarios. Erro: " + e.getMessage());

        }

    }

    public Funcionario insert(FuncionarioCreateDto dto) {

        try {

            Funcionario funcionario = mapper.toFuncionario(dto);

            dynamoDbTemplate.save(funcionario);

            return funcionario;

        } catch (Exception e) {

            throw new ErroInserirDynamoException("Houve um erro ao inserir o funcionario. Erro: " + e.getMessage());

        }

    }

    public Funcionario update(UUID id, FuncionarioUpdateDto dto) {

        try {

            var entity = getById(id);

            mapper.toFuncionario(dto, entity);

            return dynamoDbTemplate.update(entity);

        } catch (Exception e) {

            throw new ErroInserirDynamoException("Houve um erro ao atualizar o funcionario. Erro: " + e.getMessage());
        }

    }

    public void delete(UUID id) {

        try {

            Funcionario funcionario = getById(id);

            dynamoDbTemplate.delete(funcionario);

        } catch (Exception e) {

            throw new ErroDeletarDynamoException("Houve um erro ao deletar o funcionario. Erro: " + e.getMessage());

        }

    }

    public void clearFuncionarioDatabase(){

        try {

            List<Funcionario> funcionarios = getAll();

            funcionarios.forEach(dynamoDbTemplate::delete);

        } catch (Exception e) {

            throw new ErroBuscarDynamoException("Houve um erro ao deletar os funcionarios. Erro: " + e.getMessage());

        }

    }

    public Optional<Funcionario> findFuncionarioByNome(String nome) {

        DynamoDbTable<Funcionario> table = enhancedClient.table("funcionario",
                TableSchema.fromBean(Funcionario.class));

        DynamoDbIndex<Funcionario> index = table.index("funcionario-nome-index");

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
