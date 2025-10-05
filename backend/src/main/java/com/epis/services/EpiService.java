package com.epis.services;

import com.epis.entities.Epi;
import com.epis.entities.Funcionario;
import com.epis.repositories.EpiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EpiService {

    @Autowired
    private EpiRepository repository;

    public void uploadEpi(List<Epi> epi){
        repository.saveAll(epi);
    }

}
