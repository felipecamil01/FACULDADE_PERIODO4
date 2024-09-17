package com.Urna.Controller;

import com.Urna.Entity.Candidato;
import com.Urna.Service.CandidatoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringJUnitConfig
public class CandidatoControllerTest {

    @InjectMocks
    private CandidatoController candidatoController;

    @Mock
    private CandidatoService candidatoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave() {
        Candidato candidato = new Candidato();
        candidato.setId(1L);
        when(candidatoService.save(any(Candidato.class))).thenReturn(candidato);

        ResponseEntity<Candidato> response = candidatoController.save(candidato);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(candidato, response.getBody());
    }

    @Test
    void testUpdateSuccess() {
        Candidato candidato = new Candidato();
        candidato.setId(1L);
        when(candidatoService.update(anyLong(), any(Candidato.class))).thenReturn(candidato);

        ResponseEntity<Candidato> response = candidatoController.update(1L, candidato);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(candidato, response.getBody());
    }

    @Test
    void testUpdateNotFound() {
        when(candidatoService.update(anyLong(), any(Candidato.class))).thenThrow(new RuntimeException("Candidato não encontrado"));

        ResponseEntity<Candidato> response = candidatoController.update(1L, new Candidato());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testInativarSuccess() {
        when(candidatoService.inativar(anyLong())).thenReturn("Candidato inativado com sucesso");

        ResponseEntity<String> response = candidatoController.inativar(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Candidato inativado com sucesso", response.getBody());
    }

    @Test
    void testInativarError() {
        when(candidatoService.inativar(anyLong())).thenThrow(new RuntimeException("Erro ao inativar candidato"));

        ResponseEntity<String> response = candidatoController.inativar(1L);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Erro ao inativar candidato", response.getBody());
    }

    @Test
    void testReativarSuccess() {
        when(candidatoService.reativar(anyLong())).thenReturn("Candidato reativado com sucesso");

        ResponseEntity<String> response = candidatoController.reativar(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Candidato reativado com sucesso", response.getBody());
    }

    @Test
    void testReativarError() {
        when(candidatoService.reativar(anyLong())).thenThrow(new RuntimeException("Erro ao reativar candidato"));

        ResponseEntity<String> response = candidatoController.reativar(1L);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Erro ao reativar candidato", response.getBody());
    }

    @Test
    void testFindAll() {
        Candidato candidato1 = new Candidato();
        Candidato candidato2 = new Candidato();
        List<Candidato> candidatos = Arrays.asList(candidato1, candidato2);
        when(candidatoService.findAll()).thenReturn(candidatos);

        ResponseEntity<List<Candidato>> response = candidatoController.findAll();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(candidatos, response.getBody());
    }

    @Test
    void testFindAllAtivos() {
        Candidato candidato1 = new Candidato();
        Candidato candidato2 = new Candidato();
        List<Candidato> candidatos = Arrays.asList(candidato1, candidato2);
        when(candidatoService.findAllAtivos()).thenReturn(candidatos);

        ResponseEntity<List<Candidato>> response = candidatoController.findAllAtivos();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(candidatos, response.getBody());
    }

    @Test
    void testFindByIdSuccess() {
        Candidato candidato = new Candidato();
        candidato.setId(1L);
        when(candidatoService.findById(anyLong())).thenReturn(Optional.of(candidato));

        ResponseEntity<Candidato> response = candidatoController.findById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(candidato, response.getBody());
    }

    @Test
    void testFindByIdNotFound() {
        when(candidatoService.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<Candidato> response = candidatoController.findById(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
