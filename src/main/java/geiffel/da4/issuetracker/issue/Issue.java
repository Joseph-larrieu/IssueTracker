package geiffel.da4.issuetracker.issue;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import geiffel.da4.issuetracker.commentaire.Commentaire;
import geiffel.da4.issuetracker.user.User;
import geiffel.da4.issuetracker.utils.TimestampUtils;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
@Entity
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="code")
public class Issue {

    @Id
    private Long code;
    private String title;
    private String content;
    @ManyToOne
    private User emitter;

    private Timestamp dateCreated;
    private Timestamp dateClosed;

    @OneToMany(mappedBy = "issue")
    private List<Commentaire> commentaires;



    public Issue(Long code, String title, String content, User emitter, Timestamp dateCreated, Timestamp dateClosed) {
        this.code = code;
        this.title = title;
        this.content = content;
        this.emitter = emitter;
        this.dateCreated = dateCreated;
        this.dateClosed = dateClosed;
        this.commentaires = new ArrayList<>();

    }

    public Issue() {

    }


    public void addCommentaire(Commentaire commentaire){
        this.commentaires.add(commentaire);
    }
    public List<Commentaire> getCommentaires() {
        return commentaires;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getEmitter() {
        return emitter;
    }

    public void setEmitter(User emitter) {
        this.emitter = emitter;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Timestamp getDateClosed() {
        return dateClosed;
    }

    public void setDateClosed(Timestamp dateClosed) {
        this.dateClosed = dateClosed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Issue issue = (Issue) o;
        Boolean timestampEquals = TimestampUtils.isEquals(dateCreated, issue.dateCreated);
        timestampEquals = timestampEquals && TimestampUtils.isEquals(dateClosed, issue.dateClosed);
        return Objects.equals(code, issue.code) && Objects.equals(title, issue.title) && Objects.equals(content, issue.content) && timestampEquals;
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, title, content, emitter, dateCreated, dateClosed);
    }


    public Long getId() {
        return this.code;
    }
}
