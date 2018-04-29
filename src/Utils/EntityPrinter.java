package Utils;

import Domain.Nota;
import Domain.Student;
import Domain.Tema;

public class EntityPrinter {
    public static void printStudents(Iterable<Student> lista) {
        if (lista != null) {
            System.out.print(new String(new char[35]).replace("\0", " "));
            System.out.println("LISTA STUDENTILOR\n");

            System.out.println(new String(new char[105]).replace("\0", "═"));
            System.out.printf("║ %6s │ %-30s │ %3s │ %-30s │ %-20s ║\n", "NR MAT", "NUME", "GR", "E-MAIL", "PROFESOR INDRUMATOR");
            System.out.println(new String(new char[105]).replace("\0", "═"));
            lista.forEach((student) ->
                    System.out.printf("║ %6d │ %-30s │ %3d │ %-30s │ %-20s ║\n", student.getId(),
                            student.getNume(), student.getGrupa(), student.getEmail(), student.getProf_indrumator())
            );
            System.out.println(new String(new char[105]).replace("\0", "═"));
        }
    }

    public static void printTeme(Iterable<Tema> lista) {
        if(lista != null) {
            System.out.print(new String(new char[24]).replace("\0", " "));
            System.out.println("LISTA TEMELOR\n");

            System.out.println(new String(new char[65]).replace("\0", "═"));
            System.out.printf("║ %6s │ %-40s │ %-9s ║\n", "ID", "DESCRIERE", "DEADLINE");
            System.out.println(new String(new char[65]).replace("\0", "═"));
            lista.forEach(tema -> System.out.printf("║ %6d │ %-40s │ %-9s ║\n", tema.getId(),
                                    tema.getDescriere(), "Sapt. " + tema.getDeadline()));
            System.out.println(new String(new char[65]).replace("\0", "═"));
        }
    }

    public static void printNote(Iterable<Nota> lista) {
        if(lista != null) {
            System.out.print(new String(new char[19]).replace("\0", " "));
            System.out.println("LISTA NOTELOR\n");

            System.out.println(new String(new char[50]).replace("\0", "═"));
            System.out.printf("║ %7s │ %7s │ %7s │ %4s │ %-9s ║\n", "ID NOTA", "ID STUD", "ID TEMA", "NOTA", "SAPT PRED");
            System.out.println(new String(new char[50]).replace("\0", "═"));
            lista.forEach(nota -> System.out.printf("║ %7d │ %7d │ %7d │ %4d │ %-9s ║\n", nota.getId(),
                                    nota.getId_student(), nota.getId_tema(), nota.getNota(),
                                    "Sapt. " + String.valueOf(nota.getSaptamana_predare())));
            System.out.println(new String(new char[50]).replace("\0", "═"));
        }
    }
}
