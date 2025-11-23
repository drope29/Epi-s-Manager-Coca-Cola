package com.epis.dtos;

public class UsuarioUpdateDto {

    private String password;

    public UsuarioUpdateDto(){}

    public UsuarioUpdateDto(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UsuarioUpdateDto{" +
                "password='" + password + '\'' +
                '}';
    }
}
