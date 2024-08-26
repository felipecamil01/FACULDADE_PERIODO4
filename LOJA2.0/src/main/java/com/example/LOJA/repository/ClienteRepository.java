package com.example.LOJA.repository;

import com.example.LOJA.Entity.Cliente;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

 List<Cliente> findByCpf(String cpf);


 List<Cliente> findByNomeContaining(String nome);

@Query("SELECT c FROM Cliente c WHERE c.idade>=:idadeMinina")
 List<Cliente> findByIdadeMinina(@Param("idadeMinina") Integer idadeMinina);

}
