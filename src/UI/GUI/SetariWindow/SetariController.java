package UI.GUI.SetariWindow;

import Service.UserService;
import Utils.GUIUtils;
import Validator.ValidationException;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

public class SetariController implements Initializable {
    String username;
    UserService service;

    @FXML
    StackPane stackPaneContent;
    @FXML
    Label userLabel;

    @FXML
    JFXPasswordField fieldParola;
    @FXML
    JFXPasswordField fieldParolaNoua;
    @FXML
    JFXPasswordField fieldRescriereParolaNoua;

    @FXML
    JFXButton buttonModificare;

    public void setService(UserService service) {
        this.service = service;
    }

    public void setUsername(String username) {
        this.username = username;
        userLabel.setText(username);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    private void handleButtonModificare(ActionEvent actionEvent) {
        GUIUtils.removeWrongInput(fieldParola);
        GUIUtils.removeWrongInput(fieldParolaNoua);
        GUIUtils.removeWrongInput(fieldRescriereParolaNoua);

        try {
            service.modifyUser(username, fieldParola.getText(), fieldParolaNoua.getText(), fieldRescriereParolaNoua.getText());

            GUIUtils.showDialogMessage(Alert.AlertType.INFORMATION, "Modificare cu success!", "Parola a fost modificata cu success!", stackPaneContent);
        } catch (ValidationException e) {
            if (e.getMessage().contains("coincid"))
                GUIUtils.setWrongInput(fieldRescriereParolaNoua, "Parolele trebuie sa coincida.");
            if (e.getMessage().contains("veche"))
                GUIUtils.setWrongInput(fieldParola, "Parola veche nu este corecta.");
            if (e.getMessage().contains("caractere"))
                GUIUtils.setWrongInput(fieldParolaNoua, "Parola trebuie sa contina cel putin 5 caractere");

        }

        fieldParola.clear();
        fieldParolaNoua.clear();
        fieldRescriereParolaNoua.clear();
    }
}
