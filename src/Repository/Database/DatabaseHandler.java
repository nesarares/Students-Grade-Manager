package Repository.Database;

import Utils.Utils;

import javax.swing.JOptionPane;
import java.sql.*;

public final class DatabaseHandler {
    private static final String DB_URL = "jdbc:derby:" + Utils.databasePath + ";create=true";
    private static Connection connection = null;
    private static Statement statement = null;

    public DatabaseHandler() {
        createConnection();
        createStudentTable();
        createTemaTable();
        createNotaTable();
        createUserTable();
    }

    public static Connection getConnection() {
        return connection;
    }

    private void createConnection() {
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").getConstructor().newInstance();
            connection = DriverManager.getConnection(DB_URL);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Can't load database", "Database Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    private void createStudentTable() {
        String tableName = Utils.studentTableName;
        try {
            statement = connection.createStatement();

            DatabaseMetaData metadata = connection.getMetaData();
            ResultSet tables = metadata.getTables(null, null, tableName.toUpperCase(), null);

            if (! tables.next()) {
                statement.execute("CREATE TABLE " + tableName + "(" +
                        " id INTEGER primary key," +
                        " nume VARCHAR(200)," +
                        " grupa INTEGER," +
                        " email VARCHAR(200)," +
                        " prof_indrumator VARCHAR(200)," +
                        " username VARCHAR(100)" +
                        ")");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage() + " --- setupDatabase");
        }
    }

    private void createTemaTable() {
        String tableName = Utils.temaTableName;
        try {
            statement = connection.createStatement();

            DatabaseMetaData metadata = connection.getMetaData();
            ResultSet tables = metadata.getTables(null, null, tableName.toUpperCase(), null);

            if (! tables.next()) {
                statement.execute("CREATE TABLE " + tableName + "(" +
                        " id INTEGER primary key," +
                        " descriere VARCHAR(200)," +
                        " deadline INTEGER " +
                        ")");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage() + " --- setupDatabase");
        }
    }

    private void createNotaTable() {
        String tableName = Utils.notaTableName;
        try {
            statement = connection.createStatement();

            DatabaseMetaData metadata = connection.getMetaData();
            ResultSet tables = metadata.getTables(null, null, tableName.toUpperCase(), null);

            if (! tables.next()) {
                statement.execute("CREATE TABLE " + tableName + "(" +
                        " id INTEGER primary key," +
                        " id_student INTEGER, " +
                        " id_tema INTEGER, " +
                        " nota DOUBLE," +
                        " saptamana_predare INTEGER " +
                        ")");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage() + " --- setupDatabase");
        }
    }

    private void createUserTable() {
        String tableName = Utils.userTableName;
        try {
            statement = connection.createStatement();

            DatabaseMetaData metadata = connection.getMetaData();
            ResultSet tables = metadata.getTables(null, null, tableName.toUpperCase(), null);

            if (! tables.next()) {
                statement.execute("CREATE TABLE " + tableName + "(" +
                        " username VARCHAR(100) primary key," +
                        " hash VARCHAR(200), " +
                        " type VARCHAR(50) " +
                        ")");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage() + " --- setupDatabase");
        }
    }

    public ResultSet execQuery(String query) {
        ResultSet result;
        try {
            statement = connection.createStatement();
            result = statement.executeQuery(query);
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Exception at execQuery:dataHandler : " + ex.getMessage());
            return null;
        }
        return result;
    }

    public boolean execAction(String query) {
        try {
            statement = connection.createStatement();
            statement.execute(query);
            return true;
        } catch (SQLException ex) {
            System.out.println("Exception at execQuery:dataHandler : " + ex.getMessage());
            return false;
        }
    }
}
