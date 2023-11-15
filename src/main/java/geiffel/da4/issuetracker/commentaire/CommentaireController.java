package geiffel.da4.issuetracker.commentaire;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("commentaires")
public class CommentaireController {

    private CommentaireService commentaireService;

    @Autowired
    public CommentaireController(CommentaireService commentaireService) {
        this.commentaireService = commentaireService;
    }


    @GetMapping("")
    public List<Commentaire> getAll() {
        return commentaireService.getAll();
    }

    @GetMapping("{id}")
    public Commentaire getById(@PathVariable Long id) {
        return commentaireService.getById(id);
    }

    @PostMapping("")
    public ResponseEntity create(@RequestBody Commentaire commentaire) {
        Commentaire created = commentaireService.create(commentaire);
        return ResponseEntity.created(URI.create("/commentaires/"+created.getId())).build();
    }

    @PutMapping("{id}")
    public ResponseEntity update(@PathVariable Long id, @RequestBody Commentaire commentaire) {
        commentaireService.update(id, commentaire);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        commentaireService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
