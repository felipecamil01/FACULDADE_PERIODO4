package com.Urna.Repository;

import com.Urna.Entity.Candidato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidatoRepository extends JpaRepository<Candidato, Long> {

    @Query("SELECT c FROM Candidato c WHERE c.funcao = 1 AND c.status = 'ATIVO'")
    List<Candidato> findCandidatosPrefeitoAtivos();

    @Query("SELECT c FROM Candidato c WHERE c.funcao = 2 AND c.status = 'ATIVO'")
    List<Candidato> findCandidatosVereadorAtivos();

    @Query("SELECT COUNT(v) FROM Voto v WHERE v.candidatoPrefeito.id = :id OR v.candidatoVereador.id = :id")
    Integer contarVotosPorCandidato(@Param("id") Long id);

    @Query("SELECT c FROM Candidato c WHERE c.status = 'ATIVO'")
    List<Candidato> findAllAtivos();
}
