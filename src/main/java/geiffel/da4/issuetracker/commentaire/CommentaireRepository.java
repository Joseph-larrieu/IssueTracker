package geiffel.da4.issuetracker.commentaire;

import geiffel.da4.issuetracker.issue.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentaireRepository extends JpaRepository<Commentaire, Long> {
    @Query(nativeQuery = true, value="SELECT * FROM Commentaire WHERE author_id = :id")
    public List<Commentaire> getAllByAuthorId(@Param("id") Long id);

    @Query(nativeQuery = true, value="SELECT * FROM Commentaire WHERE issue_code = :code")
    public List<Commentaire> getAllByIssueCode(@Param("code") Long code);
}
