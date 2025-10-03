package com.epis.controllers;

import com.epis.dtos.FuncionarioCreateDto;
import com.epis.dtos.FuncionarioUpdateDto;
import com.epis.entities.Funcionario;
import com.epis.mapper.FuncionarioMapper;
import com.epis.services.FuncionarioService;
import com.epis.utils.UploadFiles;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/funcionarios")
public class FuncionarioController {

    @Autowired
    private FuncionarioService service;

    @PostMapping("/upload-funcionarios")
    public ResponseEntity<String> uploadFuncionarios() {

        String mensagemRetorno = "Funcionários Importados com Sucesso";

        try {
            service.uploadFuncionarios(UploadFiles.lerFuncionarios());
        } catch (Exception e) {
            mensagemRetorno = "Ocorreu um erro ao importar usuários, erro: " + e.getMessage();
        }

        return ResponseEntity.ok(mensagemRetorno);

    }

    @GetMapping("/")
    public ResponseEntity<List<Funcionario>> getAllFuncionarios() {

        List<Funcionario> funcionarios = service.getAll();

        return ResponseEntity.ok(funcionarios);

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFuncionarioById(@PathVariable Long id) {

        Funcionario funcionario = service.getById(id);
        return ResponseEntity.ok(funcionario);

    }

    @PostMapping("/")
    public ResponseEntity<Funcionario> insertFuncionario(@Valid @RequestBody FuncionarioCreateDto dto) {

        Funcionario funcionario = FuncionarioMapper.toFuncionario(dto);
        service.insert(funcionario);

        return ResponseEntity.status(HttpStatus.CREATED).body(funcionario);

    }

    @PutMapping("/{id}")
    public ResponseEntity<Funcionario> updateFuncionario(@PathVariable Long id, @RequestBody FuncionarioUpdateDto dto) {

        Funcionario funcionario = FuncionarioMapper.toFuncionario(dto);
        Funcionario funcionarioUpd = service.update(id, funcionario);

        return ResponseEntity.status(HttpStatus.CREATED).body(funcionarioUpd);

    }

}
