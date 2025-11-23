package com.epis.mapper;

import com.epis.dtos.UsuarioCreateDto;
import com.epis.entities.Usuario;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UsuarioMapper {

    private final PasswordEncoder encoder;

    public UsuarioMapper(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    public Usuario toUsuario(UsuarioCreateDto dto) {
        return new Usuario(
                UUID.randomUUID(),
                dto.getUsername(),
                encoder.encode(dto.getPassword())
        );
    }

}
