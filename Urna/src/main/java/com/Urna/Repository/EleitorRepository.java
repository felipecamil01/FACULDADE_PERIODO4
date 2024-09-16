package com.Urna.Repository;

import com.Urna.Entity.Eleitor;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EleitorRepository extends JpaRepository<Eleitor, Long> {

	@Modifying
    @Transactional
    @Query("UPDATE Eleitor e SET e.status = 'INATIVO' WHERE e.id = :id")
    void inativar(@Param("id") Long id);
    
	@Modifying
    @Transactional
    @Query("UPDATE Eleitor e SET e.status = 'APTO' WHERE e.id = :id")
    void reativar(@Param("id") Long id);

    @Query("SELECT e FROM Eleitor e WHERE e.status = 'APTO'")
    List<Eleitor> findAllAptos();
}
