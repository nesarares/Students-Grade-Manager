package Repository;

import Domain.Tema;
import Repository.Database.DatabaseHandler;
import Utils.Utils;
import Validator.TemaValidator;
import Validator.Validator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class TemaDatabaseRepository extends AbstractDatabaseRepository<Integer, Tema> {
    public TemaDatabaseRepository(DatabaseHandler databaseHandler) {
        super(new TemaValidator(), databaseHandler, Utils.temaTableName);
    }

    @Override
    protected Tema createEntityFromFile(String[] fields) {
        int id = Integer.parseInt(fields[0]);
        String descriere = fields[1];
        int deadline = Integer.parseInt(fields[2]);

        Tema tema = new Tema(id, descriere, deadline);

        return tema;
    }

    @Override
    protected Optional<Tema> createEntity(ResultSet resultSet) {
        Optional<Tema> returned = Optional.empty();

        try {
            int id = resultSet.getInt("id");
            String descriere = resultSet.getString("descriere");
            int deadline = resultSet.getInt("deadline");

            Tema tema = new Tema(id, descriere, deadline);

            return Optional.of(tema);
        } catch (SQLException e) {
            System.out.println("Eroare la create entity din result set : " + e.getMessage());
        }

        return returned;
    }

    @Override
    protected void createAddStatement() throws SQLException {
        String query = "INSERT INTO " + tableName +
                " (id, descriere, deadline) values (?, ?, ?)";
        preparedStatement = DatabaseHandler.getConnection().prepareStatement(query);
    }

    @Override
    protected void createEditStatement(Integer integer) throws SQLException {
        String query = "UPDATE " + tableName + " SET" +
                " id = ?, descriere = ?, deadline = ?" +
                " WHERE id = ?";
        preparedStatement = DatabaseHandler.getConnection().prepareStatement(query);
        preparedStatement.setInt(4, integer);
    }

    @Override
    protected void populateStatementValues(Tema entity) throws SQLException {
        preparedStatement.setInt(1, entity.getId());
        preparedStatement.setString(2, entity.getDescriere());
        preparedStatement.setInt(3, entity.getDeadline());
    }
}
