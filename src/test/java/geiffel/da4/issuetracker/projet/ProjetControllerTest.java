package geiffel.da4.issuetracker.projet;

import com.fasterxml.jackson.databind.ObjectMapper;
import geiffel.da4.issuetracker.exceptions.ExceptionHandlingAdvice;
import geiffel.da4.issuetracker.exceptions.ResourceNotFoundException;
import geiffel.da4.issuetracker.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = ProjetController.class)
@Import(ExceptionHandlingAdvice.class)
public class ProjetControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private ProjetService projetService;

    private List<Projet> projets;

    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setup() {

        projets = new ArrayList<>(){{
            add(new Projet(1L, "blah"));
            add(new Projet(2L, "bleuh"));
            add(new Projet(3L, "blih"));
            add(new Projet(4L, "bloh"));
            add(new Projet(5L, "bluh"));
        }};
        Mockito.when(projetService.getAll()).thenReturn(projets);
        Mockito.when(projetService.getById(1L)).thenReturn(projets.get(0));
    }

    @Test
    void whenQueryingRoot_shouldReturn5ProjetsInJson() throws Exception {
        mockMvc.perform(get("/projets")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(content().contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()
        ).andExpect(jsonPath("$", hasSize(5))
        ).andDo(print());
    }

    @Test
    void whenGetWithId1_shouldReturnProjet1() throws Exception {
        mockMvc.perform(get("/projets/1")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(content().contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()
        ).andExpect(jsonPath("$", hasEntry("id", 1))
        ).andDo(print());
    }

    @Test
    void whenCreatingProjet_shouldGetLinkToResource() throws Exception {
        Projet projet = new Projet(6L, "projet6");
        Mockito.when(projetService.create(Mockito.any(Projet.class))).thenReturn(projet);

        String toSend = mapper.writeValueAsString(projet);

        mockMvc.perform(post("/projets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toSend)
        ).andExpect(status().isCreated()
        ).andExpect(header().string("Location","/projets/"+projet.getId())
        ).andDo(print()).andReturn();
    }

    @Test
    void whenUpdateProjet_shouldBeNoContent_andPassCorrectProjetToService() throws Exception {
        Projet projet = projets.get(0);
        projet.setId(7L);

        ArgumentCaptor<Projet> projet_received = ArgumentCaptor.forClass(Projet.class);

        mockMvc.perform(put("/projets/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(projet))
        ).andExpect(status().isNoContent()
        ).andDo(print()).andReturn();

        Mockito.verify(projetService).update(Mockito.anyLong(), projet_received.capture());
        assertNotEquals(projet, projet_received.getValue());
    }

    @Test
    void whenDelete_shouldCallServiceWithCorrectCode() throws Exception {
        Long id_toSend = 1L;

        mockMvc.perform(delete("/projets/"+id_toSend)
        ).andExpect(status().isNoContent()
        ).andDo(print()).andReturn();

        ArgumentCaptor<Long> id_received = ArgumentCaptor.forClass(Long.class);
        verify(projetService).delete(id_received.capture());
        assertEquals(id_toSend, id_received.getValue());
    }

    @Test
    void whenDeleteNonExistingResource_shouldGet404() throws Exception {
        Mockito.doThrow(ResourceNotFoundException.class).when(projetService).delete(anyLong());

        mockMvc.perform(delete("/projets/972")
        ).andExpect(status().isNotFound()
        ).andDo(print()).andReturn();
    }

}
