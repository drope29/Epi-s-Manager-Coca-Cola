package com.epis.controllers;

import com.epis.entities.Epi;
import com.epis.entities.Funcionario;
import com.epis.services.EpiService;
import com.epis.utils.UploadFiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/epis")
public class EpiController {

    @Autowired
    private EpiService service;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadEpi() {
        String mensagemRetorno = "Epis Importados com Sucesso";

        try {
            service.uploadEpi(UploadFiles.lerEpis());
        } catch (Exception e) {
            mensagemRetorno = "Ocorreu um erro ao importar epis, erro: " + e.getMessage();
        }

        return ResponseEntity.ok(mensagemRetorno);
    }

    @GetMapping("/")
    public ResponseEntity<List<Epi>> getAllEpis() {

        List<Epi> epis = service.getAll();

        return ResponseEntity.ok(epis);

    }

}
