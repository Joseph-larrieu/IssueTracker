package geiffel.da4.issuetracker.projet;


import geiffel.da4.issuetracker.exceptions.ResourceAlreadyExistsException;
import geiffel.da4.issuetracker.exceptions.ResourceNotFoundException;
import geiffel.da4.issuetracker.issue.Issue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ContextConfiguration(classes = ProjetService.class)
public class ProjetServiceTest {

    List<Projet> projets = new ArrayList<>() {{
        add(new Projet(1L, "test1"));
        add(new Projet(2L, "test2"));
        add(new Projet(3L, "test3"));
    }};
    ProjetLocalService projetService = new ProjetLocalService(projets);


    @Test
    void whenGetAll_SizeShouldBe3(){
        List<Projet> projets = projetService.getAll();
        assertEquals(3,projets.size());
    }
    @Test
    void whenQueryingId_shouldHaveSameProjet() {
        assertAll(
                () -> assertEquals(projets.get(1), projetService.getById(2L)),
                () -> assertEquals(projets.get(0), projetService.getById(1L))
        );

    }

    @Test
    void whenCreatingProjet_shouldHaveIncreasedSize_andShouldGetIt() {
        Projet new_projet = new Projet(4L, "blyhgrec");
        int initial_size = projets.size();

        assertAll(
                () -> assertEquals(new_projet, projetService.create(new_projet)),
                () -> assertEquals(new_projet, projets.get(initial_size))
        );
    }
    @Test
    void whenCreatingWithSameId_shouldReturnEmpty_andNotIncrease() {
        Projet projet = projets.get(0);
        int initial_size = projets.size();

        assertAll(
                () -> assertThrows(ResourceAlreadyExistsException.class, ()->projetService.create(projet)),
                () -> assertEquals(initial_size, projets.size())
        );
    }
    @Test
    void whenUpdating_shouldContainModifiedProjet() {
        Projet projetToModify1 = projets.get(1);
        String newTitle = "Modified title";
        Projet updateTitleProjet = new Projet(projetToModify1.getId(), newTitle);
        projetService.update(projetToModify1.getId(), updateTitleProjet);

        assertEquals(newTitle, projetService.getById(projetToModify1.getId()).getNom());
    }

    @Test
    void whenDeleting_shouldBeSmaller() {
        int expected_size = projets.size()-1;
        Long id = 3L;
        projetService.delete(id);
        assertAll(
                () -> assertEquals(expected_size, projetService.getAll().size()),
                () -> assertThrows(ResourceNotFoundException.class, ()->projetService.getById(id))
        );
    }


}
