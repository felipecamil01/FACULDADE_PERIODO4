package com.Urna.Repository;

import com.Urna.Entity.Candidato;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidatoRepository extends JpaRepository<Candidato, Long> {

    // Lista candidatos a prefeito ativos
    @Query("SELECT c FROM Candidato c WHERE c.funcao = 1 AND c.status = 'ATIVO'")
    List<Candidato> findCandidatosPrefeitoAtivos();

    // Lista candidatos a vereador ativos
    @Query("SELECT c FROM Candidato c WHERE c.funcao = 2 AND c.status = 'ATIVO'")
    List<Candidato> findCandidatosVereadorAtivos();

    // Conta o voto
    @Query("SELECT COUNT(v) FROM Voto v WHERE v.candidatoPrefeito.id = :id OR v.candidatoVereador.id = :id")
    Integer contarVotosPorCandidato(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("UPDATE Candidato c SET c.status = 'ATIVO' WHERE c.id = :id")
    void reativar(@Param("id") Long id);
    
    @Modifying
    @Transactional
    @Query("UPDATE Candidato c SET c.status = 'INATIVO' WHERE c.id = :id")
    void inativar(@Param("id") Long id);

    // Recupera todos os candidatos com status ATIVO
    @Query("SELECT c FROM Candidato c WHERE c.status = 'ATIVO'")
    List<Candidato> findAllAtivos();
}
