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
import java.util.Arrays;
import java.util.Optional;
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
    void testVotar_EleitorNaoEncontrado() {
        // Dados de entrada
        Long eleitorId = 1L;
        Voto voto = new Voto(); // Configure o voto conforme necessário

        // Configuração do mock
        when(eleitorRepository.findById(eleitorId)).thenReturn(Optional.empty());

        // Execução e verificação
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> {
            votoService.votar(eleitorId, voto);
        });

        assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatusCode());
        assertEquals("Eleitor não encontrado", thrown.getReason());
    }
    
    @Test
    void testVotoCandidatoPrefeitoNaoEncontrado() {
        // Arrange
        Long eleitorId = 1L;
        Voto voto = new Voto();
        voto.setCandidatoPrefeito(new Candidato());
        voto.getCandidatoPrefeito().setId(999L);
        voto.setCandidatoVereador(new Candidato());
        voto.getCandidatoVereador().setId(2L);

        when(eleitorRepository.findById(eleitorId)).thenReturn(Optional.of(new Eleitor()));
        when(candidatoRepository.findById(999L)).thenReturn(Optional.empty());
        when(candidatoRepository.findById(2L)).thenReturn(Optional.of(new Candidato()));

        // Act & Assert
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> {
            votoService.votar(eleitorId, voto);
        });

        assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatusCode());
        assertEquals("Candidato para prefeito não encontrado", thrown.getReason());
    }

    @Test
    void testVotoCandidatoVereadorNaoEncontrado() {
        // Arrange
        Long eleitorId = 1L;
        Voto voto = new Voto();
        voto.setCandidatoPrefeito(new Candidato());
        voto.getCandidatoPrefeito().setId(1L);
        voto.setCandidatoVereador(new Candidato());
        voto.getCandidatoVereador().setId(999L);

        when(eleitorRepository.findById(eleitorId)).thenReturn(Optional.of(new Eleitor()));
        when(candidatoRepository.findById(1L)).thenReturn(Optional.of(new Candidato()));
        when(candidatoRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> {
            votoService.votar(eleitorId, voto);
        });

        assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatusCode());
        assertEquals("Candidato para vereador não encontrado", thrown.getReason());
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
