package com.epis.services;

import com.epis.entities.Funcionario;
import com.epis.repositories.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FuncionarioService {

    @Autowired
    private FuncionarioRepository repository;

    public List<Funcionario> uploadFuncionarios(List<Funcionario> funcionarios){
        return repository.saveAll(funcionarios);
    }

}
