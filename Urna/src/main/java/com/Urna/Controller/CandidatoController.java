package com.Urna.Controller;

import com.Urna.Entity.Candidato;
import com.Urna.Service.CandidatoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/candidato")
public class CandidatoController {

	@Autowired
    private CandidatoService candidatoService;

    @PostMapping("/save")
    public ResponseEntity<Candidato> save(@RequestBody Candidato candidato) {
        Candidato candidatoSalvo = candidatoService.save(candidato);
        return new ResponseEntity<>(candidatoSalvo, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Candidato> update(@PathVariable Long id, @RequestBody Candidato candidatoAtualizado) {
        try {
            Candidato candidato = candidatoService.update(id, candidatoAtualizado);
            return new ResponseEntity<>(candidato, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/inativar/{id}")
    public ResponseEntity<String> inativar(@PathVariable Long id) {
    	try {
            String mensagem = candidatoService.inativar(id);
            return new ResponseEntity<>(mensagem, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @DeleteMapping("/reativar/{id}")
    public ResponseEntity<String> reativar(@PathVariable Long id) {
    	try {
            String mensagem = candidatoService.reativar(id);
            return new ResponseEntity<>(mensagem, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/findAll")
    public ResponseEntity<List<Candidato>> findAll() {
        List<Candidato> candidatos = candidatoService.findAll();
        return new ResponseEntity<>(candidatos, HttpStatus.OK);
    }
    
    @GetMapping("/findAllAtivos")
    public ResponseEntity<List<Candidato>> findAllAtivos() {
        List<Candidato> candidatos = candidatoService.findAllAtivos();
        return new ResponseEntity<>(candidatos, HttpStatus.OK);
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<Candidato> findById(@PathVariable Long id) {
        Optional<Candidato> candidato = candidatoService.findById(id);
        return candidato.map(ResponseEntity::ok)
                        .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
