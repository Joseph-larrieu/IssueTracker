package geiffel.da4.issuetracker.projet;

import java.util.List;

public interface ProjetService {
    List<Projet> getAll();

    Projet getById(Long id);

    Projet create(Projet newProjet);

    void delete(Long id);

    Projet update(Long id, Projet projet);
}
