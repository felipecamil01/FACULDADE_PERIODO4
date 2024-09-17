package com.Urna.Service;

import com.Urna.Entity.Candidato;
import com.Urna.Entity.Eleitor;
import com.Urna.Entity.StatusCandidato;
import com.Urna.Entity.StatusEleitor;
import com.Urna.Repository.CandidatoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class CandidatoServiceTest {

    @Mock
    private CandidatoRepository candidatoRepository;

    @InjectMocks
    private CandidatoService candidatoService;

    private Candidato candidato;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        candidato = new Candidato();
        candidato.setId(1L);
        candidato.setNome("João Silva");
        candidato.setStatus(StatusCandidato.ATIVO);
    }

    @Test
    void testSaveCandidato() {
        when(candidatoRepository.save(any(Candidato.class))).thenReturn(candidato);

        Candidato savedCandidato = candidatoService.save(candidato);

        assertNotNull(savedCandidato);
        assertEquals(StatusCandidato.ATIVO, savedCandidato.getStatus());
        verify(candidatoRepository, times(1)).save(candidato);
    }

    @Test
    void testUpdateCandidato() {
        when(candidatoRepository.existsById(anyLong())).thenReturn(true);
        when(candidatoRepository.save(any(Candidato.class))).thenReturn(candidato);

        Candidato candidatoAtualizado = new Candidato();
        candidatoAtualizado.setNome("Maria Oliveira");

        Candidato updatedCandidato = candidatoService.update(1L, candidatoAtualizado);

        assertNotNull(updatedCandidato);
        assertEquals(1L, updatedCandidato.getId());
        assertEquals(StatusCandidato.ATIVO, updatedCandidato.getStatus());
        verify(candidatoRepository, times(1)).save(candidatoAtualizado);
    }

    @Test
    void testUpdateCandidatoNotFound() {
        when(candidatoRepository.existsById(anyLong())).thenReturn(false);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            candidatoService.update(1L, candidato);
        });

        assertEquals("Candidato não encontrado com ID: 1", exception.getMessage());
        verify(candidatoRepository, never()).save(any(Candidato.class));
    }

    @Test
    void testInativarCandidato() {
        when(candidatoRepository.existsById(anyLong())).thenReturn(true);
        when(candidatoRepository.findById(anyLong())).thenReturn(Optional.of(candidato));
        when(candidatoRepository.save(any(Candidato.class))).thenReturn(candidato);
        
        String result = candidatoService.inativar(1L);

        assertEquals("Candidato inativado com sucesso", result);
        assertEquals(StatusCandidato.INATIVO, candidato.getStatus());
        verify(candidatoRepository, times(1)).save(candidato);
    }

    @Test
    void testInativarCandidatoNotFound() {
    	when(candidatoRepository.existsById(anyLong())).thenReturn(false);

        Exception exception = assertThrows(RuntimeException.class, () -> {
        	candidatoService.inativar(1L);
        });

        assertEquals("Candidato não encontrado com ID: 1", exception.getMessage());
        verify(candidatoRepository, never()).save(candidato);
    }
    
    @Test
    void testReativarCandidato() {
    	when(candidatoRepository.existsById(anyLong())).thenReturn(true);
        when(candidatoRepository.findById(anyLong())).thenReturn(Optional.of(candidato));
        when(candidatoRepository.save(any(Candidato.class))).thenReturn(candidato);

        candidato.setStatus(StatusCandidato.INATIVO);
        String result = candidatoService.reativar(1L);

        assertEquals("Candidato reativado com sucesso", result);
        assertEquals(StatusCandidato.ATIVO, candidato.getStatus());
        verify(candidatoRepository, times(1)).save(candidato);
    }
    
    @Test
    void testReativarCandidatoNotFound() {
    	when(candidatoRepository.existsById(anyLong())).thenReturn(false);

        Exception exception = assertThrows(RuntimeException.class, () -> {
        	candidatoService.reativar(1L);
        });

        assertEquals("Candidato não encontrado com ID: 1", exception.getMessage());
        verify(candidatoRepository, never()).save(candidato);
    }

    @Test
    void testFindById() {
        when(candidatoRepository.findById(anyLong())).thenReturn(Optional.of(candidato));

        Optional<Candidato> foundCandidato = candidatoService.findById(1L);

        assertTrue(foundCandidato.isPresent());
        assertEquals(1L, foundCandidato.get().getId());
        verify(candidatoRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(candidatoRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<Candidato> foundCandidato = candidatoService.findById(1L);

        assertFalse(foundCandidato.isPresent());
        verify(candidatoRepository, times(1)).findById(1L);
    }
    
    @Test
    void testFindAll() {
        Candidato candidato1 = new Candidato();
        candidato1.setId(1L);
        candidato1.setNome("João Silva");
        candidato1.setStatus(StatusCandidato.ATIVO);

        Candidato candidato2 = new Candidato();
        candidato2.setId(2L);
        candidato2.setNome("Maria Oliveira");
        candidato2.setStatus(StatusCandidato.ATIVO);

        List<Candidato> candidatos = List.of(candidato1, candidato2);

        when(candidatoRepository.findAll()).thenReturn(candidatos);

        List<Candidato> foundCandidatos = candidatoService.findAll();

        assertNotNull(foundCandidatos);
        assertEquals(2, foundCandidatos.size());
        verify(candidatoRepository, times(1)).findAll();
    }

    @Test
    void testFindAllAtivos() {
        Candidato candidato1 = new Candidato();
        candidato1.setId(1L);
        candidato1.setNome("João Silva");
        candidato1.setStatus(StatusCandidato.ATIVO);

        Candidato candidato2 = new Candidato();
        candidato2.setId(2L);
        candidato2.setNome("Maria Oliveira");
        candidato2.setStatus(StatusCandidato.ATIVO);

        List<Candidato> candidatosAtivos = List.of(candidato1, candidato2);

        when(candidatoRepository.findAllAtivos()).thenReturn(candidatosAtivos);

        List<Candidato> foundCandidatosAtivos = candidatoService.findAllAtivos();

        assertNotNull(foundCandidatosAtivos);
        assertEquals(2, foundCandidatosAtivos.size());
        assertTrue(foundCandidatosAtivos.stream().allMatch(c -> c.getStatus() == StatusCandidato.ATIVO));
        verify(candidatoRepository, times(1)).findAllAtivos();
    }

}
