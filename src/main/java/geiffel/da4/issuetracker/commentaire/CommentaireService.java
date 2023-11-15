package geiffel.da4.issuetracker.commentaire;

import java.util.Collection;
import java.util.List;

public interface CommentaireService {
    List<Commentaire> getAll();

    Commentaire getById(Long id);

    List<Commentaire> getAllByAuthorId(Long id);

    List<Commentaire> getAllByIssueCode(Long code);

    Commentaire create(Commentaire commentaire6);

    void update(Long id, Commentaire toUpdate1);

    void delete(Long id);
}
