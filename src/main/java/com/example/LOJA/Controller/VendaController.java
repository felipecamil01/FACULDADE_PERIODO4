package com.example.LOJA.Controller;

import com.example.LOJA.Entity.Venda;
import com.example.LOJA.Service.VendaService;
import com.example.LOJA.repository.VendaRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Id;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/vendas")
public class VendaController {
    @Autowired
    private VendaService vendaService;
    @Autowired
    private VendaRepository vendaRepository;

    @PostMapping
    public ResponseEntity<Venda> criarVenda( @RequestBody Venda venda) {
        Venda novaVenda = vendaService.salvarVenda(venda);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaVenda);
    }

    @GetMapping("/listar")
    public List<Venda> listarVendas() {
        return vendaService.buscarTodasAsVendas();
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<Venda> buscarVendaPorId(@PathVariable Long id) {
        Optional<Venda> venda = vendaService.buscarVendaPorId(id);
        return venda.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("atualizar/{id}")
    public ResponseEntity<Venda> atualizarVenda(@PathVariable Long id, @Valid @RequestBody Venda vendaAtualizada) {
        try {
            Venda venda = vendaService.atualizarVenda(id, vendaAtualizada);
            return ResponseEntity.ok(venda);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("excluir/{id}")
    public ResponseEntity<Void> excluirVenda(@PathVariable Long id) {
        vendaService.excluirVenda(id);
        return ResponseEntity.noContent().build();
    }
    // Filtro por nome de cliente
    @GetMapping("/cliente")
    public ResponseEntity<List<Venda>> buscarPorCliente(@RequestParam long clienteId) {
        List<Venda> vendas = vendaRepository.findByClienteId(clienteId);
        return ResponseEntity.ok(vendas);
    }

    // Filtro por data de venda
    @GetMapping("/data")
    public ResponseEntity<List<Venda>> buscarPorDataVenda(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataVenda) {
        List<Venda> vendas = vendaRepository.findByDataVenda(dataVenda);
        return ResponseEntity.ok(vendas);
    }

    // Filtro por valor mínimo
    @GetMapping("/valor-minimo")
    public ResponseEntity<List<Venda>> buscarPorValorMinimo(@RequestParam BigDecimal valorMinimo) {
        List<Venda> vendas = vendaRepository.findByValorMinimo(valorMinimo);
        return ResponseEntity.ok(vendas);
    }
}
