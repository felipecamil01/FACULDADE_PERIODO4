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

    public String votar(Long eleitorId, Voto voto) {
        Eleitor eleitor = eleitorRepository.findById(eleitorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Eleitor não encontrado"));

        if (eleitor.getStatus() == StatusEleitor.BLOQUEADO) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário bloqueado. Não pode votar.");
        }
        if (eleitor.getStatus() == StatusEleitor.INATIVO) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário inativo. Não pode votar.");
        }
        if (eleitor.getStatus() == StatusEleitor.PENDENTE) {
            eleitor.setStatus(StatusEleitor.BLOQUEADO);
            eleitorRepository.save(eleitor);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário com cadastro pendente foi bloqueado!");
        }
        if (eleitor.getStatus() == StatusEleitor.VOTOU) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário já votou.");
        }

        Candidato candidatoPrefeito = candidatoRepository.findById(voto.getCandidatoPrefeito().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Candidato para prefeito não encontrado"));
        Candidato candidatoVereador = candidatoRepository.findById(voto.getCandidatoVereador().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Candidato para vereador não encontrado"));

        if (candidatoPrefeito.getFuncao() != 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O candidato escolhido para prefeito é um vereador");
        }
        if (candidatoVereador.getFuncao() != 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O candidato escolhido para vereador é um prefeito");
        }
        
        voto.setDataHora(LocalDateTime.now());
        voto.setHashComprovante(UUID.randomUUID().toString());

        votoRepository.save(voto);

        eleitor.setStatus(StatusEleitor.VOTOU);
        eleitorRepository.save(eleitor);

        return voto.getHashComprovante();
    }

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

        candidatosPrefeito.sort((c1, c2) -> Integer.compare(c2.getVotosApurados(), c1.getVotosApurados()));
        candidatosVereador.sort((c1, c2) -> Integer.compare(c2.getVotosApurados(), c1.getVotosApurados()));

        apuracao.setCandidatosPrefeito(candidatosPrefeito);
        apuracao.setCandidatosVereador(candidatosVereador);
        apuracao.setTotalVotos((int) votoRepository.count());

        return apuracao;
    }

}
