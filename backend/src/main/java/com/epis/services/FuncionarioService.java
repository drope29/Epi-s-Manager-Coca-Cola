package com.epis.services;

import com.epis.dtos.FuncionarioCreateDto;
import com.epis.dtos.FuncionarioUpdateDto;
import com.epis.entities.Funcionario;
import com.epis.mapper.FuncionarioMapper;
import com.epis.services.exception.ErroBuscarDynamoException;
import com.epis.services.exception.ErroInserirDynamoException;
import com.epis.services.exception.FuncaoNaoEncontradaException;
import com.epis.services.exception.FuncionarioNaoEncontradoException;
import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.Key;

import java.util.List;
import java.util.UUID;

@Service
public class FuncionarioService {

    @Autowired
    private FuncionarioMapper mapper;

    @Autowired
    private DynamoDbTemplate dynamoDbTemplate;

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
                throw new FuncionarioNaoEncontradoException("Funcionario não encontrada com o id: " + id);

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

            Funcionario teste = funcionario;

            dynamoDbTemplate.save(funcionario);

            return funcionario;

        } catch (Exception e) {

            throw new ErroInserirDynamoException("Houve um erro ao inserir a função. Erro: " + e.getMessage());

        }

    }

    public Funcionario update(UUID id, FuncionarioUpdateDto dto) {

        try {

            var entity = getById(id);

            mapper.toFuncionario(dto, entity);


            return dynamoDbTemplate.update(entity);

        } catch (Exception e) {

            throw new ErroInserirDynamoException("Houve um erro ao inserir a função. Erro: " + e.getMessage());
        }

    }

    public void delete(UUID id) {

        try {

            Funcionario funcionario = getById(id);

            dynamoDbTemplate.delete(funcionario);

        } catch (Exception e) {

            throw new ErroBuscarDynamoException("Houve um erro ao buscar as funções. Erro: " + e.getMessage());

        }

    }

    public void clearFuncionarioDatabase(){

        try {

            List<Funcionario> funcionarios = getAll();

            funcionarios.forEach(dynamoDbTemplate::delete);

        } catch (Exception e) {

            throw new ErroBuscarDynamoException("Houve um erro ao buscar as funções. Erro: " + e.getMessage());

        }

    }

}
