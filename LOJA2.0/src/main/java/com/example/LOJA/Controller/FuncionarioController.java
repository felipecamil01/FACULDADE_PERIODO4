package com.example.LOJA.Controller;

import com.example.LOJA.Entity.Cliente;
import com.example.LOJA.Entity.Funcionario;
import com.example.LOJA.Service.FuncionarioService;
import com.example.LOJA.repository.FuncionarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/funcionario")
public class FuncionarioController {

    @Autowired
    private FuncionarioService funcionarioService;
    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @PostMapping
    public ResponseEntity<Funcionario> criarFuncionario(@RequestBody Funcionario funcionario) {
        Funcionario novoFuncionario = funcionarioService.save(funcionario);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoFuncionario);
    }
    @GetMapping("/listar")
    public List<Funcionario> listar() {
        return  funcionarioService.buscarTodosFuncionario();
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<Funcionario> buscarFuncionarioPorId(@PathVariable Long id) {
        Optional<Funcionario> funcionario = funcionarioService.buscarFuncionarioPorId(id);
        return funcionario.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("atualizar/{id}")
    public ResponseEntity<Funcionario> atualizarFuncionario(@PathVariable Long id, @RequestBody Funcionario funcionarioAtualizado) {
        try {
            Funcionario funcionario =  funcionarioService.atualizarFuncionario(id, funcionarioAtualizado);
            return ResponseEntity.ok(funcionario);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("excluir/{id}")
    public ResponseEntity<Void> excluirFuncionario(@PathVariable Long id) {
        try {
            if (!funcionarioRepository.existsById(id)) {
                throw new EntityNotFoundException("Funcionário com ID " + id + " não encontrado.");
            }
            funcionarioRepository.deleteById(id);
        } catch (EntityNotFoundException e) {
            throw e; // Re-throw to be caught by the controller
        } catch (Exception e) {
            throw new RuntimeException("Erro ao excluir funcionário.", e);
        }
        return ResponseEntity.noContent().build();
    }
    // Filtro por nome
    @GetMapping("/nome")
    public ResponseEntity<List<Funcionario>> buscarPorNome(@RequestParam String nome) {
        List<Funcionario> funcionarios = funcionarioRepository.findByNomeContaining(nome);
        return ResponseEntity.ok(funcionarios);
    }

    // Filtro por matricula
    @GetMapping("/matricula")
    public ResponseEntity<List<Funcionario>> buscarPorMatricula(@RequestParam String matricula) {
        List<Funcionario> funcionarios = funcionarioRepository.findByMatricula(matricula);
        return ResponseEntity.ok(funcionarios);
    }

    // Filtro por idade mínima
    @GetMapping("/idade-minima")
    public ResponseEntity<List<Funcionario>> buscarPorIdadeMinima(@RequestParam int idadeMinima) {
        List<Funcionario> funcionarios = funcionarioRepository.findByIdadeMinina(idadeMinima);
        return ResponseEntity.ok(funcionarios);
    }
}

