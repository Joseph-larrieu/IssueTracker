package geiffel.da4.issuetracker.commentaire;
import geiffel.da4.issuetracker.exceptions.ResourceAlreadyExistsException;
import geiffel.da4.issuetracker.exceptions.ResourceNotFoundException;
import geiffel.da4.issuetracker.commentaire.CommentaireRepository;
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
public class CommentaireJPAService implements CommentaireService{

    @Autowired
    private CommentaireRepository commentaireRepository;


    @Override
    public List<Commentaire> getAll() {
        return commentaireRepository.findAll();
    }

    @Override
    public Commentaire getById(Long id) {
        Optional<Commentaire> commentaire = commentaireRepository.findById(id);
        if (commentaire.isPresent()) {
            return commentaire.get();
        } else {
            throw new ResourceNotFoundException("Commentaire", id);
        }
    }

    @Override
    public List<Commentaire> getAllByAuthorId(Long id) {
        return commentaireRepository.getAllByAuthorId(id);
    }

    @Override
    public List<Commentaire> getAllByIssueCode(Long code) {
        return commentaireRepository.getAllByIssueCode(code);
    }

    @Override
    public Commentaire create(Commentaire newCom) {
        if(commentaireRepository.existsById(newCom.getId())){
            throw new ResourceAlreadyExistsException("Commentaire",newCom.getId());
        }
        else {
            return commentaireRepository.save(newCom);
        }
    }

    @Override
    public void update(Long id, Commentaire updatedCom) {
        if(commentaireRepository.existsById(id)){
            throw new ResourceNotFoundException("Commentaire",id);
        }
        else {
            commentaireRepository.save(updatedCom);
        }

    }

    @Override
    public void delete(Long id) {
        commentaireRepository.deleteById(id);
    }
}