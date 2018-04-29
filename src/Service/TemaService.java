package Service;

import Domain.Tema;
import Domain.Nota;
import Repository.Repository;
import Repository.RepositoryException;
import Repository.TemaRepository;
import Validator.ValidationException;
import Utils.Utils;

import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class TemaService extends AbstractService<Integer, Tema> {
    private Repository<Integer, Nota> repository_nota;

    public TemaService(Repository<Integer, Tema> repository, Repository<Integer, Nota> repository_nota) {
        super(repository);
        this.repository_nota = repository_nota;
    }

    /**
     * Updateaza deadline-ul unei teme din repository
     * @param id - id-ul temei de updatat
     * @param deadline - deadline-ul nou al temei
     * @throws ValidationException daca tema cu acel Id nu exista sau deadline-ul nou
     *         nu este mai mic decat cel vechi.
     */
    public void updateDeadline(int id, int deadline) throws ValidationException{
        Optional<Tema> found = repository.find(id);
        if (!found.isPresent()) throw(new ValidationException("Tema cu acel id nu exista."));

        Tema old = found.get();

        if (old.getDeadline() > deadline) throw(new ValidationException("Noul deadline trebuie sa fie mai mare."));
        if (Utils.getCurrentWeek() >= old.getDeadline()) throw(new ValidationException("S-a depasit data de predare a temei."));

        super.update(new Tema(id, old.getDescriere(), deadline));
    }

    private Integer getTemaNota(int id_tema) {
        if(repository_nota.getAll() == null)
            return null;

        for(Nota nota : repository_nota.getAll())
            if(nota.getId_tema() == id_tema)
                return nota.getId();
        return null;
    }

    @Override
    public Tema remove(Integer id) {
        Integer id_nota = getTemaNota(id);
        while(id_nota != null) {
            repository_nota.remove(id_nota);
            id_nota = getTemaNota(id);
        }

        return super.remove(id);
    }

    public List<Tema> filtreazaTemeKeyword(String keyword, List<Tema> list) {
        if (keyword.toLowerCase().startsWith("id:")) {
            String[] fields = keyword.split(":");
            if(fields.length > 1) {
                String id = fields[1].trim().toLowerCase();
                return Filter.filterAndSorter(list, entity -> String.valueOf(entity.getId()).toLowerCase().contains(id), null);
            }
        }
        else if (keyword.toLowerCase().startsWith("desc")) {
            String[] fields = keyword.split(":");
            if(fields.length > 1) {
                String descriere = fields[1].trim().toLowerCase();
                return Filter.filterAndSorter(list, entity -> entity.getDescriere().toLowerCase().contains(descriere), null);
            }
        }
        else if (keyword.toLowerCase().startsWith("deadline:")) {
            String[] fields = keyword.split(":");
            if(fields.length > 1) {
                String deadline = fields[1].trim().toLowerCase();
                return Filter.filterAndSorter(list, entity -> String.valueOf(entity.getDeadline()).toLowerCase().contains(deadline), null);
            }
        }

        return Filter.filterAndSorter(list,
                entity -> String.valueOf(entity.getId()).contains(keyword) ||
                        entity.getDescriere().toLowerCase().contains(keyword.toLowerCase()) ||
                        String.valueOf(entity.getDeadline()).contains(keyword.toLowerCase()),
                null);
    }
}
