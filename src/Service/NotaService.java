package Service;

import Domain.Nota;
import Domain.Student;
import Domain.Tema;
import Repository.Repository;
import Repository.RepositoryException;
import Validator.NotaValidator;
import Validator.ValidationException;
import Utils.Utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static Utils.Utils.getDate;

public class NotaService extends AbstractService<Integer, Nota> {
    private Repository<Integer, Student> repository_student;
    private Repository<Integer, Tema> repository_tema;

    private enum Action {
        ADD, UPDATE
    }

    public NotaService(Repository<Integer, Nota> repository,
                       Repository<Integer, Student> repository_student,
                       Repository<Integer, Tema> repository_tema) {
        super(repository);
        this.repository_student = repository_student;
        this.repository_tema = repository_tema;
    }

    private void diminuareNota(Nota entity) {
        Optional<Tema> found = repository_tema.find(entity.getId_tema());

        if(found.isPresent()) {
            int deadline_tema = found.get().getDeadline();
            if (entity.getSaptamana_predare() > deadline_tema) {
                int weeks_late = entity.getSaptamana_predare() - deadline_tema;
                entity.setNota(entity.getNota() - 2 * weeks_late);
                if (weeks_late > 2 || entity.getNota() < 0) entity.setNota(1);
            }
        }
    }

    @Override
    public void add(Nota entity) throws ValidationException, RepositoryException {
        Random randomGenerator = new Random();
        int id = randomGenerator.nextInt(10000);

        while(repository.find(id).isPresent()) id = randomGenerator.nextInt(10000);
        entity.setId(id);

        NotaValidator.validate(entity, repository_student, repository_tema);
        diminuareNota(entity);

        super.add(entity);
    }

    public void add(Nota entity, String observatii) throws ValidationException, RepositoryException {
        add(entity);

        Optional<Tema> found = repository_tema.find(entity.getId_tema());
        if (found.isPresent()) {
            int deadline_tema = found.get().getDeadline();
            if (entity.getSaptamana_predare() > deadline_tema)
                observatii = "Nota a fost depunctata deoarece a depasit deadline-ul. " + observatii;

            if (observatii.equals("")) observatii = "-";
            appendToStudentFile(entity, observatii, Action.ADD);
        }
    }

    @Override
    public Nota update(Nota entity) throws ValidationException {
        NotaValidator.validate(entity, repository_student, repository_tema);

        diminuareNota(entity);
        Nota old = findByIds(entity.getId_student(), entity.getId_tema());

        if(old.getNota() >= entity.getNota())
            return null;
        entity.setId(old.getId());

        return super.update(entity);
    }

    @Override
    public Nota remove(Integer integer) {
        return super.remove(integer);
    }

    @Override
    public void removeAll(String check) throws RepositoryException {
        super.removeAll(check);
    }

    public Nota update(Nota entity, String observatii) throws ValidationException {
        Nota returned = update(entity);
        if(returned == null)
            return null;

        Optional<Tema> found = repository_tema.find(entity.getId_tema());
        if(found.isPresent()) {
            int deadline_tema = found.get().getDeadline();
            if (entity.getSaptamana_predare() > deadline_tema)
                observatii = "Nota a fost depunctata deoarece a depasit deadline-ul. " + observatii;

            if (observatii.equals("")) observatii = "-";
            appendToStudentFile(entity, observatii, Action.UPDATE);
        }
        return returned;
    }

    public Nota findByIds(int id_student, int id_tema) {
        for (Nota nota : this.getAll())
            if (nota.getId_student() == id_student && nota.getId_tema() == id_tema)
                return nota;
        return null;
    }

    private void appendToStudentFile(Nota nota, String observatii, Action action) {
        String filename = Utils.catalogStudentiPath + String.valueOf(nota.getId_student()) + ".txt";
        Optional<Tema> found = repository_tema.find(nota.getId_tema());
        if(found.isPresent()) {
            int deadline = found.get().getDeadline();

            try (BufferedWriter out = new BufferedWriter(new FileWriter(filename, true))) {
                if (action == Action.ADD) out.write("Adaugare nota, ");
                if (action == Action.UPDATE) out.write("Modificare nota, ");
                out.write(nota.getId_tema() + ", "
                        + nota.getNota() + ", "
                        + deadline + ", "
                        + nota.getSaptamana_predare() + ", "
                        + observatii
                        + "\n");
            } catch (IOException e) {
                System.out.println("Input-Output error.");
            }
        }
    }

    public Optional<Student> findStudent(Nota nota) {
        return repository_student.find(nota.getId_student());
    }

    public Optional<Tema> findTema(Nota nota) {
        return repository_tema.find(nota.getId_tema());
    }

    public List<Nota> filtreazaNoteKeyword(String keyword, List<Nota> list) {
        if (list == null)
            return new ArrayList<>();

        final String keywordl = keyword.toLowerCase();
        Comparator<Nota> comparator = Comparator.comparing(o -> findStudent(o).get().getNume());
        comparator = comparator.thenComparing(o -> findTema(o).get().getDescriere());
        comparator = comparator.thenComparing(Nota::getSaptamana_predare);
//        Comparator<Nota> comparator = null;

        if (keywordl.startsWith("student")) {
            String[] fields = keyword.split(":");
            if(fields.length > 1) {
                String student = fields[1].trim().toLowerCase();
                return Filter.filterAndSorter(list, entity -> findStudent(entity).get().getNume().toLowerCase().contains(student), comparator);
            }
        }
        else if (keyword.toLowerCase().startsWith("tema")) {
            String[] fields = keyword.split(":");
            if(fields.length > 1) {
                String descriere = fields[1].trim().toLowerCase();
                return Filter.filterAndSorter(list, entity -> findTema(entity).get().getDescriere().toLowerCase().contains(descriere), comparator);
            }
        }
        else if (keyword.toLowerCase().startsWith("nota")) {
            String[] fields = keyword.split(":");
            if(fields.length > 1) {
                String nota = fields[1].trim().toLowerCase();
                return Filter.filterAndSorter(list, entity -> String.valueOf(entity.getNota()).equals(nota), comparator);
            }
        }
        else if (keyword.toLowerCase().startsWith("sapt")) {
            String[] fields = keyword.split(":");
            if(fields.length > 1) {
                String saptamana = fields[1].trim().toLowerCase();
                return Filter.filterAndSorter(list, entity -> String.valueOf(entity.getSaptamana_predare()).equals(saptamana), comparator);
            }
        }

        return Filter.filterAndSorter(list,
                entity -> findStudent(entity).get().getNume().toLowerCase().contains(keywordl) ||
                            findTema(entity).get().getDescriere().toLowerCase().contains(keywordl) ||
                            String.valueOf(entity.getNota()).equals(keywordl) ||
                            String.valueOf(entity.getSaptamana_predare()).equals(keywordl),
                comparator);
    }

    public double getMedia(String student) {
        double media = 0;
        int nrNote = 0;

        for ( Nota nota : filtreazaNoteKeyword("student: " + student, repository.getAll())) {
            media += nota.getNota();
            nrNote++;
        }

        return media / nrNote;
    }

    public Double getMedia(int grupa) {
        double media = 0;
        int nrNote = 0;

        for ( Nota nota : getAll()) {
            if (findStudent(nota).get().getGrupa() == grupa) {
                media += nota.getNota();
                nrNote++;
            }
        }

        if (nrNote == 0)
            return 0.0;

        return media / nrNote;
    }
}
