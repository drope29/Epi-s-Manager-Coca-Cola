package com.epis.services;

import com.epis.entities.Funcionario;
import com.epis.repositories.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FuncionarioService {

    @Autowired
    private FuncionarioRepository repository;

    public List<Funcionario> uploadFuncionarios(List<Funcionario> funcionarios){
        return repository.saveAll(funcionarios);
    }

    public List<Funcionario> getAll() {
        return repository.findAll();
    }

    public Optional<Funcionario> getById(Long id) {
        return repository.findById(id);
    }



}
