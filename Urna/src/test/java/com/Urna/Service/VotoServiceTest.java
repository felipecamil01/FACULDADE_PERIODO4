package com.Urna.Service;

import com.Urna.Entity.*;
import com.Urna.Repository.CandidatoRepository;
import com.Urna.Repository.EleitorRepository;
import com.Urna.Repository.VotoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class VotoServiceTest {

    @Mock
    private VotoRepository votoRepository;
    
    @Mock
    private EleitorRepository eleitorRepository;
    
    @Mock
    private CandidatoRepository candidatoRepository;

    @InjectMocks
    private VotoService votoService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testVotarSuccess() {
        // Arrange
        Eleitor eleitor = new Eleitor();
        eleitor.setStatus(StatusEleitor.APTO);

        Candidato candidatoPrefeito = new Candidato();
        candidatoPrefeito.setId(1L);
        candidatoPrefeito.setFuncao(1);

        Candidato candidatoVereador = new Candidato();
        candidatoVereador.setId(2L);
        candidatoVereador.setFuncao(2);

        Voto voto = new Voto();
        voto.setCandidatoPrefeito(candidatoPrefeito);
        voto.setCandidatoVereador(candidatoVereador);

        when(eleitorRepository.findById(anyLong())).thenReturn(Optional.of(eleitor));
        when(candidatoRepository.findById(anyLong())).thenReturn(Optional.of(candidatoPrefeito), Optional.of(candidatoVereador));
        when(votoRepository.save(any(Voto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        String hashComprovante = votoService.votar(1L, voto);

        // Assert
        assertNotNull(hashComprovante);
        assertEquals(36, hashComprovante.length()); // UUID length
        assertEquals(StatusEleitor.VOTOU, eleitor.getStatus());
        verify(votoRepository).save(voto);
        verify(eleitorRepository).save(eleitor);
    }

    @Test
    public void testVotarEleitorBloqueado() {
        // Arrange
        Eleitor eleitor = new Eleitor();
        eleitor.setStatus(StatusEleitor.BLOQUEADO);

        Voto voto = new Voto();

        when(eleitorRepository.findById(anyLong())).thenReturn(Optional.of(eleitor));

        // Act & Assert
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> {
            votoService.votar(1L, voto);
        });

        assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatusCode());
        assertEquals("Usuário bloqueado. Não pode votar.", thrown.getReason());
    }

    @Test
    public void testVotarEleitorPendente() {
        // Arrange
        Eleitor eleitor = new Eleitor();
        eleitor.setStatus(StatusEleitor.PENDENTE);

        Voto voto = new Voto();

        when(eleitorRepository.findById(anyLong())).thenReturn(Optional.of(eleitor));

        // Act & Assert
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> {
            votoService.votar(1L, voto);
        });

        assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatusCode());
        assertEquals("Usuário com cadastro pendente foi bloqueado!", thrown.getReason());
        assertEquals(StatusEleitor.BLOQUEADO, eleitor.getStatus());
        verify(eleitorRepository).save(eleitor);
    }

    @Test
    public void testVotarEleitorJaVotou() {
        // Arrange
        Eleitor eleitor = new Eleitor();
        eleitor.setStatus(StatusEleitor.VOTOU);

        Voto voto = new Voto();

        when(eleitorRepository.findById(anyLong())).thenReturn(Optional.of(eleitor));

        // Act & Assert
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> {
            votoService.votar(1L, voto);
        });

        assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatusCode());
        assertEquals("Usuário já votou.", thrown.getReason());
    }

    @Test
    public void testVotarEleitorInapto() {
        // Arrange
        Eleitor eleitor = new Eleitor();
        eleitor.setStatus(StatusEleitor.INATIVO);

        Voto voto = new Voto();

        when(eleitorRepository.findById(anyLong())).thenReturn(Optional.of(eleitor));

        // Act & Assert
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> {
            votoService.votar(1L, voto);
        });

        assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatusCode());
        assertEquals("Usuário inativo. Não pode votar.", thrown.getReason());
    }

    @Test
    public void testVotarCandidatoPrefeitoIncorreto() {
        // Arrange
        Eleitor eleitor = new Eleitor();
        eleitor.setStatus(StatusEleitor.APTO);

        Candidato candidatoPrefeito = new Candidato();
        candidatoPrefeito.setId(1L);
        candidatoPrefeito.setFuncao(2); // Funcao incorreta para prefeito

        Candidato candidatoVereador = new Candidato();
        candidatoVereador.setId(2L);
        candidatoVereador.setFuncao(2);

        Voto voto = new Voto();
        voto.setCandidatoPrefeito(candidatoPrefeito);
        voto.setCandidatoVereador(candidatoVereador);

        when(eleitorRepository.findById(anyLong())).thenReturn(Optional.of(eleitor));
        when(candidatoRepository.findById(anyLong())).thenReturn(Optional.of(candidatoPrefeito), Optional.of(candidatoVereador));

        // Act & Assert
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> {
            votoService.votar(1L, voto);
        });

        assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatusCode());
        assertEquals("O candidato escolhido para prefeito é um vereador", thrown.getReason());
    }

    @Test
    public void testVotarCandidatoVereadorIncorreto() {
        // Arrange
        Eleitor eleitor = new Eleitor();
        eleitor.setStatus(StatusEleitor.APTO);

        Candidato candidatoPrefeito = new Candidato();
        candidatoPrefeito.setId(1L);
        candidatoPrefeito.setFuncao(1);

        Candidato candidatoVereador = new Candidato();
        candidatoVereador.setId(2L);
        candidatoVereador.setFuncao(1); // Funcao incorreta para vereador

        Voto voto = new Voto();
        voto.setCandidatoPrefeito(candidatoPrefeito);
        voto.setCandidatoVereador(candidatoVereador);

        when(eleitorRepository.findById(anyLong())).thenReturn(Optional.of(eleitor));
        when(candidatoRepository.findById(anyLong())).thenReturn(Optional.of(candidatoPrefeito), Optional.of(candidatoVereador));

        // Act & Assert
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> {
            votoService.votar(1L, voto);
        });

        assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatusCode());
        assertEquals("O candidato escolhido para vereador é um prefeito", thrown.getReason());
    }

    @Test
    public void testRealizarApuracao() {
        // Arrange
        Candidato candidatoPrefeito = new Candidato();
        candidatoPrefeito.setId(1L);
        candidatoPrefeito.setVotosApurados(10);

        Candidato candidatoVereador = new Candidato();
        candidatoVereador.setId(2L);
        candidatoVereador.setVotosApurados(20);

        when(candidatoRepository.findCandidatosPrefeitoAtivos()).thenReturn(Arrays.asList(candidatoPrefeito));
        when(candidatoRepository.findCandidatosVereadorAtivos()).thenReturn(Arrays.asList(candidatoVereador));
        when(candidatoRepository.contarVotosPorCandidato(anyLong())).thenReturn(10);
        when(votoRepository.count()).thenReturn(30L);

        // Act
        Apuracao apuracao = votoService.realizarApuracao();

        // Assert
        assertEquals(1, apuracao.getCandidatosPrefeito().size());
        assertEquals(1, apuracao.getCandidatosVereador().size());
        assertEquals(30, apuracao.getTotalVotos());
        verify(candidatoRepository).contarVotosPorCandidato(1L);
        verify(candidatoRepository).contarVotosPorCandidato(2L);
    }
}
