package com.Urna.Controller;

import com.Urna.Entity.Apuracao;
import com.Urna.Entity.Voto;
import com.Urna.Service.VotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/voto")
public class VotoController {

    @Autowired
    private VotoService votoService;

    @PostMapping("/votar/{eleitorId}")
    public ResponseEntity<String> votar(@PathVariable Long eleitorId, @RequestBody Voto voto) {
        String hash = votoService.votar(eleitorId, voto);
        return ResponseEntity.ok(hash);
    }

    @GetMapping("/apurar")
    public ResponseEntity<Apuracao> realizarApuracao() {
        Apuracao apuracao = votoService.realizarApuracao();
        return ResponseEntity.ok(apuracao);
    }
}

