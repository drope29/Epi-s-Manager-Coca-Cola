package com.epis.services;

import com.epis.dtos.EpiCreateDto;
import com.epis.dtos.EpiUpdateDto;
import com.epis.entities.Epi;
import com.epis.mapper.EpiMapper;
import com.epis.services.exception.ErroBuscarDynamoException;
import com.epis.services.exception.ErroDeletarDynamoException;
import com.epis.services.exception.ErroInserirDynamoException;
import com.epis.services.exception.EpiNaoEncontradoException;
import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.Key;

import java.util.List;
import java.util.UUID;

@Service
public class EpiService {

    @Autowired
    private EpiMapper mapper;

    @Autowired
    private DynamoDbTemplate dynamoDbTemplate;

    public void uploadEpis(List<Epi> epis){

        try {

            epis.forEach(dynamoDbTemplate::save);

        } catch (Exception e) {

            throw new ErroInserirDynamoException("Houve um erro ao inserir os epis. Erro: " + e.getMessage());

        }

    }

    public List<Epi> getAll() {

        try {

            return dynamoDbTemplate
                    .scanAll(Epi.class)
                    .items()
                    .stream()
                    .toList();

        } catch (Exception e) {

            throw new ErroBuscarDynamoException("Houve um erro ao buscar os epis. Erro: " + e.getMessage());

        }

    }

    public Epi getById(UUID id) {

        try {

            var key = Key.builder().partitionValue(String.valueOf(id)).build();

            Epi epi = dynamoDbTemplate.load(key, Epi.class);

            if (epi == null)
                throw new EpiNaoEncontradoException("Epi não encontrada com o id: " + id);

            return epi;

        } catch (EpiNaoEncontradoException ex) {

            throw ex;

        } catch (Exception e) {

            throw new ErroBuscarDynamoException("Houve um erro ao buscar os epis. Erro: " + e.getMessage());

        }

    }

    public Epi insert(EpiCreateDto dto) {

        try {

            Epi epi = mapper.toEpi(dto);

            dynamoDbTemplate.save(epi);

            return epi;

        } catch (Exception e) {

            throw new ErroInserirDynamoException("Houve um erro ao inserir o epi. Erro: " + e.getMessage());

        }

    }

    public Epi update(UUID id, EpiUpdateDto dto) {

        try {

            var entity = getById(id);

            mapper.toEpi(dto, entity);


            return dynamoDbTemplate.update(entity);

        } catch (Exception e) {

            throw new ErroInserirDynamoException("Houve um erro ao inserir o epi. Erro: " + e.getMessage());
        }

    }

    public void delete(UUID id) {

        try {

            Epi epi = getById(id);

            dynamoDbTemplate.delete(epi);

        } catch (Exception e) {

            throw new ErroDeletarDynamoException("Houve um erro ao buscar os epis. Erro: " + e.getMessage());

        }

    }

    public void clearEpiDatabase(){

        try {

            List<Epi> epis = getAll();

            epis.forEach(dynamoDbTemplate::delete);

        } catch (Exception e) {

            throw new ErroDeletarDynamoException("Houve um erro ao buscar os epis. Erro: " + e.getMessage());

        }

    }

}