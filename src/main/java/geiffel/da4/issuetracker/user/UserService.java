package geiffel.da4.issuetracker.user;

import geiffel.da4.issuetracker.exceptions.ResourceAlreadyExistsException;
import geiffel.da4.issuetracker.exceptions.ResourceNotFoundException;

import java.util.List;

public interface UserService {

    List<User> getAll();

    User getById(Long id);

    User create(User newUser) throws ResourceAlreadyExistsException;

    void update(Long id, User updatedUser) throws ResourceNotFoundException;

    void delete(Long id) throws ResourceNotFoundException;

}
