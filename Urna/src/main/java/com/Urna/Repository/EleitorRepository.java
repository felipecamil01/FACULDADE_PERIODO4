package com.Urna.Repository;

import com.Urna.Entity.Eleitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EleitorRepository extends JpaRepository<Eleitor, Long> {

    @Query("SELECT e FROM Eleitor e WHERE e.status = 'APTO'")
    List<Eleitor> findAllAptos();
}
