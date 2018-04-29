package UI.Console;

import Domain.Nota;
import Domain.Student;
import Domain.Tema;
import Repository.Repository;
import Repository.StudentFileRepository;
import Repository.TemaFileRepository;
import Repository.NotaFileRepository;
import Service.NotaService;
import Service.TemaService;
import Service.StudentService;

public class Main {

    public static void main(String[] args) {
        Repository<Integer, Student> studentRepository = new StudentFileRepository("./src/Data/Studenti.txt");
        Repository<Integer, Tema> temaRepository = new TemaFileRepository("./src/Data/Teme.txt");
        Repository<Integer, Nota> notaRepository = new NotaFileRepository("./src/Data/Catalog.txt");

        StudentService studentService = new StudentService(studentRepository, notaRepository);
        TemaService temaService = new TemaService(temaRepository, notaRepository);
        NotaService notaService = new NotaService(notaRepository, studentRepository, temaRepository);

        Console console = new Console(studentService, temaService, notaService);
        console.runUI();
    }
}