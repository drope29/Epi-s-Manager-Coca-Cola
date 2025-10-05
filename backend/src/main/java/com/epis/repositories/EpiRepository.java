package com.epis.repositories;

import com.epis.entities.Epi;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EpiRepository extends JpaRepository<Epi, Long> {
}
