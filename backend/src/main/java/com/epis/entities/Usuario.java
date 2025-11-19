package com.epis.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Usuario {


    private UUID usuarioId;
    private String nome;
    private String email;
    private String password;
    private ArrayList<?> authorities;

    public Usuario(){}

    public Usuario(UUID usuarioId, String nome, String email, String password, ArrayList<?> authorities) {
        this.usuarioId = UUID.randomUUID();
        this.nome = nome;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public UUID getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(UUID usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<?> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(ArrayList<?> authorities) {
        this.authorities = authorities;
    }
}
