package geiffel.da4.issuetracker.issue;


import geiffel.da4.issuetracker.exceptions.ResourceAlreadyExistsException;
import geiffel.da4.issuetracker.exceptions.ResourceNotFoundException;
import geiffel.da4.issuetracker.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ContextConfiguration(classes = IssueService.class)
public class IssueServiceTest {

    private IssueService issueService;
    private List<Issue> issues;
    private User author = Mockito.mock(User.class);

    @BeforeEach
    void setUp() {
        issues = new ArrayList<>(){{
            add(new Issue(1L, "blah", "some content1", author, Timestamp.from(Instant.now()), null));
            add(new Issue(2L, "bleuh", "some content2", author, Timestamp.from(Instant.now()), null));
            add(new Issue(3L, "blih", "some content3", author, Timestamp.from(Instant.now()), null));
            add(new Issue(4L, "bloh", "some content4", author, Timestamp.from(Instant.now()), null));
            add(new Issue(5L, "bluh", "some content5", author, Timestamp.from(Instant.now()), null));
        }};
        issueService = new IssueLocalService(issues);
    }


    @Test
    void whenGettingAll_shouldHave5Issues() {
        assertEquals(5, issueService.getAll().size(), "There should be 5 issues in total");
    }

    @Test
    void whenQueryingCode_shouldHaveSameIssue() {
        assertAll(
                () -> assertEquals(issues.get(4), issueService.getByCode(5L)),
                () -> assertEquals(issues.get(0), issueService.getByCode(1L))
        );

    }

    @Test
    void whenCreatingIssue_shouldHaveIncreasedSize_andShouldGetIt() {
        Issue new_issue = new Issue(7L, "blyhgrec", "some stuff", author, Timestamp.from(Instant.now()), null);
        int initial_size = issues.size();

        assertAll(
                () -> assertEquals(new_issue, issueService.create(new_issue)),
                () -> assertEquals(initial_size+1, issues.size()),
                () -> assertEquals(new_issue, issues.get(initial_size))
        );
    }

    @Test
    void whenCreatingWithSameId_shouldReturnEmpty_andNotIncrease() {
        Issue issue = issues.get(0);
        int initial_size = issues.size();

        assertAll(
                () -> assertThrows(ResourceAlreadyExistsException.class, ()->issueService.create(issue)),
                () -> assertEquals(initial_size, issues.size())
        );
    }

    @Test
    void whenUpdating_shouldContainModifiedIssue() {
        Issue issueToModify1 = issues.get(2);
        Issue issueToModify2 = issues.get(3);
        String newTitle = "Modified title";
        String newContent = "Modified content";
        Issue updateTitleIssue = new Issue(issueToModify1.getCode(), newTitle, issueToModify1.getContent(),
                issueToModify1.getEmitter(), issueToModify1.getDateCreated(), issueToModify1.getDateClosed());
        Issue updatedContentIssue = new Issue(issueToModify2.getCode(), issueToModify2.getTitle(), newContent,
                issueToModify2.getEmitter(), issueToModify2.getDateCreated(), issueToModify2.getDateClosed());

        issueService.update(issueToModify1.getCode(), updateTitleIssue);
        issueService.update(issueToModify2.getCode(), updatedContentIssue);

        assertAll(
                () -> assertEquals(newTitle, issueService.getByCode(issueToModify1.getCode()).getTitle()),
                () -> assertEquals(newContent, issueService.getByCode(issueToModify2.getCode()).getContent())
        );
    }

    @Test
    void whenDeleting_shouldBeSmaller() {
        int expected_size = issues.size()-1;
        Long code = 4L;
        issueService.delete(code);
        assertAll(
                () -> assertEquals(expected_size, issueService.getAll().size()),
                () -> assertThrows(ResourceNotFoundException.class, ()->issueService.getByCode(code))
        );
    }

}
