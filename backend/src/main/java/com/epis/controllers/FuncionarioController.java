package com.epis.controllers;

import com.epis.dtos.funcionario.FuncionarioCreateDto;
import com.epis.dtos.funcionario.FuncionarioResponseDto;
import com.epis.dtos.funcionario.FuncionarioUpdateDto;
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
import java.util.UUID;

@RestController
@RequestMapping("api/funcionarios")
public class FuncionarioController {

    @Autowired
    private UploadFiles uploadFiles;

    @Autowired
    private FuncionarioService service;

    @Autowired
    private FuncionarioMapper mapper;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFuncionarios() {

        String mensagemRetorno = "Funcionários Importados com Sucesso";

        try {
            service.uploadFuncionarios(uploadFiles.lerFuncionarios());
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
    public ResponseEntity<FuncionarioResponseDto> getFuncionarioById(@PathVariable UUID id) {

        Funcionario funcionario = service.getById(id);

        return ResponseEntity.ok(mapper.toFuncionarioResponseDto(funcionario));

    }

    @PostMapping("/")
    public ResponseEntity<FuncionarioResponseDto> insertFuncionario(@Valid @RequestBody FuncionarioCreateDto dto) {

        Funcionario funcionario = service.insert(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toFuncionarioResponseDto(funcionario));

    }

    @PutMapping("/{id}")
    public ResponseEntity<FuncionarioResponseDto> updateFuncionario(@PathVariable UUID id, @RequestBody FuncionarioUpdateDto dto) {

        Funcionario funcionarioUpd = service.update(id, dto);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(mapper.toFuncionarioResponseDto(funcionarioUpd));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFuncionario(@PathVariable UUID id) {

        service.delete(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> deleteFuncionario() {

        service.clearFuncionarioDatabase();

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

}
