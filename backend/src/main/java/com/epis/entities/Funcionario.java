package com.epis.entities;

import com.epis.enums.GeneroEnum;
import com.epis.enums.TurnoEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "tb_funcionarios")
public class Funcionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String RE;

    @Column
    private String nome;

    @Column
    private String funcao;

    @Column
    private String unidade;

    @Column
    @Enumerated(EnumType.STRING)
    private TurnoEnum turno;

    @Column
    @Enumerated(EnumType.STRING)
    private GeneroEnum genero;

    @Column
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date dataAdmissao;

    @Column
    private String setor;

    @OneToMany(mappedBy = "funcionario")
    private List<Movimentacao> FuncionarioMovimentacao = new ArrayList<>();

    public Funcionario(){}

    public Funcionario(String RE, String nome, String funcao) {
        this.RE = RE;
        this.nome = nome;
        this.funcao = funcao;
    }

    public Funcionario(Long id, String RE, String nome, String funcao, String unidade, TurnoEnum turno, GeneroEnum genero, Date dataAdmissao, String setor) {
        this.id = id;
        this.RE = RE;
        this.nome = nome;
        this.funcao = funcao;
        this.unidade = unidade;
        this.turno = turno;
        this.genero = genero;
        this.dataAdmissao = dataAdmissao;
        this.setor = setor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getFuncao() {
        return funcao;
    }

    public void setFuncao(String funcao) {
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

    public Date getDataAdmissao() { return dataAdmissao; }

    public void setDataAdmissao(Date dataAdmissao) { this.dataAdmissao = dataAdmissao; }

    public String getSetor() { return setor; }

    public void setSetor(String setor) { this.setor = setor; }

    @Override
    public String toString() {
        return "Funcionario{" +
                "id=" + id +
                ", RE='" + RE + '\'' +
                ", nome='" + nome + '\'' +
                ", funcao='" + funcao + '\'' +
                ", unidade='" + unidade + '\'' +
                ", turno='" + turno + '\'' +
                ", genero='" + genero + '\'' +
                '}';
    }
}
