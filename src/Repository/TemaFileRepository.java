package Repository;

import Domain.Tema;
import Validator.TemaValidator;

//public class TemaFileRepository extends TemaRepository {
//    private String filename;
//
//    public TemaFileRepository(String filename) {
//        super();
//        this.filename = filename;
//        readData();
//    }
//
//    private void readData() {
//        try(BufferedReader in = new BufferedReader(new FileReader(filename))) {
//            String line;
//
//            while((line = in.readLine()) != null) {
//                String[] fields = line.split(";");
//
//                int id = Integer.parseInt(fields[0]);
//                String descriere = fields[1];
//                int deadline = Integer.parseInt(fields[2]);
//
//                Tema tema = new Tema(id, descriere, deadline);
//
//                super.add(tema);
//            }
//
//        } catch (FileNotFoundException e) {
//            Utils.createFile(filename);
//            System.out.println("// S-a creat fisierul '" + filename + "' //");
//        } catch (IOException e) {
//            System.out.println("Input-Output error - File corrupted.");
//        } catch (RepositoryException e) {
//            System.out.println("Date duplicate citite.");
//        } catch (ValidationException e) {
//            System.out.println("Date nevalide citite.");
//        }
//    }
//
//    private void saveData() {
//        try(BufferedWriter out = new BufferedWriter(new FileWriter(filename))) {
//            getAll().forEach((tema) -> {
//                try {
//                    out.write(tema.getId() + ";"
//                            + tema.getDescriere() + ";"
//                            + tema.getDeadline() + ";"
//                            + "\n");
//                } catch (IOException e) {
//                    System.out.println("Input-Output error.");
//                }
//            });
//        } catch (IOException e) {
//            System.out.println("Input-Output error.");
//        }
//    }
//
//    private void appendData(Tema tema) {
//        try(BufferedWriter out = new BufferedWriter(new FileWriter(filename, true))) {
//            out.write(tema.getId() + ";"
//                    + tema.getDescriere() + ";"
//                    + tema.getDeadline() + ";"
//                    + "\n");
//        } catch (IOException e) {
//            System.out.println("Input-Output error.");
//        }
//    }
//
//    @Override
//    public void add(Tema entity) throws ValidationException, RepositoryException {
//        super.add(entity);
//        appendData(entity);
//    }
//
//    @Override
//    public Tema remove(Integer integer) {
//        Tema returned = super.remove(integer);
//        saveData();
//        return returned;
//    }
//
//    @Override
//    public Tema update(Tema entity) throws ValidationException {
//        Tema returned = super.update(entity);
//        saveData();
//        return returned;
//    }
//}

public class TemaFileRepository extends AbstractFileRepository<Integer, Tema> {
    public TemaFileRepository(String filename) {
        super(new TemaValidator(), filename);
    }

    @Override
    protected Tema createEntity(String[] fields) {
        int id = Integer.parseInt(fields[0]);
        String descriere = fields[1];
        int deadline = Integer.parseInt(fields[2]);

        Tema tema = new Tema(id, descriere, deadline);

        return tema;
    }
}
