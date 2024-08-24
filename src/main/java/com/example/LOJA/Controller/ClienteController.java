package com.example.LOJA.Controller;

import com.example.LOJA.Entity.Cliente;

import com.example.LOJA.Entity.Venda;
import com.example.LOJA.Service.ClienteService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/cliente")
public class ClienteController {
    @Autowired
    private ClienteService clienteService;

    @PostMapping
    public ResponseEntity<Cliente> criarCliente(@RequestBody Cliente cliente) {
        Cliente novoCliente = clienteService.save(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoCliente);
    }
    @GetMapping("/listar")
    public List<Cliente> listar() {
        return  clienteService.buscarTodosCliente();
    }


    @GetMapping("/buscar/{id}")
    public ResponseEntity<Cliente> buscarClientePorId(@PathVariable Long id) {
        Optional<Cliente> cliente = clienteService.clienteBuscarPorId(id);
        return cliente.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("atualizar/{id}")
    public ResponseEntity<Cliente> atualizarCliente(@PathVariable Long id, @Valid @RequestBody Cliente clienteAtualizada) {
        try {
            Cliente cliente = clienteService.atualizarCliente(id, clienteAtualizada);
            return ResponseEntity.ok(cliente);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("excluir/{id}")
    public ResponseEntity<Void> excluirCliente(@PathVariable Long id) {
        clienteService.excluirCliente (id);
        return ResponseEntity.noContent().build();
    }
}