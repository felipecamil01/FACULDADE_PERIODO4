package com.Urna.ServiceTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.Urna.Entity.Candidato;
import com.Urna.Entity.Eleitor;


import com.Urna.Entity.StatusEleitor;
import com.Urna.Entity.Voto;
import com.Urna.Repository.CandidatoRepository;
import com.Urna.Repository.EleitorRepository;
import com.Urna.Repository.VotoRepository;
import com.Urna.Service.VotoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

class VotoServiceTest {

    @Mock
    private VotoRepository votoRepository;

    @Mock
    private EleitorRepository eleitorRepository;

    @Mock
    private CandidatoRepository candidatoRepository;

    @InjectMocks
    private VotoService votoService;

    private Eleitor eleitor;
    private Voto voto;
    private Candidato candidatoPrefeito;
    private Candidato candidatoVereador;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup do eleitor
        eleitor = new Eleitor();
        eleitor.setId(1L);
        eleitor.setStatus(StatusEleitor.PENDENTE);

        voto = new Voto();
        candidatoPrefeito = new Candidato();
        candidatoPrefeito.setFuncao(1);
        candidatoVereador = new Candidato();
        candidatoVereador.setFuncao(2);
        voto.setCandidatoPrefeito(candidatoPrefeito);
        voto.setCandidatoVereador(candidatoVereador);
    }

    @Test
    void deveLancarExcecaoQuandoEleitorEstiverInativo() {
        eleitor.setStatus(StatusEleitor.INATIVO);
        when(eleitorRepository.findById(1L)).thenReturn(Optional.of(eleitor));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            votoService.votar(1L, voto);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Usuário inativo. Não pode votar.", exception.getReason());
    }

    @Test
    void deveLancarExcecaoQuandoEleitorEstiverBloqueado() {
        eleitor.setStatus(StatusEleitor.BLOQUEADO);
        when(eleitorRepository.findById(1L)).thenReturn(Optional.of(eleitor));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            votoService.votar(1L, voto);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Usuário bloqueado. Não pode votar.", exception.getReason());
    }

    @Test
    void deveBloquearEleitorComStatusPendenteAoVotar() {
        eleitor.setStatus(StatusEleitor.PENDENTE);
        when(eleitorRepository.findById(1L)).thenReturn(Optional.of(eleitor));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            votoService.votar(1L, voto);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Usuário com cadastro pendente foi bloqueado!", exception.getReason());

        //verifica status para BLOQUEADO
        assertEquals(StatusEleitor.BLOQUEADO, eleitor.getStatus());
        verify(eleitorRepository).save(eleitor);
    }
}

