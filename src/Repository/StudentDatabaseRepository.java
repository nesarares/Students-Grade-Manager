package Repository;

import Repository.Database.DatabaseHandler;
import Domain.Student;
import Utils.Utils;
import Validator.StudentValidator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class StudentDatabaseRepository extends AbstractDatabaseRepository<Integer, Student> {
    public StudentDatabaseRepository(DatabaseHandler databaseHandler) {
        super(new StudentValidator(), databaseHandler, Utils.studentTableName);
        getAllQuery += " ORDER BY nume";
    }

    @Override
    protected Student createEntityFromFile(String[] fields) {
        int id = Integer.parseInt(fields[0]);
        String nume = fields[1];
        int grupa = Integer.parseInt(fields[2]);
        String email = fields[3];
        String prof_indrumator = fields[4];
        String username = fields[5];

        return new Student(id, nume, grupa, email, prof_indrumator, username);
    }

    @Override
    protected Optional<Student> createEntity(ResultSet resultSet) {
        Optional<Student> returned = Optional.empty();

        try {
            int id = resultSet.getInt("id");
            String nume = resultSet.getString("nume");
            int grupa = resultSet.getInt("grupa");
            String email = resultSet.getString("email");
            String prof_indrumator = resultSet.getString("prof_indrumator");
            String username = resultSet.getString("username");

            Student student = new Student(id, nume, grupa, email, prof_indrumator, username);

            return Optional.of(student);
        } catch (SQLException e) {
            System.out.println("Eroare la create entity din result set : " + e.getMessage());
        }

        return returned;
    }

    @Override
    protected void createAddStatement() throws SQLException {
        String query = "INSERT INTO " + tableName +
                " (id, nume, grupa, email, prof_indrumator, username) values (?, ?, ?, ?, ?, ?)";
        preparedStatement = DatabaseHandler.getConnection().prepareStatement(query);
    }

    @Override
    protected void createEditStatement(Integer id) throws SQLException {
        String query = "UPDATE " + tableName + " SET" +
                " id = ?, nume = ?, grupa = ?, email = ?, prof_indrumator = ?, username = ? " +
                " WHERE id = ?";
        preparedStatement = DatabaseHandler.getConnection().prepareStatement(query);
        preparedStatement.setInt(7, id);
    }

    @Override
    protected void populateStatementValues(Student entity) throws SQLException {
        preparedStatement.setInt(1, entity.getId());
        preparedStatement.setString(2, entity.getNume());
        preparedStatement.setInt(3, entity.getGrupa());
        preparedStatement.setString(4, entity.getEmail());
        preparedStatement.setString(5, entity.getProf_indrumator());
        preparedStatement.setString(6, entity.getUsername());
    }
}
