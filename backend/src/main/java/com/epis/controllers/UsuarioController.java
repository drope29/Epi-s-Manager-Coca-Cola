package com.epis.controllers;

import com.epis.dtos.UsuarioCreateDto;
import com.epis.entities.Usuario;
import com.epis.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @PostMapping("/createUser")
    public ResponseEntity<Usuario> insertUsuario(@Valid @RequestBody UsuarioCreateDto dto) {

        Usuario usuario = service.insert(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);


    }

}
