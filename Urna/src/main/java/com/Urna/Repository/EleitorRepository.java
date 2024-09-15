package com.Urna.Repository;

import com.Urna.Entity.Eleitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EleitorRepository extends JpaRepository<Eleitor, Long> {

    // Lista apenas eleitores ativos
    @Query("SELECT e FROM Eleitor e WHERE e.status != 'INATIVO'")
    List<Eleitor> findAllAtivos();
}
