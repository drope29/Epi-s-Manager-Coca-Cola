package com.epis.controllers;

import com.epis.entities.Funcao;
import com.epis.services.FuncaoService;
import com.epis.utils.UploadFiles;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/funcoes")
public class FuncaoController {

    @Autowired
    private FuncaoService service;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFuncoes() {

        String mensagemRetorno = "Funcoes Importados com Sucesso";

        try {
            service.uploadFuncoes(UploadFiles.lerFuncoes());
        } catch (Exception e) {
            mensagemRetorno = "Ocorreu um erro ao importar funcoes, erro: " + e.getMessage();
        }

        return ResponseEntity.ok(mensagemRetorno);

    }

    @GetMapping("/")
    public ResponseEntity<List<Funcao>> getAllFuncoes() {

        List<Funcao> funcoes = service.getAll();

        return ResponseEntity.ok(funcoes);

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFuncaoById(@PathVariable Long id) {

        Funcao funcao = service.getById(id);

        return ResponseEntity.ok(funcao);

    }

    @PostMapping("/")
    public ResponseEntity<Funcao> insertFuncao(@Valid @RequestBody Funcao funcaoBody) {

        Funcao funcao = service.insert(funcaoBody);

        return ResponseEntity.status(HttpStatus.CREATED).body(funcao);

    }

    @PutMapping("/{id}")
    public ResponseEntity<Funcao> updateFuncao(@PathVariable Long id, @RequestBody Funcao funcaoBody) {

        Funcao funcaoUpd = service.update(id, funcaoBody);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(funcaoUpd);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFuncao(@PathVariable Long id) {

        service.delete(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }
    
}
