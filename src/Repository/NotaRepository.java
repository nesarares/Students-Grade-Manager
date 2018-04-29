package Repository;

import Domain.Nota;
import Validator.NotaValidator;
import Validator.ValidationException;

public class NotaRepository extends AbstractRepository<Integer, Nota> {
    public NotaRepository() {
        super(new NotaValidator());
    }

    @Override
    public void add(Nota entity) throws ValidationException, RepositoryException {
        vali.validate(entity);
        if (find(entity.getId()) != null )
            throw (new RepositoryException("Elementul cu id-ul " + entity.getId() + " exista deja in repository."));

        if(getAll() != null)
            for(Nota nota : getAll()) if(nota.getId_student() == entity.getId_student() &&
                    nota.getId_tema() == entity.getId_tema())
                throw (new RepositoryException("Studentul " + entity.getId_student() +
                        " are deja o nota la tema " + entity.getId_tema() + "."));

        storage.put(entity.getId(), entity);
    }
}
