package com.epis.dtos.usuario;

import java.util.UUID;

public class UsuarioCreateDto {

    private String username;
    private String password;
    private UUID funcionarioId;

    public UsuarioCreateDto(String username, String password, UUID funcionarioId) {
        this.username = username;
        this.password = password;
        this.funcionarioId = funcionarioId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UUID getFuncionarioId() { return funcionarioId; }

    public void setFuncionarioId(UUID funcionarioId) { this.funcionarioId = funcionarioId; }

    @Override
    public String toString() {
        return "UsuarioCreateDto{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", funcionarioId=" + funcionarioId +
                '}';
    }
}
