package UI.Console;

import Domain.Nota;
import Domain.Student;
import Domain.Tema;
import Repository.RepositoryException;
import Service.*;
import UI.Console.Menu.Command;
import UI.Console.Menu.MenuCommand;
import Utils.Utils;
import Utils.EntityPrinter;
import Validator.ValidationException;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Console {

    private StudentService studentService;
    private TemaService temaService;
    private NotaService notaService;

    private MenuCommand main_menu;
    private Scanner in = new Scanner(System.in);

    public Console(StudentService studentService, TemaService temaService, NotaService notaService) {
        this.studentService = studentService;
        this.temaService = temaService;
        this.notaService = notaService;
    }

    private class DeleteAllEntitiesCommand implements Command {
        private Service service;

        public DeleteAllEntitiesCommand(Service service) {
            this.service = service;
        }

        @Override
        public void execute() {
            String check;

            System.out.println("\nSunteti sigur ca vreti sa stergeti toate inregistrarile?");
            System.out.print("Pentru a sterge introduceti " + Utils.deleteCheck + ": ");
            check = in.nextLine();

            try {
                service.removeAll(check);
            } catch (RepositoryException e) {
                System.out.println("\n" + e.getMessage());
            }
        }
    }

    private class StudentMenu extends MenuCommand {
        MenuCommand parent;

        private class AddStudentCommand implements Command {
            @Override
            public void execute() {
                int id;
                String nume;
                int grupa;
                String email;
                String prof_indrumator;

                try {
                    System.out.println("ADAUGARE STUDENT");
                    System.out.println("----------------\n");

                    System.out.print("Numar matricol: ");
                    id = Integer.parseInt(in.nextLine());
                    System.out.print("Nume: ");
                    nume = in.nextLine();
                    System.out.print("Grupa: ");
                    grupa = Integer.parseInt(in.nextLine());
                    System.out.print("E-mail: ");
                    email = in.nextLine();
                    System.out.print("Profesor Indrumator: ");
                    prof_indrumator = in.nextLine();

                    studentService.add(new Student(id, nume, grupa, email, prof_indrumator));

                    System.out.println("\nStudent adaugat cu succes!");

                } catch (InputMismatchException | NumberFormatException ex){
                    System.out.println("\nDate introduse incorect.");
                } catch (ValidationException | RepositoryException ex) {
                    System.out.println("\n" + ex.getMessage());
                }
            }
        }

        private class DeleteStudentCommand implements Command {
            @Override
            public void execute() {
                int id;

                System.out.println("STERGERE STUDENT");
                System.out.println("----------------\n");

                System.out.print("Numar matricol: ");
                id = Integer.parseInt(in.nextLine());

                if (studentService.remove(id) != null) {
                    System.out.println("\nStudentul a fost sters.");
                } else {
                    System.out.println("\nStudentul cu acel numar matricol nu exista.");
                }
            }
        }

        private class UpdateStudentCommand implements Command {
            @Override
            public void execute() {
                int id;
                String nume;
                int grupa;
                String email;
                String prof_indrumator;

                try {
                    System.out.println("MODIFICARE STUDENT");
                    System.out.println("----------------\n");

                    System.out.print("Numar matricol al studentului de modificat: ");
                    id = Integer.parseInt(in.nextLine());
                    System.out.print("Nume: ");
                    nume = in.nextLine();
                    System.out.print("Grupa: ");
                    grupa = Integer.parseInt(in.nextLine());
                    System.out.print("E-mail: ");
                    email = in.nextLine();
                    System.out.print("Profesor Indrumator: ");
                    prof_indrumator = in.nextLine();

                    if (studentService.update(new Student(id, nume, grupa, email, prof_indrumator)) != null) {
                        System.out.println("\nStudent modificat cu succes!");
                    } else {
                        System.out.println("\nStudentul cu acel numar matricol nu exista.");
                    }

                } catch (InputMismatchException | NumberFormatException ex){
                    System.out.println("\nDate introduse incorect.");
                } catch (ValidationException ex) {
                    System.out.println("\n" + ex.getMessage());
                }
            }
        }

        private class FindStudentCommand implements Command {
            @Override
            public void execute() {
                int id;
                Student student;

                System.out.println("CAUTARE STUDENT");
                System.out.println("----------------\n");

                System.out.print("Numar matricol: ");
                id = Integer.parseInt(in.nextLine());

                try {
                    student = studentService.find(id);
                    System.out.println("\n" + student);
                } catch (RepositoryException e) {
                    System.out.println("\n" + e.getMessage());
                }
            }
        }

        private class ShowStudentListCommand implements Command {
            @Override
            public void execute() {
                EntityPrinter.printStudents(studentService.getAll());
            }
        }

        private void createMenu() {
            addCommand("0. Exit", parent);
            addCommand("1. Adaugare student", new AddStudentCommand());
            addCommand("2. Stergere student", new DeleteStudentCommand());
            addCommand("3. Stergere toti studentii", new DeleteAllEntitiesCommand(studentService));
            addCommand("4. Modificare student", new UpdateStudentCommand());
            addCommand("5. Cautare student", new FindStudentCommand());
            addCommand("6. Afisare lista studenti", new ShowStudentListCommand());
        }

        public StudentMenu(String menu_name, MenuCommand parent) {
            super(menu_name);
            this.parent = parent;
            createMenu();
        }
    }

    private class TemaMenu extends MenuCommand {
        MenuCommand parent;

        private class AddTemaCommand implements Command {
            @Override
            public void execute() {
                int id;
                String descriere;
                int deadline;

                try {
                    System.out.println("ADAUGARE TEMA");
                    System.out.println("-------------\n");

                    System.out.print("Id tema: ");
                    id = Integer.parseInt(in.nextLine());
                    System.out.print("Descriere: ");
                    descriere = in.nextLine();
                    System.out.print("Deadline: ");
                    deadline = Integer.parseInt(in.nextLine());

                    temaService.add(new Tema(id, descriere, deadline));

                    System.out.println("\nTema adaugata cu succes!");

                } catch (InputMismatchException | NumberFormatException ex){
                    System.out.println("\nDate introduse incorect.");
                } catch (ValidationException | RepositoryException ex) {
                    System.out.println("\n" + ex.getMessage());
                }
            }
        }

        private class UpdateDeadlineTemaCommand implements Command {
            @Override
            public void execute() {
                int id;
                int deadline;

                try {
                    System.out.println("MODIFICARE DEADLINE TEMA");
                    System.out.println("------------------------\n");

                    System.out.print("Id tema: ");
                    id = Integer.parseInt(in.nextLine());
                    System.out.print("Deadline: ");
                    deadline = Integer.parseInt(in.nextLine());

                    temaService.updateDeadline(id, deadline);

                    System.out.println("\nTema modificata cu succes!");

                } catch (InputMismatchException | NumberFormatException ex){
                    System.out.println("\nDate introduse incorect.");
                } catch (ValidationException ex) {
                    System.out.println("\n" + ex.getMessage());
                }
            }
        }

        private class DeleteTemaCommand implements Command {
            @Override
            public void execute() {
                int id;

                System.out.println("STERGERE TEMA");
                System.out.println("-------------\n");

                System.out.print("Id tema: ");
                id = Integer.parseInt(in.nextLine());

                if (temaService.remove(id) != null) {
                    System.out.println("\nTema a fost stearsa.");
                } else {
                    System.out.println("\nTema cu acel numar id nu exista.");
                }
            }
        }

        private class ShowTemaListCommand implements Command {
            @Override
            public void execute() {
                EntityPrinter.printTeme(temaService.getAll());
            }
        }

        private void createMenu() {
            addCommand("0. Exit", parent);
            addCommand("1. Adaugare tema", new AddTemaCommand());
            addCommand("2. Modificare deadline tema existenta", new UpdateDeadlineTemaCommand());
            addCommand("3. Stergere tema", new DeleteTemaCommand());
            addCommand("4. Stergere toate teme", new DeleteAllEntitiesCommand(temaService));
            addCommand("5. Afisare teme de laborator", new ShowTemaListCommand());
        }

        public TemaMenu(String menu_name, MenuCommand parent) {
            super(menu_name);
            this.parent = parent;
            createMenu();
        }
    }

    private class NotaMenu extends MenuCommand {
        private MenuCommand parent;

        private class AddNotaCommand implements Command {
            @Override
            public void execute() {
                int id_student, id_tema, nota, saptamana_predare;
                String input, observatii;

                try {
                    System.out.println("ADAUGARE NOTA");
                    System.out.println("-------------\n");

                    System.out.print("Id student: ");
                    id_student = Integer.parseInt(in.nextLine());
                    System.out.print("Id tema: ");
                    id_tema = Integer.parseInt(in.nextLine());
                    System.out.print("Nota: ");
                    nota = Integer.parseInt(in.nextLine());
                    System.out.print("Saptamana de predare (c = saptamana curenta): ");
                    input = in.nextLine();
                    if(input.equals("c")) saptamana_predare = Utils.getCurrentWeek();
                    else saptamana_predare = Integer.parseInt(input);
                    System.out.print("Observatii: ");
                    observatii = in.nextLine();

                    notaService.add(new Nota(0, id_student, id_tema, nota, saptamana_predare), observatii);

                    System.out.println("\nNota adaugata cu succes!");

                } catch (InputMismatchException | NumberFormatException ex){
                    System.out.println("\nDate introduse incorect.");
                } catch (ValidationException | RepositoryException ex) {
                    System.out.println("\nEroare: " + ex.getMessage());
                }
            }
        }

        private class UpdateNotaCommand implements Command {
            @Override
            public void execute() {
                int id_student, id_tema, nota, saptamana_predare;
                String input, observatii;

                try {
                    System.out.println("MODIFICARE NOTA");
                    System.out.println("---------------\n");

                    System.out.print("Id student: ");
                    id_student = Integer.parseInt(in.nextLine());
                    System.out.print("Id tema: ");
                    id_tema = Integer.parseInt(in.nextLine());
                    System.out.print("Nota: ");
                    nota = Integer.parseInt(in.nextLine());
                    System.out.print("Saptamana de predare (c = saptamana curenta): ");
                    input = in.nextLine();
                    if(input.equals("c")) saptamana_predare = Utils.getCurrentWeek();
                    else saptamana_predare = Integer.parseInt(input);
                    System.out.print("Observatii: ");
                    observatii = in.nextLine();

                    if(notaService.update(new Nota(0, id_student, id_tema, nota, saptamana_predare), observatii) != null)
                        System.out.println("\nNota modificata cu succes!");
                    else
                        System.out.println("\nS-a pastrat nota initiala.");

                } catch (InputMismatchException | NumberFormatException ex){
                    System.out.println("\nDate introduse incorect.");
                } catch (ValidationException ex) {
                    System.out.println("\nEroare: " + ex.getMessage());
                }
            }
        }

        private class DeleteNotaCommand implements Command {
            @Override
            public void execute() {
                int id;

                System.out.println("STERGERE NOTA");
                System.out.println("-------------\n");

                System.out.print("Id nota: ");
                id = Integer.parseInt(in.nextLine());

                if (notaService.remove(id) != null) {
                    System.out.println("\nNota a fost stearsa.");
                } else {
                    System.out.println("\nNota cu acel numar id nu exista.");
                }
            }
        }

        private class ShowNotaListCommand implements Command {
            @Override
            public void execute() {
                EntityPrinter.printNote(notaService.getAll());
            }
        }

        private void createMenu() {
            addCommand("0. Exit", parent);
            addCommand("1. Adaugare nota / predare tema", new AddNotaCommand());
            addCommand("2. Modificare nota", new UpdateNotaCommand());
            addCommand("3. Stergere nota", new DeleteNotaCommand());
            addCommand("4. Stergere toate note", new DeleteAllEntitiesCommand(notaService));
            addCommand("5. Afisare note", new ShowNotaListCommand());
        }

        public NotaMenu(String menu_name, MenuCommand parent) {
            super(menu_name);
            this.parent = parent;
            createMenu();
        }

    }

    private class FilterMenu extends MenuCommand {
        MenuCommand parent;
        Filter filterer = new Filter(studentService, temaService, notaService);

        public FilterMenu(String menu_name, MenuCommand parent) {
            super(menu_name);
            this.parent = parent;
            createMenu();
        }

        private class FiltrareNotaMaxCommand implements Command {
            @Override
            public void execute() {
                int nota;

                try {
                    System.out.println("FILTRARE NOTA MAXIMA");
                    System.out.println("--------------------\n");
                    System.out.print("Introduceti nota maxima: ");
                    nota = Integer.parseInt(in.nextLine());

                    System.out.println();
                    EntityPrinter.printNote(filterer.filtreazaNoteSub(nota));
                } catch (NumberFormatException | InputMismatchException e) {
                    System.out.println("\nDate introduse invalide.");
                }
            }
        }

        private class FiltrareNotaTemaCommand implements Command {
            @Override
            public void execute() {
                int tema;

                try {
                    System.out.println("FILTRARE NOTA DUPA TEMA");
                    System.out.println("-----------------------\n");
                    System.out.print("Introduceti id-ul temei: ");
                    tema = Integer.parseInt(in.nextLine());

                    System.out.println();
                    EntityPrinter.printNote(filterer.filtreazaNoteIdTema(tema));
                } catch (NumberFormatException | InputMismatchException e) {
                    System.out.println("\nDate introduse invalide.");
                }
            }
        }

        private class FiltrareNotaSaptamanaCommand implements Command {
            @Override
            public void execute() {
                int saptamana;
                String input;

                try {
                    System.out.println("FILTRARE NOTA DUPA SAPTAMANA");
                    System.out.println("----------------------------\n");
                    System.out.print("Introduceti saptamana de predare a notei (c - saptamana curenta): ");
                    input = in.nextLine();
                    if(input.equals("c")) saptamana = Utils.getCurrentWeek();
                    else saptamana = Integer.parseInt(input);

                    System.out.println();
                    EntityPrinter.printNote(filterer.filtreazaNoteSaptamana(saptamana));
                } catch (NumberFormatException | InputMismatchException e) {
                    System.out.println("\nDate introduse invalide.");
                }
            }
        }

        private class FiltrareStudentiProfesorCommand implements Command {
            @Override
            public void execute() {
                String profesor;

                try {
                    System.out.println("FILTRARE STUDENTI PROFESOR");
                    System.out.println("--------------------------\n");
                    System.out.print("Introduceti profesor: ");
                    profesor = in.nextLine();

                    System.out.println();
                    EntityPrinter.printStudents(filterer.filtreazaStudentiProf(profesor));
                } catch (NumberFormatException | InputMismatchException e) {
                    System.out.println("\nDate introduse invalide.");
                }
            }
        }

        private class FiltrareStudentiGrupaCommand implements Command {
            @Override
            public void execute() {
                int grupa;

                try {
                    System.out.println("FILTRARE STUDENTI DUPA GRUPA");
                    System.out.println("----------------------------\n");
                    System.out.print("Introduceti grupa: ");
                    grupa = Integer.parseInt(in.nextLine());

                    System.out.println();
                    EntityPrinter.printStudents(filterer.filtreazaStudentiGrupa(grupa));
                } catch (NumberFormatException | InputMismatchException e) {
                    System.out.println("\nDate introduse invalide.");
                }
            }
        }

        private class FiltrareStudentiKeywordCommand implements Command {
            @Override
            public void execute() {
                String keyword;

                try {
                    System.out.println("FILTRARE STUDENTI DUPA KEYWORD");
                    System.out.println("------------------------------\n");
                    System.out.print("Introduceti keyword: ");
                    keyword = in.nextLine();

                    System.out.println();
                    EntityPrinter.printStudents(filterer.filtreazaStudentiKeyword(keyword));
                } catch (NumberFormatException | InputMismatchException e) {
                    System.out.println("\nDate introduse invalide.");
                }
            }
        }

        private class FiltrareTemeDeadlineCommand implements Command {
            @Override
            public void execute() {
                int deadline;
                String input;

                try {
                    System.out.println("FILTRARE TEME DUPA DEADLINE");
                    System.out.println("---------------------------\n");
                    System.out.print("Introduceti deadline (c - saptamana curenta): ");
                    input = in.nextLine();
                    if(input.equals("c")) deadline = Utils.getCurrentWeek();
                    else deadline = Integer.parseInt(input);

                    System.out.println();
                    EntityPrinter.printTeme(filterer.filtreazaTemaDeadline(deadline));
                } catch (NumberFormatException | InputMismatchException e) {
                    System.out.println("\nDate introduse invalide.");
                }
            }
        }

        private class SortareTemeDeadlineCommand implements Command {
            @Override
            public void execute() {
                EntityPrinter.printTeme(filterer.sorteazaTemaDeadline());
            }
        }

        private class FiltrareTemeKeywordCommand implements Command {
            @Override
            public void execute() {
                String keyword;

                try {
                    System.out.println("FILTRARE TEME DUPA KEYWORD");
                    System.out.println("--------------------------\n");
                    System.out.print("Introduceti keyword: ");
                    keyword = in.nextLine();

                    System.out.println();
                    EntityPrinter.printTeme(filterer.filtreazaTemaKeyword(keyword));
                } catch (NumberFormatException | InputMismatchException e) {
                    System.out.println("\nDate introduse invalide.");
                }
            }
        }

        private class SortareNoteTemaCommand implements Command {
            @Override
            public void execute() {
                EntityPrinter.printNote(filterer.sorteazaNoteTemaPredare());
            }
        }

        private void createMenu() {
            addCommand("0. Exit", parent);
            addCommand("01. Filtrare note dupa nota maxima", new FiltrareNotaMaxCommand());
            addCommand("02. Filtrare note dupa tema", new FiltrareNotaTemaCommand());
            addCommand("03. Filtrare note dupa saptamana predata", new FiltrareNotaSaptamanaCommand());
            addCommand("04. Filtrare studenti dupa profesor", new FiltrareStudentiProfesorCommand());
            addCommand("05. Filtrare studenti dupa grupa", new FiltrareStudentiGrupaCommand());
            addCommand("06. Filtrare studenti dupa keyword", new FiltrareStudentiKeywordCommand());
            addCommand("07. Filtrare teme dupa deadline", new FiltrareTemeDeadlineCommand());
            addCommand("08. Filtrare teme dupa keyword", new FiltrareTemeKeywordCommand());
            addCommand("09. Sortare teme dupa deadline", new SortareTemeDeadlineCommand());
            addCommand("10. Sortare note dupa id tema", new SortareNoteTemaCommand());
        }
    }

    private void createMenu() {
        main_menu = new MenuCommand("Meniu Principal");

        MenuCommand studentMenu = new StudentMenu("Meniu Student", main_menu);
        MenuCommand temaMenu = new TemaMenu("Meniu Teme", main_menu);
        MenuCommand notaMenu = new NotaMenu("Meniu Note", main_menu);
        MenuCommand filterMenu = new FilterMenu("Meniu filtrari", main_menu);

        main_menu.addCommand("0. Iesire", () -> System.exit(0));
        main_menu.addCommand("1. Meniu studenti", studentMenu);
        main_menu.addCommand("2. Meniu teme", temaMenu);
        main_menu.addCommand("3. Meniu note", notaMenu);
        main_menu.addCommand("4. Filtrari si sortari", filterMenu);
    }

    public void runUI() {
        createMenu();
        MenuCommand current_menu = main_menu;
        int optiune;

        System.out.println();

        while(true) {

            System.out.print("╔");
            System.out.print(new String(new char[Utils.menulength + 2]).replace("\0", "═"));
            System.out.println("╗");

            System.out.printf("║ %-" + Utils.menulength + "s ║\n", current_menu.getMenuName());

            System.out.print("╟");
            System.out.print(new String(new char[Utils.menulength + 2]).replace("\0", "─"));
            System.out.println("╢");

            current_menu.execute();

            System.out.print("╚");
            System.out.print(new String(new char[Utils.menulength + 2]).replace("\0", "═"));
            System.out.println("╝");

            try {
                System.out.print("\nOptiune: ");
                optiune = Integer.parseInt(in.nextLine());
                System.out.println();

                if(optiune < 0 || optiune >= current_menu.getCommands().size())
                    throw(new InputMismatchException());

                Command selected_command = current_menu.getCommands().get(optiune);
                if (selected_command instanceof MenuCommand)
                    current_menu = (MenuCommand) selected_command;
                else {
                    selected_command.execute();
                    System.out.println();
                }
            } catch (InputMismatchException | NumberFormatException ex) {
                System.out.println("Optiune invalida.");
            }
        }
    }
}
