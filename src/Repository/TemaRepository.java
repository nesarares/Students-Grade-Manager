package Repository;

import Domain.Tema;
import Validator.TemaValidator;

public class TemaRepository extends AbstractRepository<Integer, Tema> {
    public TemaRepository() {
        super(new TemaValidator());
    }
}
