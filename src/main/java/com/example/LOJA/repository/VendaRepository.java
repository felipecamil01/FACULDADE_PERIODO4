package com.example.LOJA.repository;

import com.example.LOJA.Entity.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Long> {

   @Query("select v from Venda v Where v.cliente.id=:clienteId")
 List<Venda> findByClienteId(@Param("clienteId") Long clienteId);

   List<Venda> findByDataVenda (LocalDate dataVenda);
@Query("SELECT v FROM Venda v WHERE v.valorTotal>=:valoraMinimo")
   List<Venda> findByValorMinimo(@Param("valorMinimo")BigDecimal valorMinimo);
}
