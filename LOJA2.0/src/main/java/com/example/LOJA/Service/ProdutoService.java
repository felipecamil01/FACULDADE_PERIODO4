package com.example.LOJA.Service;

import com.example.LOJA.Entity.Produto;
import com.example.LOJA.repository.ProdutoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
@Service
public class ProdutoService {


    @Autowired
    private ProdutoRepository produtoRepository;

    public Produto save(Produto produto) {

        return produtoRepository.save(produto);
    }

    public List<Produto> findAll() {
        return produtoRepository.findAll();
    }

    public Optional<Produto> findById(Long id) {
        return produtoRepository.findById(id);
    }


        public Produto atualizarProduto (Long id, Produto produtoAtualizado){
            Optional<Produto> produtoOptional = produtoRepository.findById(id);

            if (produtoOptional.isPresent()) {
                Produto produtoExistente = produtoOptional.get();

                produtoExistente.setNome(produtoAtualizado.getNome());
                produtoExistente.setMarca(produtoAtualizado.getMarca());
                produtoExistente.setValor(produtoAtualizado.getValor());

                return produtoRepository.save(produtoExistente);
            } else {
                throw new EntityNotFoundException("Produto não encontrado com o ID: " + id);
            }
        }

    public void deleteById (Long id){

        produtoRepository.deleteById(id);
    }
    }