package com.epis.entities;

import com.epis.enums.GeneroEnum;
import com.epis.enums.TurnoEnum;
import com.epis.utils.DateAttributeConverter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.UUID;
import java.util.Date;

@DynamoDbBean
public class Funcionario {


    private UUID funcionarioId;
    private String RE;
    private String nome;
    private UUID funcaoId;
    private String unidade;
    private TurnoEnum turno;
    private GeneroEnum genero;
    private Date dataAdmissao;
    private String setor;

    public String cadastroAtivo;

    public Funcionario(){}

    public Funcionario(String RE, String nome, UUID funcaoId) {
        this.funcionarioId = UUID.randomUUID();
        this.RE = RE;
        this.nome = nome;
        this.funcaoId = funcaoId;
        this.cadastroAtivo = "1";
    }

    public Funcionario(UUID funcionarioId, String RE, String nome, UUID funcaoId, String unidade, TurnoEnum turno, GeneroEnum genero, Date dataAdmissao, String setor) {
        this.funcionarioId = UUID.randomUUID();
        this.RE = RE;
        this.nome = nome;
        this.funcaoId = funcaoId;
        this.unidade = unidade;
        this.turno = turno;
        this.genero = genero;
        this.dataAdmissao = dataAdmissao;
        this.setor = setor;
        this.cadastroAtivo = "1";
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

    @DynamoDbSecondaryPartitionKey(indexNames = "funcionario-nome-index")
    @DynamoDbAttribute("nome")
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public UUID getFuncaoId() { return funcaoId; }

    public void setFuncaoId(UUID funcaoId) { this.funcaoId = funcaoId; }

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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @DynamoDbConvertedBy(DateAttributeConverter.class)
    public Date getDataAdmissao() { return dataAdmissao; }

    public void setDataAdmissao(Date dataAdmissao) { this.dataAdmissao = dataAdmissao; }

    public String getSetor() { return setor; }

    public void setSetor(String setor) { this.setor = setor; }

    @DynamoDbSecondaryPartitionKey(indexNames = "funcionario-ativo-index")
    @DynamoDbAttribute("cadastroAtivo")
    public String getCadastroAtivo() {
        return cadastroAtivo;
    }

    public void setCadastroAtivo(String cadastroAtivo) {
        this.cadastroAtivo = cadastroAtivo;
    }

    @Override
    public String toString() {
        return "Funcionario{" +
                "funcionarioId=" + funcionarioId +
                ", RE='" + RE + '\'' +
                ", nome='" + nome + '\'' +
                ", funcao='" + funcaoId + '\'' +
                ", unidade='" + unidade + '\'' +
                ", turno='" + turno + '\'' +
                ", genero='" + genero + '\'' +
                '}';
    }
}
