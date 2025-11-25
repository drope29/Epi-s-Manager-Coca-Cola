package com.epis.mapper;

import com.epis.dtos.UsuarioCreateDto;
import com.epis.entities.Funcionario;
import com.epis.entities.Usuario;
import com.epis.services.FuncionarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UsuarioMapper {

    @Autowired
    private FuncionarioService funcionarioService;

    private final PasswordEncoder encoder;

    public UsuarioMapper(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    public Usuario toUsuario(UsuarioCreateDto dto) {

        return new Usuario(
                UUID.randomUUID(),
                dto.getUsername(),
                encoder.encode(dto.getPassword()),
                dto.getFuncionarioId()
        );
    }

}
