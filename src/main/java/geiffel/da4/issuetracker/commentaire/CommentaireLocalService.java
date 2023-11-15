package geiffel.da4.issuetracker.commentaire;

import geiffel.da4.issuetracker.exceptions.ResourceAlreadyExistsException;
import geiffel.da4.issuetracker.exceptions.ResourceNotFoundException;
import geiffel.da4.issuetracker.user.User;
import geiffel.da4.issuetracker.utils.LocalService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentaireLocalService extends LocalService<Commentaire, Long>
        implements CommentaireService {
    public CommentaireLocalService(List<Commentaire> commentaires) {
        super(commentaires);
    }

    @Override
    protected String getIdentifier() {
        return "id";
    }

    @Override
    public List<Commentaire> getAll() {
        return super.getAll();
    }

    @Override
    public Commentaire getById(Long id) {
        return this.getByIdentifier(id);
    }

    @Override
    public List<Commentaire> getAllByAuthorId(Long id) {
        return allValues.stream()
                .filter(commentaire -> commentaire.getAuthorId()==id)
                .toList();
    }
    @Override
    public List<Commentaire> getAllByIssueCode(Long code) {
        return this.allValues.stream()
                .filter(commentaire -> commentaire.getIssueCode()==code)
                .toList();
    }

    @Override
    public Commentaire create(Commentaire commentaire) {
        try {
            this.findByProperty(commentaire.getId());
            throw new ResourceAlreadyExistsException("Commentaire", commentaire.getId());
        } catch (ResourceNotFoundException e) {
            this.allValues.add(commentaire);
            return commentaire;
        }
    }

    @Override
    public void update(Long id, Commentaire toUpdate) {
        IndexAndValue<Commentaire> found = this.findByProperty(id);
        this.allValues.remove(found.index());
        this.allValues.add(found.index(), toUpdate);
    }

    @Override
    public void delete(Long id) {
        IndexAndValue<Commentaire> found = this.findByProperty(id);
        this.allValues.remove(found.index());
    }
}
