package Service;

import Domain.Nota;
import Domain.Student;
import Domain.Tema;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Filter {
    private StudentService student_service;
    private TemaService tema_service;
    private NotaService nota_service;

    public static <E> List<E> filterAndSorter(List<E> lista, Predicate<E> p, Comparator<E> c) {
        if (lista == null || lista.isEmpty()) {
            return new ArrayList<>();
        }

        if(c == null)
            return lista.stream().filter(p).collect(Collectors.toList());
        if(p == null)
            return lista.stream().sorted(c).collect(Collectors.toList());
        return lista.stream().filter(p).sorted(c).collect(Collectors.toList());
    }

    public Filter(StudentService student_service, TemaService tema_service, NotaService nota_service) {
        this.student_service = student_service;
        this.tema_service = tema_service;
        this.nota_service = nota_service;
    }

    public List<Nota> filtreazaNoteSub(double nota) {
        return filterAndSorter(nota_service.getAll(), entity -> entity.getNota() <= nota, Comparator.comparingDouble(Nota::getNota));
    }

    public List<Nota> filtreazaNoteIdTema(int id_tema) {
        return filterAndSorter(nota_service.getAll(), entity -> entity.getId_tema() == id_tema, null);
    }

    public List<Nota> filtreazaNoteSaptamana(int saptamana) {
        return filterAndSorter(nota_service.getAll(), entity -> entity.getSaptamana_predare() == saptamana, Comparator.comparingDouble(Nota::getNota));
    }

    public List<Student> filtreazaStudentiProf(String profesor) {
        return filterAndSorter(student_service.getAll(), entity -> entity.getProf_indrumator().contains(profesor), Comparator.comparingInt(Student::getId));
    }

    public List<Student> filtreazaStudentiGrupa(int grupa) {
        return filterAndSorter(student_service.getAll(), entity -> entity.getGrupa() == grupa, Comparator.comparing(Student::getNume));
    }

    public List<Student> filtreazaStudentiKeyword(String keyword) {
        return filterAndSorter(student_service.getAll(),
                entity -> String.valueOf(entity.getId()).contains(keyword) ||
                        String.valueOf(entity.getGrupa()).contains(keyword) ||
                        entity.getProf_indrumator().toLowerCase().contains(keyword.toLowerCase()) ||
                        entity.getEmail().toLowerCase().contains(keyword.toLowerCase()) ||
                        entity.getNume().toLowerCase().contains(keyword.toLowerCase()),
                Comparator.comparing(Student::getNume));
    }

    public List<Tema> filtreazaTemaDeadline(int deadline) {
        return filterAndSorter(tema_service.getAll(), entity -> entity.getDeadline() == deadline, Comparator.comparing(Tema::getDeadline));
    }

    public List<Tema> sorteazaTemaDeadline() {
        return filterAndSorter(tema_service.getAll(), null, Comparator.comparingInt(Tema::getDeadline));
    }

    public List<Tema> filtreazaTemaKeyword(String keyword) {
        return filterAndSorter(tema_service.getAll(),
                entity -> entity.getDescriere().toLowerCase().contains(keyword.toLowerCase()) ||
                        String.valueOf(entity.getDeadline()).contains(keyword),
                Comparator.comparing(Tema::getId));
    }

    public List<Nota> sorteazaNoteTemaPredare() {
        return filterAndSorter(nota_service.getAll(), null,
                Comparator.comparing(Nota::getId_tema)
                        .thenComparing(Nota::getId_student));
    }
}
