package com.epis.controllers;

import com.epis.dtos.AuthenticationDto;
import com.epis.dtos.ResponseAuthenticatedDto;
import com.epis.dtos.UsuarioUpdateDto;
import com.epis.entities.Usuario;
import com.epis.services.AuthService;
import com.epis.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationDto authDto) {

        ResponseAuthenticatedDto authenticatedDto = new ResponseAuthenticatedDto(authService.login(authDto), authDto.getUsername(), authDto.getPassword());

        return ResponseEntity.ok().body(authenticatedDto);

    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal UserDetails userDetails) {

        Usuario usuario = usuarioService.getByLogin(userDetails.getUsername());

        UsuarioUpdateDto usuarioUpd = new UsuarioUpdateDto( usuario.getPassword(), usuario.getTokenVersion() + 1 );

        usuarioService.update(usuario.getUsuarioId(), usuarioUpd);

        return ResponseEntity.noContent().build();

    }

}
