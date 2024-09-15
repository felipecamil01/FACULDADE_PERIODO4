package com.Urna.Service;

import com.Urna.Entity.*;
import com.Urna.Repository.CandidatoRepository;
import com.Urna.Repository.EleitorRepository;
import com.Urna.Repository.VotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class VotoService {
    @Autowired
    private VotoRepository votoRepository;
    @Autowired
    private EleitorRepository eleitorRepository;
    @Autowired
    private CandidatoRepository candidatoRepository;

    //voto
    public String votar(Long eleitorId, Voto voto) {
        Eleitor eleitor = eleitorRepository.findById(eleitorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Eleitor não encontrado"));

        if (eleitor.getStatus() != StatusEleitor.APTO) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Eleitor inapto para votação");
        }
        verificarCandidatos(voto);
        voto.setDataHora(LocalDateTime.now());
        voto.setHashComprovante(UUID.randomUUID().toString());

        votoRepository.save(voto);

        //atualiza o status
        eleitor.setStatus(StatusEleitor.VOTOU);
        eleitorRepository.save(eleitor);

        return voto.getHashComprovante();
    }

    //valida candidato
    private void verificarCandidatos(Voto voto) {
        if (voto.getCandidatoPrefeito().getFuncao() != 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O candidato escolhido para prefeito é um vereador");
        }
        if (voto.getCandidatoVereador().getFuncao() != 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O candidato escolhido para vereador é um prefeito");
        }
    }

    //Apuracao
    public Apuracao realizarApuracao() {
        Apuracao apuracao = new Apuracao();

        List<Candidato> candidatosPrefeito = candidatoRepository.findCandidatosPrefeitoAtivos();
        List<Candidato> candidatosVereador = candidatoRepository.findCandidatosVereadorAtivos();

        for (Candidato candidato : candidatosPrefeito) {
            candidato.setVotosApurados(candidatoRepository.contarVotosPorCandidato(candidato.getId()));
        }
        for (Candidato candidato : candidatosVereador) {
            candidato.setVotosApurados(candidatoRepository.contarVotosPorCandidato(candidato.getId()));
        }
        apuracao.setCandidatosPrefeito(candidatosPrefeito);
        apuracao.setCandidatosVereador(candidatosVereador);
        apuracao.setTotalVotos(candidatosPrefeito.size() + candidatosVereador.size());

        return apuracao;
    }
}
