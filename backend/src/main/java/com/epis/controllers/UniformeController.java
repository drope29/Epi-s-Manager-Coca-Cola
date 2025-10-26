package com.epis.controllers;

import com.epis.dtos.UniformeCreateDto;
import com.epis.dtos.UniformePorFuncaoDto;
import com.epis.dtos.UniformeUpdateDto;
import com.epis.entities.Uniforme;
import com.epis.services.UniformeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/uniformes")
public class UniformeController {

   /* @Autowired
    private UniformeService service;

    @GetMapping("/")
    public ResponseEntity<List<Uniforme>> getAllUniformes(){

        List<Uniforme> uniformes = service.getAll();

        return ResponseEntity.ok().body(uniformes);

    }

    @GetMapping("/{id}")
    public ResponseEntity<Uniforme> getById(@PathVariable Long id){

        Uniforme uniforme = service.getById(id);

        return ResponseEntity.ok().body(uniforme);

    }


    @PostMapping("/")
    public ResponseEntity<Uniforme> insertUniforme(@RequestBody UniformeCreateDto dto) {

        Uniforme uniforme = service.insert(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(uniforme);

    }

    @PutMapping("/{id}")
    public ResponseEntity<Uniforme> updateUniforme(@PathVariable Long id, @RequestBody UniformeUpdateDto dto) {

        Uniforme uniforme = service.update(id, dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(uniforme);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUniforme(@PathVariable Long id){

        service.delete(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    @GetMapping("/buscaUniforme/{funcao_id}")
    public ResponseEntity<UniformePorFuncaoDto> getUniformesPorFuncao(@PathVariable("funcao_id") Long funcaoId) {

        UniformePorFuncaoDto dto = service.getUniformesPorFuncao(funcaoId);

        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();

    }*/
}
