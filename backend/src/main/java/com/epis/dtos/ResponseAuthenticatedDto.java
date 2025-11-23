package com.epis.dtos;

public class ResponseAuthenticatedDto {

    private AccessDto token;
    private String username;
    private String password;

    public ResponseAuthenticatedDto(AccessDto token, String username, String password) {
        this.token = token;
        this.username = username;
        this.password = password;
    }

    public AccessDto getToken() {
        return token;
    }

    public void setToken(AccessDto token) {
        this.token = token;
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

    @Override
    public String toString() {
        return "ResponseAuthenticatedDto{" +
                "token='" + token + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
