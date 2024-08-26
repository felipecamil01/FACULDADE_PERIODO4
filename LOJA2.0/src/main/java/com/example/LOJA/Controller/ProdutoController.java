package com.example.LOJA.Controller;

import com.example.LOJA.Entity.Produto;
import com.example.LOJA.Service.ProdutoService;
import com.example.LOJA.repository.ProdutoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
    @RestController
    @RequestMapping("/produtos")
    public class ProdutoController {

        @Autowired
        private ProdutoService produtoService;
        @Autowired
        private ProdutoRepository produtoRepository;

        @GetMapping
        public ResponseEntity<List<Produto>> getAllProdutos() {
            List<Produto> produtos = produtoService.findAll();
            return ResponseEntity.ok(produtos);
        }

        @GetMapping("/{id}")
        public ResponseEntity<Produto> getProdutoById(@PathVariable Long id) {
            Optional<Produto> produto = produtoService.findById(id);
            return produto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        }

        @PostMapping
        public ResponseEntity<Produto> createProduto(@RequestBody Produto produto) {
            Produto novoProduto = produtoService.save(produto);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoProduto);
        }

        @PutMapping("/{id}")
        public ResponseEntity<Produto> updateProduto(@PathVariable Long id, @RequestBody Produto produtoAtualizado) {
            Optional<Produto> produtoExistente = produtoService.findById(id);

            if (produtoExistente.isPresent()) {
                Produto produto = produtoExistente.get();
                produto.setNome(produtoAtualizado.getNome());
                produto.setValor(produtoAtualizado.getValor());
                produto.setMarca(produtoAtualizado.getMarca());
                Produto produtoAtualizadoSalvo = produtoService.save(produto);
                return ResponseEntity.ok(produtoAtualizadoSalvo);
            } else {
                return ResponseEntity.notFound().build();
            }
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteProduto(@PathVariable Long id) {
            try {
                if (!produtoRepository.existsById(id)) {
                    throw new EntityNotFoundException("Produto com ID " + id + " não encontrado.");
                }
                produtoRepository.deleteById(id);
            } catch (EntityNotFoundException e) {
                throw e; // Re-throw to be caught by the controller
            } catch (Exception e) {
                throw new RuntimeException("Erro ao excluir Produto.", e);
            }
            return ResponseEntity.noContent().build();
        }

        // Busca produtos por nome contendo a string fornecida
        @GetMapping("/buscaNome")
        public ResponseEntity<List<Produto>> findByNomeContaining(@RequestParam String nome) {
            List<Produto> produtos = produtoRepository.findByNomeContaining(nome);
            if (produtos.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(produtos);
        }
        @GetMapping("/marca")
        public ResponseEntity<List<Produto>> getProdutosByMarca(@RequestParam String marca) {
            List<Produto> produtos = produtoRepository.findByMarcaContaining(marca);
            if (produtos.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(produtos);
        }

        // Busca produtos por faixa de preço
        @GetMapping("/preco")
        public ResponseEntity<List<Produto>> ProdutosByFaixaDePreco(
                @RequestParam BigDecimal precoMinimo,
                @RequestParam BigDecimal precoMaximo) {
            List<Produto> produtos = produtoRepository.findByFaixaDePreco(precoMinimo, precoMaximo);
            if (produtos.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(produtos);
        }
    }
