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
import java.util.List;
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

        String hashComprovante = votoService.votar(1L, voto);

        assertNotNull(hashComprovante);
        assertEquals(36, hashComprovante.length());
        assertEquals(StatusEleitor.VOTOU, eleitor.getStatus());
        verify(votoRepository).save(voto);
        verify(eleitorRepository).save(eleitor);
    }
    
    @Test
    void testVotar_EleitorNaoEncontrado() {
        Long eleitorId = 1L;
        Voto voto = new Voto(); 
        
        when(eleitorRepository.findById(eleitorId)).thenReturn(Optional.empty());

        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> {
            votoService.votar(eleitorId, voto);
        });

        assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatusCode());
        assertEquals("Eleitor não encontrado", thrown.getReason());
    }
    
    @Test
    void testVotoCandidatoPrefeitoNaoEncontrado() {
        Long eleitorId = 1L;
        Voto voto = new Voto();
        voto.setCandidatoPrefeito(new Candidato());
        voto.getCandidatoPrefeito().setId(999L);
        voto.setCandidatoVereador(new Candidato());
        voto.getCandidatoVereador().setId(2L);

        when(eleitorRepository.findById(eleitorId)).thenReturn(Optional.of(new Eleitor()));
        when(candidatoRepository.findById(999L)).thenReturn(Optional.empty());
        when(candidatoRepository.findById(2L)).thenReturn(Optional.of(new Candidato()));

        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> {
            votoService.votar(eleitorId, voto);
        });

        assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatusCode());
        assertEquals("Candidato para prefeito não encontrado", thrown.getReason());
    }

    @Test
    void testVotoCandidatoVereadorNaoEncontrado() {
        Long eleitorId = 1L;
        Voto voto = new Voto();
        voto.setCandidatoPrefeito(new Candidato());
        voto.getCandidatoPrefeito().setId(1L);
        voto.setCandidatoVereador(new Candidato());
        voto.getCandidatoVereador().setId(999L);

        when(eleitorRepository.findById(eleitorId)).thenReturn(Optional.of(new Eleitor()));
        when(candidatoRepository.findById(1L)).thenReturn(Optional.of(new Candidato()));
        when(candidatoRepository.findById(999L)).thenReturn(Optional.empty());

        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> {
            votoService.votar(eleitorId, voto);
        });

        assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatusCode());
        assertEquals("Candidato para vereador não encontrado", thrown.getReason());
    }
    
    @Test
    public void testVotarEleitorBloqueado() {
        Eleitor eleitor = new Eleitor();
        eleitor.setStatus(StatusEleitor.BLOQUEADO);

        Voto voto = new Voto();

        when(eleitorRepository.findById(anyLong())).thenReturn(Optional.of(eleitor));

        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> {
            votoService.votar(1L, voto);
        });

        assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatusCode());
        assertEquals("Usuário bloqueado. Não pode votar.", thrown.getReason());
    }

    @Test
    public void testVotarEleitorPendente() {
        Eleitor eleitor = new Eleitor();
        eleitor.setStatus(StatusEleitor.PENDENTE);

        Voto voto = new Voto();

        when(eleitorRepository.findById(anyLong())).thenReturn(Optional.of(eleitor));

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
        Eleitor eleitor = new Eleitor();
        eleitor.setStatus(StatusEleitor.VOTOU);

        Voto voto = new Voto();

        when(eleitorRepository.findById(anyLong())).thenReturn(Optional.of(eleitor));

        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> {
            votoService.votar(1L, voto);
        });

        assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatusCode());
        assertEquals("Usuário já votou.", thrown.getReason());
    }

    @Test
    public void testVotarEleitorInapto() {
        Eleitor eleitor = new Eleitor();
        eleitor.setStatus(StatusEleitor.INATIVO);

        Voto voto = new Voto();

        when(eleitorRepository.findById(anyLong())).thenReturn(Optional.of(eleitor));

        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> {
            votoService.votar(1L, voto);
        });

        assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatusCode());
        assertEquals("Usuário inativo. Não pode votar.", thrown.getReason());
    }

    @Test
    public void testVotarCandidatoPrefeitoIncorreto() {
        Eleitor eleitor = new Eleitor();
        eleitor.setStatus(StatusEleitor.APTO);

        Candidato candidatoPrefeito = new Candidato();
        candidatoPrefeito.setId(1L);
        candidatoPrefeito.setFuncao(2);

        Candidato candidatoVereador = new Candidato();
        candidatoVereador.setId(2L);
        candidatoVereador.setFuncao(2);

        Voto voto = new Voto();
        voto.setCandidatoPrefeito(candidatoPrefeito);
        voto.setCandidatoVereador(candidatoVereador);

        when(eleitorRepository.findById(anyLong())).thenReturn(Optional.of(eleitor));
        when(candidatoRepository.findById(anyLong())).thenReturn(Optional.of(candidatoPrefeito), Optional.of(candidatoVereador));

        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> {
            votoService.votar(1L, voto);
        });

        assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatusCode());
        assertEquals("O candidato escolhido para prefeito é um vereador", thrown.getReason());
    }

    @Test
    public void testVotarCandidatoVereadorIncorreto() {
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

        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> {
            votoService.votar(1L, voto);
        });

        assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatusCode());
        assertEquals("O candidato escolhido para vereador é um prefeito", thrown.getReason());
    }

    @Test
    public void testRealizarApuracao() {
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

        Apuracao apuracao = votoService.realizarApuracao();

        assertEquals(1, apuracao.getCandidatosPrefeito().size());
        assertEquals(1, apuracao.getCandidatosVereador().size());
        assertEquals(30, apuracao.getTotalVotos());
        verify(candidatoRepository).contarVotosPorCandidato(1L);
        verify(candidatoRepository).contarVotosPorCandidato(2L);
    }
    
    @Test
    public void testRealizarApuracaoOrdemCerta() {
        Candidato candidato1 = new Candidato();
        candidato1.setId(1L);
        candidato1.setVotosApurados(10);

        Candidato candidato2 = new Candidato();
        candidato2.setId(2L);
        candidato2.setVotosApurados(20);

        Candidato candidato3 = new Candidato();
        candidato3.setId(3L);
        candidato3.setVotosApurados(15);

        when(candidatoRepository.findCandidatosPrefeitoAtivos()).thenReturn(Arrays.asList(candidato1, candidato2, candidato3));
        when(candidatoRepository.findCandidatosVereadorAtivos()).thenReturn(Arrays.asList(candidato3, candidato1, candidato2));
        when(candidatoRepository.contarVotosPorCandidato(anyLong())).thenAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            if (id == 1L) return 10;
            if (id == 2L) return 20;
            if (id == 3L) return 15;
            return 0;
        });
        when(votoRepository.count()).thenReturn(45L);

        Apuracao apuracao = votoService.realizarApuracao();

        List<Candidato> candidatosPrefeito = apuracao.getCandidatosPrefeito();
        List<Candidato> candidatosVereador = apuracao.getCandidatosVereador();

        assertEquals(3, candidatosPrefeito.size());
        assertEquals(3, candidatosVereador.size());
        assertEquals(20, candidatosPrefeito.get(0).getVotosApurados());
        assertEquals(15, candidatosPrefeito.get(1).getVotosApurados());
        assertEquals(10, candidatosPrefeito.get(2).getVotosApurados());
        assertEquals(20, candidatosVereador.get(0).getVotosApurados());
        assertEquals(15, candidatosVereador.get(1).getVotosApurados());
        assertEquals(10, candidatosVereador.get(2).getVotosApurados());
    }

}
