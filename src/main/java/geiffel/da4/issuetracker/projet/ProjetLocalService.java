package geiffel.da4.issuetracker.projet;

import java.util.List;

import geiffel.da4.issuetracker.exceptions.ResourceAlreadyExistsException;
import geiffel.da4.issuetracker.exceptions.ResourceNotFoundException;
import geiffel.da4.issuetracker.user.User;
import geiffel.da4.issuetracker.utils.LocalService;
import org.springframework.stereotype.Service;

@Service
public class ProjetLocalService extends LocalService<Projet,Long> implements ProjetService {

    public ProjetLocalService(List<Projet> projets) {
        super(projets);
    }

    @Override
    protected String getIdentifier() {
        return "id";
    }

    @Override
    public List<Projet> getAll() {
        return super.getAll();
    }

    @Override
    public Projet getById(Long id) {
        return this.getByIdentifier(id);
    }

    @Override
    public Projet create(Projet newProjet) throws ResourceAlreadyExistsException {
        try {
            this.findById(newProjet.getId());
            throw new ResourceAlreadyExistsException("Projet", newProjet.getId());
        } catch (ResourceNotFoundException e) {
            this.allValues.add(newProjet);
            return newProjet;
        }
    }

    private IndexAndValue<Projet> findById(Long id) {
        return super.findByProperty(id);
    }

    public Projet update(Long id, Projet updateProjet) {
        IndexAndValue<Projet> found = this.findById(id);
        this.allValues.remove(found.index());
        this.allValues.add(found.index(), updateProjet);
        return updateProjet;
    }

    public void delete(Long id) {
        IndexAndValue<Projet> found = this.findById(id);
        this.allValues.remove(found.value());
    }
}

