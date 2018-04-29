package Service;

import Domain.hasId;
import Repository.Repository;
import Repository.RepositoryException;
import Utils.*;
import Validator.ValidationException;

import java.util.ArrayList;
import java.util.Optional;

public class AbstractService<Id, E extends hasId<Id>> extends AbstractObservable<E> implements Service<Id, E> {
    protected Repository<Id, E> repository;

    public AbstractService(Repository<Id, E> repository) {
        this.repository = repository;
    }

    @Override
    public long size() {
        return repository.size();
    }

    @Override
    public void add(E entity) throws ValidationException, RepositoryException {
        repository.add(entity);
        notifyObservers(createEvent(ListEventType.ADD, repository.getAll(), entity));
    }

    @Override
    public E remove(Id id) {
        E removed = repository.remove(id);
        if (removed != null)
            notifyObservers(createEvent(ListEventType.REMOVE, repository.getAll(), removed));
        return removed;
    }

    @Override
    public void removeAll(String check) throws RepositoryException {
        if(check.equals(Utils.deleteCheck)) {
            repository.removeAll();
            notifyObservers(createEvent(ListEventType.REMOVE, repository.getAll(), null));
        }
        else
            throw(new RepositoryException("Cuvant cheie invalid, stergerea nu a fost efectuata."));
    }

    @Override
    public E update(E entity) throws ValidationException {
        E updated = repository.update(entity);
        if (updated != null) {
            notifyObservers(createEvent(ListEventType.UPDATE, repository.getAll(), updated));
        }
        return updated;
    }

    @Override
    public E find(Id id) throws RepositoryException {
        Optional<E> found = repository.find(id);
        if (found.isPresent())
            return found.get();
        else
            throw(new RepositoryException("Nu exista entitatea cu acel id."));
    }

    @Override
    public ArrayList<E> getAll() {
        return repository.getAll();
    }
}
