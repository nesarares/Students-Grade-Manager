package Repository;

import Domain.Nota;
import Repository.Database.DatabaseHandler;
import Utils.Utils;
import Validator.NotaValidator;
import Validator.Validator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class NotaDatabaseRepository extends AbstractDatabaseRepository<Integer, Nota> {
    public NotaDatabaseRepository(DatabaseHandler databaseHandler) {
        super(new NotaValidator(), databaseHandler, Utils.notaTableName);
    }

    @Override
    protected Nota createEntityFromFile(String[] fields) {
        int id = Integer.parseInt(fields[0]);
        int id_student = Integer.parseInt(fields[1]);
        int id_tema = Integer.parseInt(fields[2]);
        double nota = Double.parseDouble(fields[3]);
        int saptamana_predare = Integer.parseInt(fields[4]);

        Nota object = new Nota(id, id_student, id_tema, nota, saptamana_predare);

        return object;
    }

    @Override
    protected Optional<Nota> createEntity(ResultSet resultSet) {
        Optional<Nota> returned = Optional.empty();

        try {
            int id = resultSet.getInt("id");
            int id_student = resultSet.getInt("id_student");
            int id_tema = resultSet.getInt("id_tema");
            double nota = resultSet.getDouble("nota");
            int saptamana_predare = resultSet.getInt("saptamana_predare");

            Nota notaObject = new Nota(id, id_student, id_tema, nota, saptamana_predare);

            return Optional.of(notaObject);
        } catch (SQLException e) {
            System.out.println("Eroare la create entity din result set : " + e.getMessage());
        }

        return returned;
    }

    @Override
    protected void createAddStatement() throws SQLException {
        String query = "INSERT INTO " + tableName +
                " (id, id_student, id_tema, nota, saptamana_predare) values (?, ?, ?, ?, ?)";
        preparedStatement = DatabaseHandler.getConnection().prepareStatement(query);
    }

    @Override
    protected void createEditStatement(Integer integer) throws SQLException {
        String query = "UPDATE " + tableName + " SET" +
                " id = ?, id_student = ?, id_tema = ?, nota = ?, saptamana_predare = ?" +
                " WHERE id = ?";
        preparedStatement = DatabaseHandler.getConnection().prepareStatement(query);
        preparedStatement.setInt(6, integer);
    }

    @Override
    protected void populateStatementValues(Nota entity) throws SQLException {
        preparedStatement.setInt(1, entity.getId());
        preparedStatement.setInt(2, entity.getId_student());
        preparedStatement.setInt(3, entity.getId_tema());
        preparedStatement.setDouble(4, entity.getNota());
        preparedStatement.setInt(5, entity.getSaptamana_predare());
    }

    @Override
    protected void optionalValidations(Nota entity) throws RepositoryException {
        if(getAll() != null)
            for(Nota nota : getAll())
                if(nota.getId_student() == entity.getId_student() && nota.getId_tema() == entity.getId_tema())
                    throw (new RepositoryException("Studentul " + entity.getId_student() + " are deja o nota la tema " + entity.getId_tema() + "."));
    }
}
