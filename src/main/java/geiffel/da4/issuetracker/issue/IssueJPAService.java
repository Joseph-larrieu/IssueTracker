package geiffel.da4.issuetracker.issue;

import geiffel.da4.issuetracker.exceptions.ResourceAlreadyExistsException;
import geiffel.da4.issuetracker.exceptions.ResourceNotFoundException;
import geiffel.da4.issuetracker.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Qualifier("jpa")
@Primary
public class IssueJPAService implements IssueService {

    @Autowired
    private IssueRepository issueRepository;

    @Override
    public List<Issue> getAll() {
        return issueRepository.findAll();
    }

    @Override
    public Issue getByCode(Long code) {
        Optional<Issue> issueOptional = issueRepository.findById(code);
        if (issueOptional.isPresent()) {
            return issueOptional.get();
        } else {
            throw new ResourceNotFoundException("Issue", code);
        }
    }

    @Override
    public User getById(Long id) {
        return null;
    }

    @Override
    public Issue create(Issue newIssue) {
        Long id = newIssue.getId();
        if(issueRepository.existsById(id)) {
            throw new ResourceAlreadyExistsException("Issue", id);
        }else{
            return issueRepository.save(newIssue);
        }
    }

    @Override
    public void update(Long code, Issue updatedIssue) {
        if(!issueRepository.existsById(code)){
            throw new ResourceAlreadyExistsException("Issue", code);
        }else {
            issueRepository.save(updatedIssue);
        }
    }

    @Override
    public void delete(Long code) {
        issueRepository.deleteById(code);
    }
}