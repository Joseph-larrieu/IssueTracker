package geiffel.da4.issuetracker.issue;

import geiffel.da4.issuetracker.user.User;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface IssueService {
    List<Issue> getAll();

    Issue getByCode(Long l);

    User getById(Long id);

    Issue create(Issue newIssue);

    void update(Long code, Issue updatedIssue);

    void delete(Long code);
}
