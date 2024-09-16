package com.Urna.Controller;

import com.Urna.Entity.Eleitor;
import com.Urna.Service.EleitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/eleitor")
public class EleitorController {

    @Autowired
    private EleitorService eleitorService;

    @PostMapping("/save")
    public ResponseEntity<Eleitor> save(@RequestBody Eleitor eleitor) {
        Eleitor eleitorSalvo = eleitorService.save(eleitor);
        return new ResponseEntity<>(eleitorSalvo, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Eleitor> update(@PathVariable Long id, @RequestBody Eleitor eleitorAtualizado) {
        try {
            Eleitor eleitor = eleitorService.update(id, eleitorAtualizado);
            return new ResponseEntity<>(eleitor, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/inativar/{id}")
    public ResponseEntity<String> inativar(@PathVariable Long id) {
    	try {
            String mensagem = eleitorService.inativar(id);
            return new ResponseEntity<>(mensagem, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @DeleteMapping("/reativar/{id}")
    public ResponseEntity<String> reativar(@PathVariable Long id) {
    	try {
            String mensagem = eleitorService.reativar(id);
            return new ResponseEntity<>(mensagem, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<Eleitor>> findAll() {
        List<Eleitor> eleitores = eleitorService.findAll();
        return new ResponseEntity<>(eleitores, HttpStatus.OK);
    }
    
    @GetMapping("/findAllAptos")
    public ResponseEntity<List<Eleitor>> findAllAptos() {
        List<Eleitor> eleitores = eleitorService.findAllAptos();
        return new ResponseEntity<>(eleitores, HttpStatus.OK);
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<Eleitor> findById(@PathVariable Long id) {
        Optional<Eleitor> eleitor = eleitorService.findById(id);
        return eleitor.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
