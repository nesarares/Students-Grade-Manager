package Repository;

import Domain.hasId;
import Validator.ValidationException;

import java.util.ArrayList;
import java.util.Optional;

public interface Repository <Id, E extends hasId<Id>> {

    /**
     * @return numarul de elemente din Repository
     */
    long size();

    /**
     * Valideaza si adauga un element in repository.
     * @param entity - entitatea de adaugat
     * @throws ValidationException - daca entitatea nu e valida
     * @throws RepositoryException - daca exista in repository un element cu acel id
     */
    void add(E entity) throws ValidationException, RepositoryException;

    /**
     * Sterge un element din repository si il returneaza
     * @param id - id-ul elementului de sters
     * @return  null daca nu exista elementul cu acel id
     *          sau elementul care s-a sters.
     */
    E remove(Id id);

    /**
     * Sterge toate elementele din repository
     */
    void removeAll();

    /**
     * Updateaza o valoare din repository cu una noua
     * @param entity - entitatea de updatat
     * @return null daca nu exista elementul cu acel id in repository
     *         sau elementul vechi de pe acea pozitie
     * @throws ValidationException - daca entitatea nu e valida
     */
    E update(E entity) throws ValidationException;

    /**
     * Cauta un element in Repository si il returneaza daca exista.
     * @param id - id-ul elementului de returnat
     * @return E - elementul de returnat
     *         sau null - daca nu exista elementul
     */
    Optional<E> find(Id id);

    /**
     * Returneaza toate elementele din repository
     * @return ArrayList cu toate valorile / elementele
     *         null daca nu exista elemente
     */
    ArrayList<E> getAll();
}
