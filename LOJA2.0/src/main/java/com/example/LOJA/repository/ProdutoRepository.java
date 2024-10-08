package com.example.LOJA.repository;

import com.example.LOJA.Entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    List<Produto> findByNomeContaining(String nome);

    List<Produto> findByMarcaContaining(String marca);

    @Query("SELECT p FROM Produto p WHERE p.valor >= :precoMinimo AND p.valor <= :precoMaximo")
    List<Produto> findByFaixaDePreco(@Param("precoMinimo") BigDecimal precoMinimo, @Param("precoMaximo") BigDecimal precoMaximo);
}
