package com.epis.services;

import com.epis.dtos.usuario.UsuarioUpdateDto;
import com.epis.dtos.usuario.UsuarioCreateDto;
import com.epis.entities.Usuario;
import com.epis.mapper.UsuarioMapper;
import com.epis.services.exception.*;
import com.epis.services.exception.UsuarioNaoEncontradoException;
import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

@Service
public class UsuarioService {

    @Autowired
    private DynamoDbTemplate dynamoDbTemplate;

    @Autowired
    private DynamoDbEnhancedClient enhancedClient;

    @Autowired
    private UsuarioMapper mapper;

    public Usuario getByLogin(String username) {

        DynamoDbTable<Usuario> table = enhancedClient.table("usuario",
                TableSchema.fromBean(Usuario.class));

        DynamoDbIndex<Usuario> index = table.index("username-index");

        QueryEnhancedRequest request = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(
                        Key.builder().partitionValue(username).build()
                ))
                .limit(1)
                .build();

        SdkIterable<Page<Usuario>> results = index.query(request);

        Usuario usuario = StreamSupport.stream(results.spliterator(), false)
                .flatMap(page -> StreamSupport.stream(page.items().spliterator(), false))
                .findFirst()
                .orElse(null);

        if (usuario == null) {
            throw new UsuarioNaoEncontradoException("Usuário não encontrado ou credenciais inválidas");
        }

        return usuario;

    }

    public List<Usuario> getAll() {

        try {

            return dynamoDbTemplate
                    .scanAll(Usuario.class)
                    .items()
                    .stream()
                    .toList();

        } catch (Exception e) {

            throw new ErroBuscarDynamoException("Houve um erro ao buscar os usuarios. Erro: " + e.getMessage());

        }

    }

    public Usuario getById(UUID id) {

        try {

            var key = Key.builder().partitionValue(String.valueOf(id)).build();

            Usuario usuario = dynamoDbTemplate.load(key, Usuario.class);

            if (usuario == null)
                throw new UsuarioNaoEncontradoException("Usuario não encontrado com o id: " + id);

            return usuario;

        } catch (UsuarioNaoEncontradoException ex) {

            throw ex;

        } catch (Exception e) {

            throw new ErroBuscarDynamoException("Houve um erro ao buscar os usuarios. Erro: " + e.getMessage());

        }

    }

    public Usuario insert(UsuarioCreateDto dto) {

        try {

            Usuario usuario = mapper.toUsuario(dto);

            dynamoDbTemplate.save(usuario);

            return usuario;

        } catch (Exception e) {

            throw new ErroInserirDynamoException("Houve um erro as inserir o usuario. Erro: " + e.getMessage());

        }

    }

    public Usuario update(UUID id, UsuarioUpdateDto dto) {

        try {

            var entity = getById(id);

            entity.setPassword(dto.getPassword());
            entity.setTokenVersion(dto.getTokenVersion());

            return dynamoDbTemplate.update(entity);

        } catch (Exception e) {

            throw new ErroInserirDynamoException("Houve um erro ao atualizar o usuario. Erro: " + e.getMessage());
        }

    }

    public void delete(UUID id) {

        try {

            Usuario usuario = getById(id);

            dynamoDbTemplate.delete(usuario);

        } catch (Exception e) {

            throw new ErroDeletarDynamoException("Houve um erro ao buscar os usuarios. Erro: " + e.getMessage());

        }

    }

    public boolean verifyByLogin(String username) {

        DynamoDbTable<Usuario> table = enhancedClient.table("usuario",
                TableSchema.fromBean(Usuario.class));

        DynamoDbIndex<Usuario> index = table.index("username-index");

        QueryEnhancedRequest request = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(
                        Key.builder().partitionValue(username).build()
                ))
                .limit(1)
                .build();

        SdkIterable<Page<Usuario>> results = index.query(request);

        Usuario usuario = StreamSupport.stream(results.spliterator(), false)
                .flatMap(page -> StreamSupport.stream(page.items().spliterator(), false))
                .findFirst()
                .orElse(null);

        return usuario != null;



    }

}
