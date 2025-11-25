package com.epis.entities;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

import java.util.UUID;

@DynamoDbBean
public class Funcao {

    private UUID id;
    private String nome;

    public Funcao(){}

    public Funcao(String nome) {
        this.id = UUID.randomUUID();
        this.nome = nome;
    }

    public Funcao(UUID id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    @DynamoDbPartitionKey
    @DynamoDbAttribute("funcaoId")
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "Funcao{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                '}';
    }

}
