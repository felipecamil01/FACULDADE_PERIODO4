package com.Urna.Repository;

import com.Urna.Entity.Candidato;
import com.Urna.Entity.StatusCandidato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CandidatoRepository extends JpaRepository<Candidato, Long> {

    // Lista candidatos a prefeito ativos
    @Query("SELECT c FROM Candidato c WHERE c.funcao = 1 AND c.status = 'ATIVO'")
    List<Candidato> findCandidatosPrefeitoAtivos();

    // Lista candidatos a vereador ATV
    @Query("SELECT c FROM Candidato c WHERE c.funcao = 2 AND c.status = 'ATIVO'")
    List<Candidato> findCandidatosVereadorAtivos();

    // Conta o voto
    @Query("SELECT COUNT(v) FROM Voto v WHERE v.candidatoPrefeito.id = :id OR v.candidatoVereador.id = :id")
    Integer contarVotosPorCandidato(@Param("id") Long id);
}
