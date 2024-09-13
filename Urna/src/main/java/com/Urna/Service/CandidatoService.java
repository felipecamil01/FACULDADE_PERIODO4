package com.Urna.Service;

import com.Urna.Entity.Candidato;
import com.Urna.Repository.CandidatoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CandidatoService {

    private final CandidatoRepository candidatoRepository;

    public CandidatoService(CandidatoRepository candidatoRepository) {
        this.candidatoRepository = candidatoRepository;
    }
    public List<Candidato> getCandidatosPrefeitoAtivos() {
        return candidatoRepository.findCandidatosPrefeitoAtivos();
    }
    public List<Candidato> getCandidatosVereadorAtivos() {
        return candidatoRepository.findCandidatosVereadorAtivos();
    }
}