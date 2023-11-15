package geiffel.da4.issuetracker.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import geiffel.da4.issuetracker.exceptions.ExceptionHandlingAdvice;
import geiffel.da4.issuetracker.exceptions.ResourceAlreadyExistsException;
import geiffel.da4.issuetracker.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest
@ContextConfiguration(classes = UserController.class)
@Import(ExceptionHandlingAdvice.class)
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    private List<User> users;

    @BeforeEach
    void setUp() {
        users = new ArrayList<>() {{
            add(new User(1L, "Machin", Fonction.USER));
            add(new User(2L, "Chose", Fonction.DEVELOPPER));
            add(new User(3L, "Truc", Fonction.USER));
            add(new User(14L, "higher", Fonction.DEVELOPPER));
            add(new User(7L, "lower", Fonction.USER));
            add(new User(28L, "way higher", Fonction.DEVELOPPER));
        }};
        when(userService.getAll()).thenReturn(users);
        when(userService.getById(7L)).thenReturn(users.get(4));
        when(userService.getById(49L)).thenThrow(ResourceNotFoundException.class);
    }

    @Test
    void whenGettingAll_shouldGet6_andBe200() throws Exception {
        mockMvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()
        ).andExpect(jsonPath("$", hasSize(6))
        ).andDo(print());
    }

    @Test
    void whenGettingId7L_shouldReturnSame() throws Exception{
        mockMvc.perform(get("/users/7")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()
        ).andExpect(jsonPath("$.id", is(7))
        ).andExpect(jsonPath("$.nom", is("lower"))
        ).andExpect(jsonPath("$.fonction", is("USER"))
        ).andReturn();
    }

    @Test
    void whenGettingUnexistingId_should404() throws Exception {
        mockMvc.perform(get("/users/49")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound()
        ).andDo(print());
    }

    @Test
    void whenCreatingNew_shouldReturnLink_andShouldBeStatusCreated() throws Exception {
        User new_user = new User(89L, "nouveau", Fonction.USER);
        ArgumentCaptor<User> user_received = ArgumentCaptor.forClass(User.class);
        when(userService.create(any())).thenReturn(new_user);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(new_user))
        ).andExpect(status().isCreated()
        ).andExpect(header().string("Location", "/users/"+new_user.getId())
        ).andDo(print());

        verify(userService).create(user_received.capture());
        assertEquals(new_user, user_received.getValue());
    }

    @Test
    void whenCreatingWithExistingId_should404() throws Exception {
        when(userService.create(any())).thenThrow(ResourceAlreadyExistsException.class);
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(this.users.get(2)))
        ).andExpect(status().isConflict()
        ).andDo(print());
    }

    @Test
    void whenUpdating_shouldReceiveUserToUpdate_andReturnNoContent() throws Exception {
        User initial_user = users.get(1);
        User updated_user = new User(initial_user.getId(), "updated", initial_user.getFonction());
        ArgumentCaptor<User> user_received = ArgumentCaptor.forClass(User.class);

        mockMvc.perform(put("/users/"+initial_user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(updated_user))
        ).andExpect(status().isNoContent());

        verify(userService).update(anyLong(), user_received.capture());
        assertEquals(updated_user, user_received.getValue());
    }

    @Test
    void whenDeletingExisting_shouldCallServiceWithCorrectId_andSendNoContent() throws Exception {
        Long id = 28L;

        mockMvc.perform(delete("/users/"+id)
        ).andExpect(status().isNoContent()
        ).andDo(print());

        ArgumentCaptor<Long> id_received = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(userService).delete(id_received.capture());
        assertEquals(id, id_received.getValue());
    }
}
