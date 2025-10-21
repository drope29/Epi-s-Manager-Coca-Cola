package com.epis.services;

import com.epis.dtos.FuncionarioCreateDto;
import com.epis.dtos.FuncionarioUpdateDto;
import com.epis.entities.Funcionario;
import com.epis.mapper.FuncionarioMapper;
import com.epis.repositories.FuncionarioRepository;
import com.epis.services.exception.FuncionarioNaoEncontradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FuncionarioService {

    @Autowired
    private FuncionarioRepository repository;

    @Autowired
    private FuncionarioMapper mapper;

    public void uploadFuncionarios(List<Funcionario> funcionarios){
        repository.saveAll(funcionarios);
    }

    public List<Funcionario> getAll() {
        return repository.findAll();
    }

    public Funcionario getById(Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new FuncionarioNaoEncontradoException("Funcionário não encontrado com id " + id));

    }

    public Funcionario insert(FuncionarioCreateDto dto) {

        Funcionario funcionario = mapper.toFuncionario(dto);

        return repository.save(funcionario);

    }

    public Funcionario update(Long id, FuncionarioUpdateDto dto) {

        Funcionario entity = getById(id);

        mapper.toFuncionario(dto, entity);

        return repository.save(entity);

    }

    public void delete(Long id) {

        Funcionario funcionario = getById(id);

        repository.delete(funcionario);

    }

}
