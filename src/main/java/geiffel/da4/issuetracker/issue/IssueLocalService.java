package geiffel.da4.issuetracker.issue;

import geiffel.da4.issuetracker.exceptions.ResourceAlreadyExistsException;
import geiffel.da4.issuetracker.exceptions.ResourceNotFoundException;
import geiffel.da4.issuetracker.user.User;
import geiffel.da4.issuetracker.utils.LocalService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IssueLocalService extends LocalService<Issue, Long> implements IssueService {
    public IssueLocalService(List<Issue> issues) {
        super(issues);
    }

    @Override
    protected String getIdentifier() {
        return "code";
    }

    @Override
    public List<Issue> getAll() {
        return this.allValues;
    }

    @Override
    public Issue getByCode(Long l) {
        IndexAndValue<Issue> found = this.findByProperty(l);
        return found.value();
    }

    @Override
    public User getById(Long id) {
        return null;
    }

    @Override
    public Issue create(Issue newIssue) {
        try {
            this.findByProperty(newIssue.getCode());
            throw new ResourceAlreadyExistsException("Issue", newIssue.getCode());
        } catch (ResourceNotFoundException e) {
            this.allValues.add(newIssue);
            return newIssue;
        }
    }

    @Override
    public void update(Long code, Issue updatedIssue) {
        IndexAndValue<Issue> found = this.findByProperty(code);
        this.allValues.remove(found.index());
        this.allValues.add(found.index(), updatedIssue);
    }

    @Override
    public void delete(Long code) {
        IndexAndValue<Issue> found = this.findByProperty(code);
        this.allValues.remove(found.index());
    }
}
