package com.epis.entities;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;

import java.util.UUID;

@DynamoDbBean
public class Usuario {

    private UUID usuarioId;
    private String username;
    private String password;
    private UUID funcionarioId;
    private Integer tokenVersion;

    public Usuario(){}

    public Usuario(UUID usuarioId, String username, String password, UUID funcionarioId) {
        this.usuarioId = UUID.randomUUID();
        this.username = username;
        this.password = password;
        this.funcionarioId = funcionarioId;
        this.tokenVersion = 0;
    }

    @DynamoDbPartitionKey
    @DynamoDbAttribute("usuarioId")
    public UUID getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(UUID usuarioId) {
        this.usuarioId = usuarioId;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = "username-index")
    @DynamoDbAttribute("username")
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

    public Integer getTokenVersion() {
        return tokenVersion;
    }

    public void setTokenVersion(Integer tokenVersion) {
        this.tokenVersion = tokenVersion;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "usuarioId=" + usuarioId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
