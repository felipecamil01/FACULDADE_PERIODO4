package com.example.LOJA.repository;

import com.example.LOJA.Entity.Cliente;
import com.example.LOJA.Entity.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {


    List<Funcionario>findByMatricula(String matricula);

    List<Funcionario>findByNomeContaining(String nome);

    @Query("SELECT f FROM Funcionario f WHERE f.idade>=:idadeMinina")
    List<Funcionario> findByIdadeMinina(@Param("idadeMinina") Integer idadeMinina);

}