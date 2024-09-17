package Controller;

import com.Urna.Entity.Apuracao;
import com.Urna.Entity.Voto;
import com.Urna.Service.VotoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class VotoControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private VotoController votoController;

    @Mock
    private VotoService votoService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(votoController).build();
    }

    @Test
    public void testVotar() throws Exception {
        String hash = "someHash";

        when(votoService.votar(anyLong(), any(Voto.class))).thenReturn(hash);

        mockMvc.perform(post("/voto/votar/1")
                .contentType("application/json")
                .content("{\"someField\":\"someValue\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string(hash));

        verify(votoService, times(1)).votar(anyLong(), any(Voto.class));
    }

    @Test
    public void testRealizarApuracao() throws Exception {
        Apuracao apuracao = new Apuracao();

        when(votoService.realizarApuracao()).thenReturn(apuracao);

        mockMvc.perform(get("/voto/apurar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());

        verify(votoService, times(1)).realizarApuracao();
    }
}
