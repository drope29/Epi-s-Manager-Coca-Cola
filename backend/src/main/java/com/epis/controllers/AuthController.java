package com.epis.controllers;

import com.epis.dtos.AuthenticationDto;
import com.epis.dtos.ResponseAuthenticatedDto;
import com.epis.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationDto authDto) {

        ResponseAuthenticatedDto authenticatedDto = new ResponseAuthenticatedDto(authService.login(authDto), authDto.getUsername(), authDto.getPassword());

        return ResponseEntity.ok().body(authenticatedDto);

    }

}
