package com.epis.dtos.usuario;

public class UsuarioUpdateDto {

    private String password;
    private Integer tokenVersion;

    public UsuarioUpdateDto(){}

    public UsuarioUpdateDto(String password) {
        this.password = password;
    }

    public UsuarioUpdateDto(String password, Integer tokenVersion) {
        this.password = password;
        this.tokenVersion = tokenVersion;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getTokenVersion() {
        return tokenVersion;
    }

    public void setTokenVersion(Integer tokenVersion) {
        this.tokenVersion = tokenVersion;
    }

    @Override
    public String toString() {
        return "UsuarioUpdateDto{" +
                "password='" + password + '\'' +
                ", tokenVersion=" + tokenVersion +
                '}';
    }
}
