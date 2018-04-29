package Repository;

import Domain.User;
import Repository.Database.DatabaseHandler;
import Utils.Utils;
import Validator.UserValidator;
import Validator.Validator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UsersDatabaseRepository extends AbstractDatabaseRepository<String, User> {
    public UsersDatabaseRepository(DatabaseHandler databaseHandler) {
        super(new UserValidator(), databaseHandler, Utils.userTableName);
        idColumnName = "username";
    }

    @Override
    protected User createEntityFromFile(String[] fields) {
        String username = fields[0];
        String hash = fields[1];
        User.UserType type = User.UserType.valueOf(fields[2]);

        return new User(username, hash, type);
    }

    @Override
    protected Optional<User> createEntity(ResultSet resultSet) {

        Optional<User> returned = Optional.empty();

        try {
            String username = resultSet.getString("username");
            String hash = resultSet.getString("hash");
            String type = resultSet.getString("type");

            User user = new User(username, hash, User.UserType.valueOf(type));

            return Optional.of(user);
        } catch (SQLException e) {
            System.out.println("Eroare la create entity din result set : " + e.getMessage());
        }

        return returned;
    }

    @Override
    protected void createAddStatement() throws SQLException {
        String query = "INSERT INTO " + tableName +
                " (username, hash, type) values (?, ?, ?)";
        preparedStatement = DatabaseHandler.getConnection().prepareStatement(query);
    }

    @Override
    protected void createEditStatement(String s) throws SQLException {
        String query = "UPDATE " + tableName + " SET" +
                " username = ?, hash = ?, type = ?" +
                " WHERE username = ?";
        preparedStatement = DatabaseHandler.getConnection().prepareStatement(query);
        preparedStatement.setString(4, s);
    }

    @Override
    protected void populateStatementValues(User entity) throws SQLException {
        preparedStatement.setString(1, entity.getUsername());
        preparedStatement.setString(2, entity.getHash());
        preparedStatement.setString(3, entity.getType().toString());
    }
}
