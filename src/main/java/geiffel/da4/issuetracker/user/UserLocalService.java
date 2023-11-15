package geiffel.da4.issuetracker.user;


import geiffel.da4.issuetracker.exceptions.ResourceAlreadyExistsException;
import geiffel.da4.issuetracker.exceptions.ResourceNotFoundException;
import geiffel.da4.issuetracker.utils.LocalService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Qualifier("local")
public class UserLocalService extends LocalService<User, Long> implements UserService {


    public UserLocalService() {
        super();
        this.allValues.add(new User(1L, "Machin", Fonction.DEVELOPPER));
    }

    public UserLocalService(List<User> users) {
        super(users);
    }

    @Override
    public List<User> getAll() {
        return super.getAll();
    }

    @Override
    public User getById(Long id) {
        return this.getByIdentifier(id);
    }

    @Override
    public User create(User toCreate) throws ResourceAlreadyExistsException {
        try {
            this.findById(toCreate.getId());
            throw new ResourceAlreadyExistsException("User", toCreate.getId());
        } catch (ResourceNotFoundException e) {
            this.allValues.add(toCreate);
            return toCreate;
        }
    }

    @Override
    public void update(Long id, User updatedUser) throws ResourceNotFoundException {
        IndexAndValue<User> found = this.findById(id);
        this.allValues.remove(found.index());
        this.allValues.add(found.index(), updatedUser);
    }

    @Override
    public void delete(Long id) throws ResourceNotFoundException {
        IndexAndValue<User> found = this.findById(id);
        this.allValues.remove(found.value());
    }

    @Override
    public String getIdentifier() {
        return "id";
    }

    public IndexAndValue<User> findById(Long id) {
        return super.findByProperty(id);
    }
}
