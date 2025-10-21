package com.epis.repositories;

import com.epis.entities.Uniforme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UniformeRepoitory extends JpaRepository<Uniforme, Long> {

    @Query("SELECT u FROM Uniforme u " +
            "JOIN FETCH u.epi " +
            "WHERE u.funcao.id = :funcaoId")
    List<Uniforme> buscarEpisPorFuncao(@Param("funcaoId") Long funcaoId);
}
