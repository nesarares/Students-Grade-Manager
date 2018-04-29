package Service;

import Domain.hasId;
import Repository.AbstractRepository;
import Repository.Repository;
import Repository.RepositoryException;
import Validator.ValidationException;

import java.util.ArrayList;

public interface Service <Id, E extends hasId<Id>> {

    /**
     * @return numarul de elemente din storage
     */
    long size();

    /**
     * Adauga un element in storage.
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
     * Sterge toate entitatile din repository
     * @param check - parametru pentru siguranta stergerii
     * @throws RepositoryException - daca check nu e bun
     */
    void removeAll(String check) throws RepositoryException;

    /**
     * Updateaza o valoare din storage cu una noua
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
    E find(Id id) throws RepositoryException;

    /**
     * Returneaza toate elementele din storage
     * @return obiect iterabil cu toate elementele
     */
    ArrayList<E> getAll();
}
