package Controller;

import com.Urna.Entity.Eleitor;
import com.Urna.Service.EleitorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class EleitorControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private EleitorController eleitorController;

    @Mock
    private EleitorService eleitorService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(eleitorController).build();
    }

    @Test
    public void testSave() throws Exception {
        Eleitor eleitor = new Eleitor();
        eleitor.setId(1L);

        when(eleitorService.save(any(Eleitor.class))).thenReturn(eleitor);

        mockMvc.perform(post("/eleitor/save")
                .contentType("application/json")
                .content("{\"id\":1}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));

        verify(eleitorService, times(1)).save(any(Eleitor.class));
    }

    @Test
    public void testUpdate() throws Exception {
        Eleitor eleitor = new Eleitor();
        eleitor.setId(1L);

        when(eleitorService.update(anyLong(), any(Eleitor.class))).thenReturn(eleitor);

        mockMvc.perform(put("/eleitor/update/1")
                .contentType("application/json")
                .content("{\"id\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(eleitorService, times(1)).update(anyLong(), any(Eleitor.class));
    }

    @Test
    public void testUpdateNotFound() throws Exception {
        when(eleitorService.update(anyLong(), any(Eleitor.class))).thenThrow(new RuntimeException());

        mockMvc.perform(put("/eleitor/update/1")
                .contentType("application/json")
                .content("{\"id\":1}"))
                .andExpect(status().isNotFound());

        verify(eleitorService, times(1)).update(anyLong(), any(Eleitor.class));
    }

    @Test
    public void testInativar() throws Exception {
        when(eleitorService.inativar(anyLong())).thenReturn("Inativado com sucesso");

        mockMvc.perform(delete("/eleitor/inativar/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Inativado com sucesso"));

        verify(eleitorService, times(1)).inativar(anyLong());
    }

    @Test
    public void testInativarError() throws Exception {
        when(eleitorService.inativar(anyLong())).thenThrow(new RuntimeException("Erro ao inativar"));

        mockMvc.perform(delete("/eleitor/inativar/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Erro ao inativar"));

        verify(eleitorService, times(1)).inativar(anyLong());
    }

    @Test
    public void testReativar() throws Exception {
        when(eleitorService.reativar(anyLong())).thenReturn("Reativado com sucesso");

        mockMvc.perform(delete("/eleitor/reativar/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Reativado com sucesso"));

        verify(eleitorService, times(1)).reativar(anyLong());
    }

    @Test
    public void testReativarError() throws Exception {
        when(eleitorService.reativar(anyLong())).thenThrow(new RuntimeException("Erro ao reativar"));

        mockMvc.perform(delete("/eleitor/reativar/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Erro ao reativar"));

        verify(eleitorService, times(1)).reativar(anyLong());
    }

    @Test
    public void testFindAll() throws Exception {
        Eleitor eleitor1 = new Eleitor();
        eleitor1.setId(1L);
        Eleitor eleitor2 = new Eleitor();
        eleitor2.setId(2L);

        when(eleitorService.findAll()).thenReturn(Arrays.asList(eleitor1, eleitor2));

        mockMvc.perform(get("/eleitor/findAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));

        verify(eleitorService, times(1)).findAll();
    }

    @Test
    public void testFindAllAptos() throws Exception {
        Eleitor eleitor = new Eleitor();
        eleitor.setId(1L);

        when(eleitorService.findAllAptos()).thenReturn(Collections.singletonList(eleitor));

        mockMvc.perform(get("/eleitor/findAllAptos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1));

        verify(eleitorService, times(1)).findAllAptos();
    }

    @Test
    public void testFindById() throws Exception {
        Eleitor eleitor = new Eleitor();
        eleitor.setId(1L);

        when(eleitorService.findById(anyLong())).thenReturn(Optional.of(eleitor));

        mockMvc.perform(get("/eleitor/findById/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(eleitorService, times(1)).findById(anyLong());
    }

    @Test
    public void testFindByIdNotFound() throws Exception {
        when(eleitorService.findById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/eleitor/findById/1"))
                .andExpect(status().isNotFound());

        verify(eleitorService, times(1)).findById(anyLong());
    }
}
