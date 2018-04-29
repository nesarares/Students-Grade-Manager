package UI.GUI.Main;

import Domain.User;
import Service.*;
import UI.GUI.DateStudentWindow.DateStudentController;
import UI.GUI.Login.LoginController;
import UI.GUI.NoteMainWindow.NoteController;
import UI.GUI.RapoarteWindow.RapoarteController;
import UI.GUI.SetariWindow.SetariController;
import UI.GUI.StudentMainWindow.StudentController;
import UI.GUI.StudentMainWindow.StudentView;
import UI.GUI.TemeMainWindow.TemeController;
import UI.GUI.UtilizatoriWindow.UtilizatoriController;
import Utils.GUIUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    StackPane stackPaneContent = null;
    @FXML
    AnchorPane rootPane = null;
    @FXML
    AnchorPane contentPane = null;
    @FXML
    VBox menuPane = null;
    @FXML
    Button buttonStudenti = null;
    @FXML
    Button buttonTeme = null;
    @FXML
    Button buttonNote = null;
    @FXML
    Button buttonDate = null;
    @FXML
    Button buttonRapoarte = null;
    @FXML
    Button buttonUtilizatori = null;
    @FXML
    Button buttonSetari = null;

    private Node studentView, temeView, noteView, dateView, rapoarteView, setariView, utilizatoriView;

    private StudentService studentService;
    private TemaService temaService;
    private NotaService notaService;
    private UserService userService;
    private ReportService reportService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setServices(StudentService studentService, TemaService temaService, NotaService notaService, UserService userService, ReportService reportService) {
        this.studentService = studentService;
        this.temaService = temaService;
        this.notaService = notaService;
        this.userService = userService;
        this.reportService = reportService;

        studentView = initStudentView();
        temeView = initTemeView();
        noteView = initNoteView();

        handleButtonStudenti(null);
    }

    public void setUserType(User.UserType userType, String username) {
        setariView = initSetariView(username, userService);

        if (userType == User.UserType.admin) {
            utilizatoriView = initUtilizatoriView(userService);
            studentService.addObserver(userService);
            menuPane.getChildren().removeAll(buttonNote, buttonTeme, buttonDate, buttonRapoarte);
            handleButtonUtilizatori(null);
        }

        if (userType == User.UserType.profesor) {
            rapoarteView = initRapoarteView();
            menuPane.getChildren().removeAll(buttonStudenti, buttonDate, buttonUtilizatori);
            handleButtonNote(null);
        }

        if (userType == User.UserType.student) {
            dateView = initDateView(username);
            menuPane.getChildren().removeAll(buttonNote, buttonStudenti, buttonTeme, buttonUtilizatori, buttonRapoarte);
            handleButtonDate(null);
        }
    }

    private void handleAbstractButton(Node view, Button button) {
        contentPane.getChildren().setAll(view);

        AnchorPane.setTopAnchor(view, 0d);
        AnchorPane.setBottomAnchor(view, 0d);
        AnchorPane.setLeftAnchor(view, 0d);
        AnchorPane.setRightAnchor(view, 0d);

        buttonStudenti.setDisable(false);
        buttonTeme.setDisable(false);
        buttonNote.setDisable(false);
        buttonDate.setDisable(false);
        buttonRapoarte.setDisable(false);
        buttonSetari.setDisable(false);
        buttonUtilizatori.setDisable(false);
        button.setDisable(true);
    }

    @FXML
    public void handleButtonStudenti(ActionEvent actionEvent) {
        handleAbstractButton(studentView, buttonStudenti);
    }

    @FXML
    public void handleButtonTeme(ActionEvent actionEvent) {
        handleAbstractButton(temeView, buttonTeme);
    }

    @FXML
    public void handleButtonNote(ActionEvent actionEvent) {
        handleAbstractButton(noteView, buttonNote);
    }

    @FXML
    public void handleButtonDate(ActionEvent actionEvent) {
        handleAbstractButton(dateView, buttonDate);
    }

    @FXML
    public void handleButtonRapoarte(ActionEvent actionEvent) {
        handleAbstractButton(rapoarteView, buttonRapoarte);
    }

    @FXML
    public void handleButtonSetari(ActionEvent actionEvent) {
        handleAbstractButton(setariView, buttonSetari);
    }

    @FXML
    public void handleButtonUtilizatori(ActionEvent actionEvent) {
        handleAbstractButton(utilizatoriView, buttonUtilizatori);
    }

    private ScrollPane initStudentView() {
        StudentController student_controller = new StudentController(studentService);
        studentService.addObserver(student_controller);

        StudentView student_view = new StudentView(student_controller);
        student_controller.setView(student_view);

        ScrollPane main = new ScrollPane(student_view.getView());
        main.setFitToWidth(true);
        main.getStylesheets().add(getClass().getResource("../stylesheet.css").toString());

        return main;
    }

    private ScrollPane initTemeView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../TemeMainWindow/TemeView.fxml"));
            Parent view = loader.load();

            TemeController controller = loader.getController();
            controller.setService(temaService);
            temaService.addObserver(controller);

            ScrollPane main = new ScrollPane(view);
            main.setFitToWidth(true);

            return main;
        } catch (IOException e) {
            System.out.println("Eroare la initializarea view-ului de teme.");
            return new ScrollPane();
        }
    }

    private ScrollPane initNoteView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../NoteMainWindow/NoteView.fxml"));
            Parent view = loader.load();
            NoteController controller = loader.getController();

            controller.setService(notaService);
            notaService.addObserver(controller);
            controller.setServices(studentService, temaService);

            ScrollPane main = new ScrollPane(view);
            main.setFitToWidth(true);

            return main;
        } catch (IOException e) {
            System.out.println("Eroare la initializarea view-ului de note.");
            return new ScrollPane();
        }
    }

    private ScrollPane initDateView(String username) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../DateStudentWindow/DateStudentView.fxml"));
            Parent view = loader.load();
            DateStudentController controller = loader.getController();

            controller.setService(notaService);
            notaService.addObserver(controller);
            controller.setServices(studentService, temaService);
            controller.setUsername(username);

            ScrollPane main = new ScrollPane(view);
            main.setFitToWidth(true);

            return main;
        } catch (IOException e) {
            System.out.println("Eroare la initializarea view-ului de date ale studentului.");
            return new ScrollPane();
        }
    }

    private ScrollPane initRapoarteView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../RapoarteWindow/RapoarteView.fxml"));
            Parent view = loader.load();
            RapoarteController controller = loader.getController();

            controller.setService(reportService);

            ScrollPane main = new ScrollPane(view);
            main.setFitToWidth(true);

            return main;
        } catch (IOException e) {
            System.out.println("Eroare la initializarea view-ului de rapoarte.");
            return new ScrollPane();
        }
    }

    private ScrollPane initSetariView(String username, UserService service) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../SetariWindow/SetariView.fxml"));
            Parent view = loader.load();
            SetariController controller = loader.getController();

            controller.setUsername(username);
            controller.setService(service);

            ScrollPane main = new ScrollPane(view);
            main.setFitToWidth(true);

            return main;
        } catch (IOException e) {
            System.out.println("Eroare la initializarea view-ului de setari.");
            return new ScrollPane();
        }
    }

    private ScrollPane initUtilizatoriView(UserService service) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../UtilizatoriWindow/UtilizatoriView.fxml"));
            Parent view = loader.load();
            UtilizatoriController controller = loader.getController();
            controller.setService(service);
            service.addObserver(controller);

            ScrollPane main = new ScrollPane(view);
            main.setFitToWidth(true);

            return main;
        } catch (IOException e) {
            System.out.println("Eroare la initializarea view-ului de utilizatori.");
            return new ScrollPane();
        }
    }

    @FXML
    private void handleMenuFullScreen(ActionEvent actionEvent) {
        Stage stage = ((Stage) rootPane.getScene().getWindow());
        stage.setFullScreen(!stage.isFullScreen());
    }

    @FXML
    private void handleMenuClose(ActionEvent actionEvent) {
        ((Stage) rootPane.getScene().getWindow()).close();
    }

    @FXML
    private void handleMenuAbout(ActionEvent actionEvent) {
        GUIUtils.showDialogMessage(Alert.AlertType.INFORMATION, "Despre", "Aplicatie realizata in scopul gestiunii studentilor" +
                " si a notelor de la temele de laborator ale acestora." +
                "\n\n\nAutor: Nesa Rares - gr. 225", stackPaneContent);
    }

    @FXML
    private void handleSignOut(ActionEvent actionEvent) {
        ((Stage) stackPaneContent.getScene().getWindow()).close();

        Stage loginStage = new Stage();
        loginStage.setTitle("Manager de studenti");
        loginStage.getIcons().add(new Image("/Resources/icon.png"));
        loginStage.setResizable(false);

        // SET SCENE

        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../Login/LoginView.fxml"));
            root = loader.load();
            LoginController controller = loader.getController();
            controller.setServices(studentService, temaService, notaService, userService, reportService);
            Scene scene = new Scene(root);

            loginStage.setScene(scene);
            loginStage.show();
        } catch (IOException e) {
            System.out.println("Eroare la incarcarea view-ului de login.");
        }

    }
}
