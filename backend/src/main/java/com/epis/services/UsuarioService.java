package com.epis.services;

import com.epis.entities.Usuario;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {


    public Usuario getByLogin() {
        return new Usuario();
    }

}
