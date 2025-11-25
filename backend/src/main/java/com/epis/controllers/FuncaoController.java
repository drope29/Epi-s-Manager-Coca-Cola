package com.epis.controllers;

import com.epis.entities.Funcao;
import com.epis.services.FuncaoService;
import com.epis.utils.UploadFiles;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/funcoes")
public class FuncaoController {

    @Autowired
    private FuncaoService service;

    @GetMapping("/getAllFuncoes")
    @PreAuthorize("hasRole('ESTAGIARIO') or hasRole('CHEFE')")
    public ResponseEntity<List<Funcao>> getAllFuncoes() {

        List<Funcao> funcoes = service.getAll();

        return ResponseEntity.ok(funcoes);

    }

    @GetMapping("/{id}")
    public ResponseEntity<Funcao> getFuncaoById(@PathVariable UUID id) {

        Funcao funcao = service.getById(id);

        return ResponseEntity.ok(funcao);

    }

    @PostMapping("/")
    public ResponseEntity<Funcao> insertFuncao(@Valid @RequestBody Funcao funcaoBody) {

        service.insert(funcaoBody);

        return ResponseEntity.status(HttpStatus.CREATED).body(funcaoBody);

    }

    @PutMapping("/{id}")
    public ResponseEntity<Funcao> updateFuncao(@PathVariable UUID id, @RequestBody Funcao funcaoBody) {

        var funcaoUpd = service.update(id, funcaoBody);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(funcaoUpd);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFuncao(@PathVariable UUID id) {

        service.delete(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }
    
}
