package UI.GUI.Login;

import Domain.User;
import Repository.UsersRepository;
import Service.*;
import UI.GUI.Main.MainController;
import Utils.GUIUtils;
import Utils.Utils;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    private StudentService studentService;
    private TemaService temaService;
    private NotaService notaService;
    private UserService userService;
    private ReportService reportService;

    @FXML
    StackPane stackPaneContent;
    @FXML
    JFXTextField fieldUser;
    @FXML
    JFXPasswordField fieldPassword;

    @FXML
    JFXTextField fakeErrorField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
//            userService.addUser("admin", "admin", User.UserType.admin);
//            userService.addUser("profesor", "profesor", User.UserType.profesor);
//            userService.addUser("student", "student", User.UserType.student);
//            userService.addUser("ssir2198", "parolamea", User.UserType.student);
//            userService.addUser("rair2912",Utils.defaultPassword, User.UserType.student);
//            userService.addUser("mjie2932", Utils.defaultPassword, User.UserType.student);
//            userService.addUser("pajr2334", Utils.defaultPassword, User.UserType.student);
//            userService.addUser("pdir2174", Utils.defaultPassword, User.UserType.student);
//            userService.addUser("mgir2146", Utils.defaultPassword, User.UserType.student);
        } catch (Exception e) { }
    }

    public void setServices(StudentService studentService, TemaService temaService, NotaService notaService, UserService userService, ReportService reportService) {
        this.studentService = studentService;
        this.temaService = temaService;
        this.notaService = notaService;
        this.userService = userService;
        this.reportService = reportService;
    }

    @FXML
    private void closeWindow() {
        ((Stage) fieldUser.getScene().getWindow()).close();
    }

    @FXML
    private void handleLogin(ActionEvent actionEvent) {
        String user = "";
        String password = "";

        GUIUtils.removeWrongInput(fakeErrorField);

        if (fieldUser.getText() != null)
            user = fieldUser.getText().trim();
        if (fieldPassword.getText() != null)
            password = fieldPassword.getText();

        if (userService.verifyPassword(user, password)) {
            closeWindow();
            User.UserType userType = userService.getUserType(user);
            loadMainView(userType, user);
        } else {
            GUIUtils.setWrongInput(fakeErrorField, "Utilizatorul sau parola nu sunt corecte.");
            fieldPassword.clear();
            fieldUser.clear();
            fieldUser.requestFocus();
        }
    }

    private void loadMainView(User.UserType userType, String username) {
        Stage mainStage = new Stage();

        mainStage.setTitle("Manager de studenti");
        mainStage.getIcons().add(new Image("/Resources/icon.png"));

        // SCALE WINDOW BASED ON RESOLUTION

        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

        double width = 1280;
        double height = 860;

        mainStage.setMinWidth(1024);
        mainStage.setMinHeight(768);

        if (width > primaryScreenBounds.getWidth() || height > primaryScreenBounds.getHeight()) {
            mainStage.setMinWidth(800);
            mainStage.setMinHeight(600);
            mainStage.setMaximized(true);
        }
        if (width > primaryScreenBounds.getWidth())
            width = primaryScreenBounds.getWidth();

        if (height > primaryScreenBounds.getHeight())
            height = primaryScreenBounds.getHeight();

//        primaryStage.initStyle(StageStyle.TRANSPARENT);

        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../Main/MainView.fxml"));
            root = loader.load();

            MainController controller = loader.getController();
            controller.setServices(studentService, temaService, notaService, userService, reportService);
            controller.setUserType(userType, username);

            Scene scene = new Scene(root, width, height);

            mainStage.setScene(scene);
            mainStage.show();
        } catch (IOException e) {
            System.out.println("Eroare la incarcarea view-ului principal.");
        }
    }
}
