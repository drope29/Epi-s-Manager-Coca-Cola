package com.epis.controllers;

import com.epis.dtos.EpiCreateDto;
import com.epis.dtos.EpiUpdateDto;
import com.epis.entities.Epi;
import com.epis.mapper.EpiMapper;
import com.epis.services.EpiService;
import com.epis.utils.UploadFiles;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id}")
    public ResponseEntity<?> getEpiById(@PathVariable Long id) {

        Epi epi = service.getById(id);

        return ResponseEntity.ok(epi);

    }

    @PostMapping("/")
    public ResponseEntity<Epi> insertEpi(@Valid @RequestBody EpiCreateDto dto) {

        Epi epi = service.insert(EpiMapper.toEpi(dto));

        return ResponseEntity.status(HttpStatus.CREATED).body(epi);

    }

    @PutMapping("/{id}")
    public ResponseEntity<Epi> updateEpi(@PathVariable Long id, @RequestBody EpiUpdateDto dto) {

        Epi epiUpd = service.update(id, EpiMapper.toEpi(dto));

        return ResponseEntity.status(HttpStatus.CREATED).body(epiUpd);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEpi(@PathVariable Long id) {

        service.delete(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Epi Deletado com Sucesso");

    }

}
