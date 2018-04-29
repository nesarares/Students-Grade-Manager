package Repository;

import Utils.Utils;
import Validator.Validator;
import Validator.ValidationException;
import Domain.hasId;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public abstract class AbstractFileRepository<Id, E extends hasId<Id>> extends AbstractRepository<Id, E> {
    private String filename;

    public AbstractFileRepository(Validator<E> vali, String filename) {
        super(vali);
        this.filename = filename;
        readData();
    }

    private void readData() {
        Path path = Paths.get(filename);
        Stream<String> lines;
        try {
            lines = Files.lines(path);
            lines.forEach(line -> {
                String[] fields = line.split(";");
                E entity = createEntity(fields);
                try {
                    super.add(entity);
                } catch (RepositoryException e) {
                    System.out.println("Date duplicate citite.");
                } catch (ValidationException e) {
                    System.out.println("Date invalide citite.");
                }
            });
        } catch (FileNotFoundException e) {
            Utils.createFile(filename);
            System.out.println("// S-a creat fisierul '" + filename + "' //");
        } catch (IOException | NumberFormatException e) {
            System.out.println("Input-Output error - File corrupted.");
        }
    }

    protected abstract E createEntity(String[] fields);

    private void saveData() {
        try(BufferedWriter out = new BufferedWriter(new FileWriter(filename))) {
            if(getAll() != null)
            getAll().forEach((entity) -> {
                try {
                    out.write(entity.toString() + "\n");
                } catch (IOException e) {
                    System.out.println("Input-Output error.");
                }
            });
        } catch (IOException e) {
            System.out.println("Input-Output error.");
        }
    }

    private void appendData(E entity) {
        try(BufferedWriter out = new BufferedWriter(new FileWriter(filename, true))) {
            out.write(entity.toString() + "\n");
        } catch (IOException e) {
            System.out.println("Input-Output error.");
        }
    }

    @Override
    public void add(E entity) throws ValidationException, RepositoryException {
        super.add(entity);
        appendData(entity);
    }

    @Override
    public E remove(Id id) {
        E returned = super.remove(id);
        saveData();
        return returned;
    }

    @Override
    public void removeAll() {
        super.removeAll();
        saveData();
    }

    @Override
    public E update(E entity) throws ValidationException {
        E returned = super.update(entity);
        saveData();
        return returned;
    }
}
