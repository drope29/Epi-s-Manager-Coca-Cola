package com.epis.services;

import com.epis.entities.Funcionario;
import com.epis.repositories.FuncionarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FuncionarioService {

    @Autowired
    private FuncionarioRepository repository;

    public void uploadFuncionarios(List<Funcionario> funcionarios){
        repository.saveAll(funcionarios);
    }

    public List<Funcionario> getAll() {
        return repository.findAll();
    }

    public Funcionario getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Funcionário não encontrado com id " + id));
    }


    public void insert(Funcionario funcionario) {
        repository.save(funcionario);
    }

    public Funcionario update(Long id, Funcionario funcionario) {
        Funcionario entity = getById(id);

        if (funcionario.getNome() != null) {
            entity.setNome(funcionario.getNome());
        }

        if (funcionario.getRE() != null) {
            entity.setRE(funcionario.getRE());
        }

        if (funcionario.getFuncao() != null) {
            entity.setFuncao(funcionario.getFuncao());
        }

        return repository.save(entity);
    }


}
