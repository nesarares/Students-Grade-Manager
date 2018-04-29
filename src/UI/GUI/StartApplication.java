package UI.GUI;

import Domain.Nota;
import Domain.Student;
import Domain.Tema;
import Domain.User;
import Repository.*;
import Repository.Database.DatabaseHandler;
import Service.*;
import UI.GUI.Login.LoginController;
import Utils.Utils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class StartApplication extends Application {
    DatabaseHandler databaseHandler = new DatabaseHandler();

    private Repository<Integer, Student> studentRepository = new StudentDatabaseRepository(databaseHandler);
    private Repository<Integer, Tema> temaRepository = new TemaDatabaseRepository(databaseHandler);
    private Repository<Integer, Nota> notaRepository = new NotaDatabaseRepository(databaseHandler);
    private Repository<String, User> usersRepository = new UsersDatabaseRepository(databaseHandler);

    private StudentService studentService = new StudentService(studentRepository, notaRepository);
    private TemaService temaService = new TemaService(temaRepository, notaRepository);
    private NotaService notaService = new NotaService(notaRepository, studentRepository, temaRepository);
    private ReportService reportService = new ReportService(studentService, temaService, notaService);
    private UserService userService = new UserService(usersRepository);

    @Override
    public void start(Stage primaryStage) throws Exception {
//        ((StudentDatabaseRepository) studentRepository).importDataFromFile(Utils.studentiFilePath, ";");
//        ((TemaDatabaseRepository) temaRepository).importDataFromFile(Utils.temeFilePath, ";");
//        ((NotaDatabaseRepository) notaRepository).importDataFromFile(Utils.noteFilePath, ";");
//        ((UsersDatabaseRepository) usersRepository).importDataFromFile(Utils.usersFilePath, ";");

        primaryStage.setTitle("Manager de studenti");
        primaryStage.getIcons().add(new Image("/Resources/icon.png"));
        primaryStage.setResizable(false);

        // SET SCENE

        FXMLLoader loader = new FXMLLoader(getClass().getResource("./Login/LoginView.fxml"));
        Parent root = loader.load();

        LoginController controller = loader.getController();
        controller.setServices(studentService, temaService, notaService, userService, reportService);

        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
