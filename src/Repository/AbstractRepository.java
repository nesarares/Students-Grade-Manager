package Repository;

import Domain.hasId;
import Validator.Validator;
import Validator.ValidationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class AbstractRepository<Id, E extends hasId<Id>> implements Repository<Id, E> {
    protected Map<Id, E> storage = new HashMap<>();
    protected Validator<E> vali;

    public AbstractRepository(Validator<E> vali) {
        this.vali = vali;
    }

    @Override
    public long size() {
        return storage.size();
    }

    @Override
    public void add(E entity) throws ValidationException, RepositoryException {
        vali.validate(entity);

        Optional<E> found = find(entity.getId());

        if(found.isPresent())
            throw(new RepositoryException("Elementul cu id-ul " + entity.getId() + " exista deja in repository."));

        storage.put(entity.getId(), entity);
    }

    @Override
    public E remove(Id id) {
        return storage.remove(id);
    }

    @Override
    public void removeAll() {
        storage.clear();
    }

    @Override
    public E update(E entity) throws ValidationException {
        vali.validate(entity);
        return storage.replace(entity.getId(), entity);
    }

    @Override
    public Optional<E> find(Id id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public ArrayList<E> getAll() {
        if(storage.size() == 0) return null;
        return new ArrayList<>(storage.values());
    }
}
