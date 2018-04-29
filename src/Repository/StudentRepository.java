package Repository;

import Domain.Student;
import Validator.StudentValidator;

public class StudentRepository extends AbstractRepository<Integer, Student> {
    public StudentRepository() {
        super(new StudentValidator());
    }
}
