package com.epis.controllers;

import com.epis.dtos.usuario.UsuarioUpdateDto;
import com.epis.dtos.usuario.UsuarioCreateDto;
import com.epis.entities.Usuario;
import com.epis.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @GetMapping("/")
    public ResponseEntity<List<Usuario>> getAllUsuarios() {

        List<Usuario> usuarios = service.getAll();

        return ResponseEntity.ok(usuarios);

    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable UUID id) {

        Usuario usuarios = service.getById(id);

        return ResponseEntity.ok(usuarios);

    }

    @PostMapping("/")
    public ResponseEntity<Usuario> insertUsuario(@Valid @RequestBody UsuarioCreateDto dto) {

        Usuario usuario = service.insert(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);

    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> updateUsuario(@PathVariable UUID id, @RequestBody UsuarioUpdateDto dto) {

        Usuario usuarioUpd = service.update(id, dto);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(usuarioUpd);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUsuario(@PathVariable UUID id) {

        service.delete(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

}
