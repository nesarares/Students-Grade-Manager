package Repository;

import Domain.Nota;
import Validator.NotaValidator;
import Validator.ValidationException;

public class NotaFileRepository extends AbstractFileRepository<Integer, Nota> {
    public NotaFileRepository(String filename) {
        super(new NotaValidator(), filename);
    }

    @Override
    protected Nota createEntity(String[] fields) {
        int id = Integer.parseInt(fields[0]);
        int id_student = Integer.parseInt(fields[1]);
        int id_tema = Integer.parseInt(fields[2]);
        double nota = Double.parseDouble(fields[3]);
        int saptamana_predare = Integer.parseInt(fields[4]);

        Nota object = new Nota(id, id_student, id_tema, nota, saptamana_predare);

        return object;
    }

    @Override
    public void add(Nota entity) throws ValidationException, RepositoryException {
        vali.validate(entity);
        if (find(entity.getId()).isPresent())
            throw (new RepositoryException("Elementul cu id-ul " + entity.getId() + " exista deja in repository."));

        if(getAll() != null)
            for(Nota nota : getAll())
                if(nota.getId_student() == entity.getId_student() && nota.getId_tema() == entity.getId_tema())
                    throw (new RepositoryException("Studentul " + entity.getId_student() + " are deja o nota la tema " + entity.getId_tema() + "."));

        super.add(entity);
    }
}
