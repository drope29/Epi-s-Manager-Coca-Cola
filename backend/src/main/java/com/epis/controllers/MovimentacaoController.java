package com.epis.controllers;

import com.epis.dtos.movimentacao.MovimentacaoCreateDto;
import com.epis.dtos.movimentacao.MovimentacaoResponseDto;
import com.epis.dtos.movimentacao.MovimentacaoUpdateDto;
import com.epis.entities.Movimentacao;
import com.epis.mapper.MovimentacaoMapper;
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

    @Autowired
    private MovimentacaoMapper mapper;

    @GetMapping("/")
    public ResponseEntity<List<MovimentacaoResponseDto>> getAll() {

        List<Movimentacao> movimentacoes = service.getAll();

        return ResponseEntity.ok(mapper.toMovimentacaoResponseDtoList(movimentacoes));

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEpiById(@PathVariable UUID id) {

        Movimentacao movimentacao = service.getById(id);

        return ResponseEntity.ok(mapper.toMovimentacaoResponseDto(movimentacao));

    }


    @PostMapping("/")
    public ResponseEntity<MovimentacaoResponseDto> insertMovimentacao(@RequestBody MovimentacaoCreateDto dto) {

        Movimentacao movimentacao = service.insert(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toMovimentacaoResponseDto(movimentacao));

    }

    @PutMapping("/{id}")
    public ResponseEntity<MovimentacaoResponseDto> updateMovimentacao(@PathVariable UUID id, @RequestBody MovimentacaoUpdateDto dto) {

        Movimentacao movimentacaoUpd = service.update(id, dto);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(mapper.toMovimentacaoResponseDto(movimentacaoUpd));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovimentacao(@PathVariable UUID id) {

        service.delete(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }
}
