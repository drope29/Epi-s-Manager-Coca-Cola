package com.epis.services;

import com.epis.entities.Funcionario;
import com.epis.repositories.FuncionarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public Funcionario insert(Funcionario funcionario) { return repository.save(funcionario); }

    public Funcionario update(Long id, Funcionario funcionario) {

        Funcionario entity = getById(id);

        Optional.ofNullable(funcionario.getNome()).ifPresent(entity::setNome);
        Optional.ofNullable(funcionario.getRE()).ifPresent(entity::setRE);
        Optional.ofNullable(funcionario.getFuncao()).ifPresent(entity::setFuncao);
        Optional.ofNullable(funcionario.getUnidade()).ifPresent(entity::setUnidade);
        Optional.ofNullable(funcionario.getTurno()).ifPresent(entity::setTurno);
        Optional.ofNullable(funcionario.getGenero()).ifPresent(entity::setGenero);

        return repository.save(entity);

    }

    public void delete(Long id) {

        Funcionario funcionario = getById(id);

        repository.delete(funcionario);

    }

}
