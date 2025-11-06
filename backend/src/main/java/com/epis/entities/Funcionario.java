package com.epis.entities;

import com.epis.enums.GeneroEnum;
import com.epis.enums.TurnoEnum;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.util.UUID;

@DynamoDbBean
public class Funcionario {


    private UUID funcionarioId;
    private String RE;
    private String nome;
    private Funcao funcao;
    private String unidade;
    private TurnoEnum turno;
    private GeneroEnum genero;

    public Funcionario(){}

    public Funcionario(String RE, String nome, Funcao funcao) {
        this.funcionarioId = UUID.randomUUID();
        this.RE = RE;
        this.nome = nome;
        this.funcao = funcao;
    }

    public Funcionario(String RE, String nome, Funcao funcao, String unidade, TurnoEnum turno, GeneroEnum genero) {
        this.funcionarioId = UUID.randomUUID();
        this.RE = RE;
        this.nome = nome;
        this.funcao = funcao;
        this.unidade = unidade;
        this.turno = turno;
        this.genero = genero;
    }

    @DynamoDbPartitionKey
    @DynamoDbAttribute("funcionarioId")
    public UUID getFuncionarioId() {
        return funcionarioId;
    }

    public void setFuncionarioId(UUID funcionarioId) {
        this.funcionarioId = funcionarioId;
    }

    public String getRE() {
        return RE;
    }

    public void setRE(String RE) {
        this.RE = RE;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Funcao getFuncao() {
        return funcao;
    }

    public void setFuncao(Funcao funcao) {
        this.funcao = funcao;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public TurnoEnum getTurno() {
        return turno;
    }

    public void setTurno(TurnoEnum turno) {
        this.turno = turno;
    }

    public GeneroEnum getGenero() {
        return genero;
    }

    public void setGenero(GeneroEnum genero) {
        this.genero = genero;
    }

    @Override
    public String toString() {
        return "Funcionario{" +
                "funcionarioId=" + funcionarioId +
                ", RE='" + RE + '\'' +
                ", nome='" + nome + '\'' +
                ", funcao='" + funcao + '\'' +
                ", unidade='" + unidade + '\'' +
                ", turno='" + turno + '\'' +
                ", genero='" + genero + '\'' +
                '}';
    }
}
