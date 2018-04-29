package Service;

import Domain.Nota;
import Domain.Student;
import Repository.Repository;
import Repository.StudentRepository;
import Repository.RepositoryException;
import Utils.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static Utils.Utils.getDate;

public class StudentService extends AbstractService<Integer, Student> {
    private Repository<Integer, Nota> repository_nota;

    public StudentService(Repository<Integer, Student> repository, Repository<Integer, Nota> repository_nota) {
        super(repository);
        this.repository_nota = repository_nota;
    }

    private Integer getStudentNota(int id_student) {
        if(repository_nota.getAll() == null)
            return null;

        for(Nota nota : repository_nota.getAll())
            if(nota.getId_student() == id_student)
                return nota.getId();
        return null;
    }

    @Override
    public Student remove(Integer id) {
        Integer id_nota = getStudentNota(id);

        backupStudentFile(id);

        while(id_nota != null) {
            repository_nota.remove(id_nota);
            id_nota = getStudentNota(id);
        }

        return super.remove(id);
    }

    public void removeMultiple(Iterable<Student> list) {
        List<Integer> idList = new ArrayList<>();
        list.forEach(student -> {
            int id = student.getId();
            idList.add(id);
        });
        idList.forEach(this::remove);
    }

    @Override
    public void removeAll(String check) throws RepositoryException {
        if(check.equals(Utils.deleteCheck)) {
            getAll().forEach(student -> backupStudentFile(student.getId()));
            repository_nota.removeAll();
        }
        super.removeAll(check);
    }

    public List<Student> filtreazaStudentiKeyword(String keyword, List<Student> list) {
        if (keyword.toLowerCase().startsWith("nume:")) {
            String[] fields = keyword.split(":");
            if(fields.length > 1) {
                String nume = fields[1].trim().toLowerCase();
                return Filter.filterAndSorter(list, entity -> entity.getNume().toLowerCase().contains(nume), null);
            }
        }
        else if (keyword.toLowerCase().startsWith("grupa:")) {
            String[] fields = keyword.split(":");
            if(fields.length > 1) {
                String grupa = fields[1].trim().toLowerCase();
                return Filter.filterAndSorter(list, entity -> String.valueOf(entity.getGrupa()).toLowerCase().contains(grupa), null);
            }
        }
        else if (keyword.toLowerCase().startsWith("prof")) {
            String[] fields = keyword.split(":");
            if(fields.length > 1) {
                String prof = fields[1].trim().toLowerCase();
                return Filter.filterAndSorter(list, entity -> String.valueOf(entity.getProf_indrumator()).toLowerCase().contains(prof), null);
            }
        }

        return Filter.filterAndSorter(list,
                entity -> String.valueOf(entity.getId()).contains(keyword) ||
                        String.valueOf(entity.getGrupa()).contains(keyword) ||
                        entity.getProf_indrumator().toLowerCase().contains(keyword.toLowerCase()) ||
                        entity.getEmail().toLowerCase().contains(keyword.toLowerCase()) ||
                        entity.getNume().toLowerCase().contains(keyword.toLowerCase()),
                Comparator.comparing(Student::getNume));
    }

    private void backupStudentFile(int id) {
        Utils.moveFile(Utils.catalogStudentiPath + id + ".txt",
                Utils.catalogStudentiBackupPath + "BKP_" + id + "_" + getDate() + ".txt");
    }

    public Student find(String username) {
        for (Student student : getAll())
            if (student.getUsername().equals(username))
                return student;

        return null;
    }

    public List<Integer> getGrupe() {
        List<Integer> list = new ArrayList<>();

        getAll().forEach(student -> {
            if (! list.contains(student.getGrupa()))
                list.add(student.getGrupa());
        });

        list.sort(Comparator.naturalOrder());

        return list;
    }
}
