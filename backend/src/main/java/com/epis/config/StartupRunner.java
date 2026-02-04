package com.epis.config;

import com.epis.dtos.funcionario.FuncionarioCreateDto;
import com.epis.dtos.usuario.UsuarioCreateDto;
import com.epis.entities.Funcao;
import com.epis.entities.Funcionario;
import com.epis.services.EpiService;
import com.epis.services.FuncaoService;
import com.epis.services.FuncionarioService;
import com.epis.services.UsuarioService;
import com.epis.utils.UploadFiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Order(2)
public class StartupRunner implements CommandLineRunner {

    @Autowired
    private FuncaoService funcaoService;

    @Autowired
    private FuncionarioService funcionarioService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EpiService epiService;

    @Autowired
    private UploadFiles uploadFiles;

    @Override
    public void run(String... args) throws Exception {

        startupAdmin();

    }

    private void startupAdmin() {

        System.out.println("Verificando usuário admin...");

        Funcao funcaoAdmin = funcaoService
                .findFuncaoByNome("admin")
                .orElseGet(() -> {
                    System.out.println("Criando função admin...");
                    return funcaoService.insert(new Funcao("admin"));
                });

        boolean usuarioExiste = usuarioService.verifyByLogin("admin");

        if (usuarioExiste) {
            System.out.println("Usuário admin já existe.");
            return;
        }

        System.out.println("Criando funcionário admin...");

        Funcionario funcionario = funcionarioService.insert(
                new FuncionarioCreateDto(
                        "000",
                        "admin",
                        funcaoAdmin.getId(),
                        "Blumenau",
                        "Manha",
                        "Masculino",
                        new Date(),
                        "admin"
                )
        );

        System.out.println("Criando usuário admin...");

        usuarioService.insert(
                new UsuarioCreateDto(
                        "admin",
                        "admin",
                        funcionario.getFuncionarioId()
                )
        );

        startupFuncionariosFuncoesEpis();

        System.out.println("Sistema iniciado com sucesso!");

    }

    private void startupFuncionariosFuncoesEpis() {

        System.out.println("Importando Funcionarios, Funcoes e Epi's da planilha...");

        funcionarioService.uploadFuncionarios(uploadFiles.lerFuncionarios());
        epiService.uploadEpis(uploadFiles.lerEpis());

    }

}
