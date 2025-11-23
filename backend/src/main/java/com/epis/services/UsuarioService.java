package com.epis.services;

import com.epis.dtos.UsuarioCreateDto;
import com.epis.entities.Usuario;
import com.epis.mapper.UsuarioMapper;
import com.epis.services.exception.ErroInserirDynamoException;
import com.epis.services.exception.UsuarioNaoEncontradoException;
import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

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

    public Usuario insert(UsuarioCreateDto dto) {

        try {

            Usuario usuario = mapper.toUsuario(dto);

            dynamoDbTemplate.save(usuario);

            return usuario;

        } catch (Exception e) {

            throw new ErroInserirDynamoException("Houve um erro as inserir o usuario. Erro: " + e.getMessage());

        }

    }

}
