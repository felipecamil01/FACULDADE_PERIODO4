package com.Urna.Service;

import com.Urna.Entity.Candidato;
import com.Urna.Entity.StatusCandidato;
import com.Urna.Repository.CandidatoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CandidatoService {

    @Autowired
    private CandidatoRepository candidatoRepository;

    public Candidato save(Candidato candidato) {
    	candidato.setStatus(StatusCandidato.ATIVO);
        return candidatoRepository.save(candidato);
    }

    public Candidato update(Long id, Candidato candidatoAtualizado) {
        if (candidatoRepository.existsById(id)) {
            candidatoAtualizado.setId(id);
            candidatoAtualizado.setStatus(StatusCandidato.ATIVO);
            return candidatoRepository.save(candidatoAtualizado);
        } else {
            throw new RuntimeException("Candidato não encontrado com ID: " + id);
        }
    }

    public String inativar(Long id) {
        if (candidatoRepository.existsById(id)) {
            candidatoRepository.inativar(id);
            return "Candidato inativado com sucesso";
        } else {
            throw new RuntimeException("Candidato não encontrado com ID: " + id);
        }
    }

    public String reativar(Long id) {
        if (candidatoRepository.existsById(id)) {
        	candidatoRepository.reativar(id);
            return "Candidato reativado com sucesso";
        } else {
            throw new RuntimeException("Candidato não encontrado com ID: " + id);
        }
    }

    public List<Candidato> findAll() {
        return candidatoRepository.findAll();
    }

    public List<Candidato> findAllAtivos() {
        return candidatoRepository.findAllAtivos();
    }

    public Optional<Candidato> findById(Long id) {
        return candidatoRepository.findById(id);
    }
}
