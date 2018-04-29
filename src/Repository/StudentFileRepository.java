package Repository;

import Domain.Student;
import Validator.StudentValidator;

public class StudentFileRepository extends AbstractFileRepository<Integer, Student> {
    public StudentFileRepository(String filename) {
        super(new StudentValidator(), filename);
    }

    @Override
    protected Student createEntity(String[] fields) {
        int id = Integer.parseInt(fields[0]);
        String nume = fields[1];
        int grupa = Integer.parseInt(fields[2]);
        String email = fields[3];
        String prof_indrumator = fields[4];
        String username = fields[5];

        return new Student(id, nume, grupa, email, prof_indrumator, username);
    }
}