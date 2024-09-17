package com.Urna.Service;

import com.Urna.Entity.Eleitor;
import com.Urna.Entity.StatusEleitor;
import com.Urna.Repository.EleitorRepository;
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

class EleitorServiceTest {

    @Mock
    EleitorRepository eleitorRepository;

    @InjectMocks
    EleitorService eleitorService;

    Eleitor eleitor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        eleitor = new Eleitor();
        eleitor.setId(1L);
        eleitor.setNome("João Silva");
        eleitor.setCpf("121.753.469-51");
        eleitor.setEmail("pedro.henriqueifpr@gmail.com");
        eleitor.setProfissao("Estudante");
        eleitor.setTelefoneCelular("(45)998517717");
        eleitor.setStatus(StatusEleitor.APTO);
        
    }

    @Test
    void testSaveEleitor() {
        when(eleitorRepository.save(any(Eleitor.class))).thenReturn(eleitor);

        Eleitor savedEleitor = eleitorService.save(eleitor);

        assertNotNull(savedEleitor);
        assertEquals(StatusEleitor.APTO, savedEleitor.getStatus());
        verify(eleitorRepository, times(1)).save(eleitor);
    }
    
    @Test
    void testSaveEleitorPendenteCpfNull() {
        when(eleitorRepository.save(any(Eleitor.class))).thenReturn(eleitor);

        eleitor.setCpf(null);
        Eleitor savedEleitor = eleitorService.save(eleitor);
        

        assertNotNull(savedEleitor);
        assertEquals(StatusEleitor.PENDENTE, savedEleitor.getStatus());
        verify(eleitorRepository, times(1)).save(eleitor);
    }

    @Test
    void testSaveEleitorPendenteEmailNull() {
        when(eleitorRepository.save(any(Eleitor.class))).thenReturn(eleitor);

        eleitor.setEmail(null);
        Eleitor savedEleitor = eleitorService.save(eleitor);
        

        assertNotNull(savedEleitor);
        assertEquals(StatusEleitor.PENDENTE, savedEleitor.getStatus());
        verify(eleitorRepository, times(1)).save(eleitor);
    }
    
    void testSaveEleitorPendenteCpfBlank() {
        when(eleitorRepository.save(any(Eleitor.class))).thenReturn(eleitor);

        eleitor.setCpf("");
        Eleitor savedEleitor = eleitorService.save(eleitor);
        

        assertNotNull(savedEleitor);
        assertEquals(StatusEleitor.PENDENTE, savedEleitor.getStatus());
        verify(eleitorRepository, times(1)).save(eleitor);
    }
    
    void testSaveEleitorPendenteEmailBlank() {
        when(eleitorRepository.save(any(Eleitor.class))).thenReturn(eleitor);

        eleitor.setEmail("");
        Eleitor savedEleitor = eleitorService.save(eleitor);
        

        assertNotNull(savedEleitor);
        assertEquals(StatusEleitor.PENDENTE, savedEleitor.getStatus());
        verify(eleitorRepository, times(1)).save(eleitor);
    }

    @Test
    void testUpdateEleitor() {
        when(eleitorRepository.existsById(anyLong())).thenReturn(true);
        when(eleitorRepository.findById(anyLong())).thenReturn(Optional.of(eleitor));
        when(eleitorRepository.save(any(Eleitor.class))).thenReturn(eleitor);

        Eleitor eleitorAtualizado = new Eleitor();
        eleitorAtualizado.setNome("Maria Oliveira");
        eleitorAtualizado.setCpf("121.753.469-51");
        eleitorAtualizado.setEmail("pedro.henriqueifpr@gmail.com");
        eleitorAtualizado.setProfissao("Estudante");
        eleitorAtualizado.setTelefoneCelular("(45)998517717");
        eleitorAtualizado.setStatus(StatusEleitor.APTO);

        Eleitor updatedEleitor = eleitorService.update(1L, eleitorAtualizado);

        assertNotNull(updatedEleitor);
        assertEquals(1L, updatedEleitor.getId());
        assertEquals(StatusEleitor.APTO, updatedEleitor.getStatus());
        verify(eleitorRepository, times(1)).save(eleitorAtualizado);
    }
    
    void testUpdateEleitorInativo() {
        when(eleitorRepository.existsById(anyLong())).thenReturn(true);
        when(eleitorRepository.findById(anyLong())).thenReturn(Optional.of(eleitor));
        when(eleitorRepository.save(any(Eleitor.class))).thenReturn(eleitor);

        Eleitor eleitorAtualizado = new Eleitor();
        eleitorAtualizado.setNome("Maria Oliveira");
        eleitorAtualizado.setCpf("121.753.469-51");
        eleitorAtualizado.setEmail("pedro.henriqueifpr@gmail.com");
        eleitorAtualizado.setProfissao("Estudante");
        eleitorAtualizado.setTelefoneCelular("(45)998517717");
        eleitorAtualizado.setStatus(StatusEleitor.INATIVO);

        Eleitor updatedEleitor = eleitorService.update(1L, eleitorAtualizado);

        assertNotNull(updatedEleitor);
        assertEquals(1L, updatedEleitor.getId());
        assertEquals(StatusEleitor.INATIVO, updatedEleitor.getStatus());
        verify(eleitorRepository, times(1)).save(eleitorAtualizado);
    }
    
    void testUpdateEleitorNull() {
        when(eleitorRepository.existsById(anyLong())).thenReturn(true);
        when(eleitorRepository.findById(anyLong())).thenReturn(Optional.of(eleitor));
        when(eleitorRepository.save(any(Eleitor.class))).thenReturn(eleitor);

        Eleitor eleitorAtualizado = new Eleitor();
        eleitorAtualizado.setNome("Maria Oliveira");
        eleitorAtualizado.setCpf(null);
        eleitorAtualizado.setEmail(null);
        eleitorAtualizado.setProfissao("Estudante");
        eleitorAtualizado.setTelefoneCelular("(45)998517717");
        eleitorAtualizado.setStatus(StatusEleitor.PENDENTE);

        Eleitor updatedEleitor = eleitorService.update(1L, eleitorAtualizado);

        assertNotNull(updatedEleitor);
        assertEquals(1L, updatedEleitor.getId());
        assertEquals(StatusEleitor.PENDENTE, updatedEleitor.getStatus());
        verify(eleitorRepository, times(1)).save(eleitorAtualizado);
    }


    @Test
    void testUpdateEleitorNotFound() {
        when(eleitorRepository.existsById(anyLong())).thenReturn(false);

        Exception exception = assertThrows(RuntimeException.class, () -> {
        	eleitorService.update(1L, eleitor);
        });

        assertEquals("Eleitor não encontrado com ID: 1", exception.getMessage());
        verify(eleitorRepository, never()).save(any(Eleitor.class));
    }

    @Test
    void testInativarEleitor() {
        when(eleitorRepository.existsById(anyLong())).thenReturn(true);

        String result = eleitorService.inativar(1L);

        assertEquals("Eleitor inativado com sucesso", result);
        verify(eleitorRepository, times(1)).inativar(1L);
    }

    @Test
    void testInativarEleitorNotFound() {
        when(eleitorRepository.existsById(anyLong())).thenReturn(false);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            eleitorService.inativar(1L);
        });

        assertEquals("Eleitor não encontrado com ID: 1", exception.getMessage());
        verify(eleitorRepository, never()).inativar(anyLong());
    }

    @Test
    void testReativarEleitor() {
        when(eleitorRepository.existsById(anyLong())).thenReturn(true);
        when(eleitorRepository.findById(anyLong())).thenReturn(Optional.of(eleitor));

        String result = eleitorService.reativar(1L);

        assertEquals("Eleitor reativado com sucesso", result);
        verify(eleitorRepository, times(1)).reativar(1L);
    }


    @Test
    void testReativarEleitorNotFound() {
        when(eleitorRepository.existsById(anyLong())).thenReturn(false);

        Exception exception = assertThrows(RuntimeException.class, () -> {
        	eleitorService.reativar(1L);
        });

        assertEquals("Eleitor não encontrado com ID: 1", exception.getMessage());
        verify(eleitorRepository, never()).reativar(anyLong());
    }

    @Test
    void testFindById() {
        when(eleitorRepository.findById(anyLong())).thenReturn(Optional.of(eleitor));

        Optional<Eleitor> foundEleitor = eleitorService.findById(1L);

        assertTrue(foundEleitor.isPresent());
        assertEquals(1L, foundEleitor.get().getId());
        verify(eleitorRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(eleitorRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<Eleitor> foundEleitor = eleitorService.findById(1L);

        assertFalse(foundEleitor.isPresent());
        verify(eleitorRepository, times(1)).findById(1L);
    }
    
    @Test
    void testFindAll() {
        Eleitor Eleitor1 = new Eleitor();
        Eleitor1.setId(1L);
        Eleitor1.setNome("João Silva");
        Eleitor1.setStatus(StatusEleitor.APTO);

        Eleitor Eleitor2 = new Eleitor();
        Eleitor2.setId(2L);
        Eleitor2.setNome("Maria Oliveira");
        Eleitor2.setStatus(StatusEleitor.APTO);

        List<Eleitor> Eleitors = List.of(Eleitor1, Eleitor2);

        when(eleitorRepository.findAll()).thenReturn(Eleitors);

        List<Eleitor> foundEleitors = eleitorService.findAll();

        assertNotNull(foundEleitors);
        assertEquals(2, foundEleitors.size());
        verify(eleitorRepository, times(1)).findAll();
    }

    @Test
    void testfindAllAptos() {
        Eleitor Eleitor1 = new Eleitor();
        Eleitor1.setId(1L);
        Eleitor1.setNome("João Silva");
        Eleitor1.setStatus(StatusEleitor.APTO);

        Eleitor Eleitor2 = new Eleitor();
        Eleitor2.setId(2L);
        Eleitor2.setNome("Maria Oliveira");
        Eleitor2.setStatus(StatusEleitor.APTO);

        List<Eleitor> EleitorsAPTOs = List.of(Eleitor1, Eleitor2);

        when(eleitorRepository.findAllAptos()).thenReturn(EleitorsAPTOs);

        List<Eleitor> foundEleitorsAPTOs = eleitorService.findAllAptos();

        assertNotNull(foundEleitorsAPTOs);
        assertEquals(2, foundEleitorsAPTOs.size());
        assertTrue(foundEleitorsAPTOs.stream().allMatch(c -> c.getStatus() == StatusEleitor.APTO));
        verify(eleitorRepository, times(1)).findAllAptos();
    }

}
