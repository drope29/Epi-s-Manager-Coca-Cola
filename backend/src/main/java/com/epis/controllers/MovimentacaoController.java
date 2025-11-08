package com.epis.controllers;

import com.epis.dtos.MovimentacaoCreateDto;
import com.epis.dtos.MovimentacaoUpdateDto;
import com.epis.entities.Movimentacao;
import com.epis.services.MovimentacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/movimentacao")
public class MovimentacaoController {

    @Autowired
    private MovimentacaoService service;

    @GetMapping("/")
    public ResponseEntity<List<Movimentacao>> getAll() {

        List<Movimentacao> movimentacoes = service.getAll();

        return ResponseEntity.ok(movimentacoes);

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEpiById(@PathVariable UUID id) {

        Movimentacao movimentacao = service.getById(id);

        return ResponseEntity.ok(movimentacao);

    }


    @PostMapping("/")
    public ResponseEntity<Movimentacao> insertMovimentacao(@RequestBody MovimentacaoCreateDto dto) {

        Movimentacao movimentecao = service.insert(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(movimentecao);

    }

    @PutMapping("/{id}")
    public ResponseEntity<Movimentacao> updateMovimentacao(@PathVariable UUID id, @RequestBody MovimentacaoUpdateDto dto) {

        Movimentacao movimentacaoUpd = service.update(id, dto);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(movimentacaoUpd);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovimentacao(@PathVariable UUID id) {

        service.delete(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }
}
