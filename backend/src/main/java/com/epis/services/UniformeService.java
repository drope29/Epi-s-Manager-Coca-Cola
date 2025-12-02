package com.epis.services;

import com.epis.dtos.uniforme.UniformeCreateDto;
import com.epis.dtos.uniforme.UniformeUpdateDto;
import com.epis.entities.Uniforme;
import com.epis.mapper.UniformeMapper;
import com.epis.services.exception.*;
import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.Key;

import java.util.List;
import java.util.UUID;

@Service
public class UniformeService {

    @Autowired
    private UniformeMapper mapper;

    @Autowired
    private DynamoDbTemplate dynamoDbTemplate;

    public List<Uniforme> getAll() {

        try {

            return dynamoDbTemplate
                    .scanAll(Uniforme.class)
                    .items()
                    .stream()
                    .toList();

        } catch (Exception e) {

            throw new ErroBuscarDynamoException("Houve um erro ao buscar os uniformes. Erro: " + e.getMessage());

        }

    }

    public Uniforme getById(UUID id) {

        try {

            var key = Key.builder().partitionValue(String.valueOf(id)).build();

            Uniforme uniforme = dynamoDbTemplate.load(key, Uniforme.class);

            if (uniforme == null)
                throw new UniformeNaoEncontradoException("Uniforme não encontrada com o id: " + id);

            return uniforme;

        } catch (UniformeNaoEncontradoException ex) {

            throw ex;

        } catch (Exception e) {

            throw new ErroBuscarDynamoException("Houve um erro ao buscar o uniforme. Erro: " + e.getMessage());

        }

    }

    public Uniforme insert(UniformeCreateDto dto) {

        try {

            Uniforme uniforme = mapper.toUniforme(dto);

            dynamoDbTemplate.save(uniforme);

            return uniforme;

        } catch (Exception e) {

            throw new ErroInserirDynamoException("Houve um erro as inserir o uniforme. Erro: " + e.getMessage());

        }

    }

    public Uniforme update(UUID id, UniformeUpdateDto dto) {

        try {

            var entity = getById(id);

            mapper.toUniforme(dto, entity);


            return dynamoDbTemplate.update(entity);

        } catch (Exception e) {

            throw new ErroInserirDynamoException("Houve um erro ao atualizar o uniforme. Erro: " + e.getMessage());
        }

    }

    public void delete(UUID id) {

        try {

            Uniforme uniforme = getById(id);

            dynamoDbTemplate.delete(uniforme);

        } catch (Exception e) {

            throw new ErroDeletarDynamoException("Houve um erro ao buscar os uniformes. Erro: " + e.getMessage());

        }

    }
    
}
