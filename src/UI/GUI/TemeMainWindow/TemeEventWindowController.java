package UI.GUI.TemeMainWindow;

import Domain.Tema;
import Repository.RepositoryException;
import Utils.*;
import Validator.ValidationException;
import com.jfoenix.controls.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class TemeEventWindowController implements Initializable {
    private TemeController controller;

    @FXML
    JFXButton ButtonEvent;
    @FXML
    JFXButton ButtonClose;
    @FXML
    Label Title;

    @FXML
    JFXTextField FieldId;
    @FXML
    JFXTextField FieldDescriere;
    @FXML
    JFXDatePicker FieldDeadline;
    @FXML
    JFXTextField FakeFieldDeadline;

    public void setController(TemeController controller) {
        this.controller = controller;
    }

    void setEditWindow(Tema tema) {
        GUIUtils.removeWrongInput(FieldId);
        GUIUtils.removeWrongInput(FieldDescriere);
        GUIUtils.removeWrongInput(FakeFieldDeadline);

        Title.setText("Modificare tema");
        ButtonEvent.setText("Modifica");

        FieldId.setText(String.valueOf(tema.getId()));
        FieldId.setDisable(true);

        FieldDescriere.setText(tema.getDescriere());
        FieldDescriere.setDisable(true);

        FieldDeadline.getEditor().clear();

        ButtonEvent.setOnAction(this::handleEditButton);
    }

    void setAddWindow() {
        GUIUtils.removeWrongInput(FieldId);
        GUIUtils.removeWrongInput(FieldDescriere);
        GUIUtils.removeWrongInput(FakeFieldDeadline);
        Title.setText("Adaugare tema");
        ButtonEvent.setText("Adauga");

        FieldId.setDisable(false);
        FieldDescriere.setDisable(false);

        FieldId.clear();
        FieldDescriere.clear();
        FieldDeadline.getEditor().clear();

        ButtonEvent.setOnAction(this::handleAddButton);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    private void handleCancelButton() {
        controller.handleCloseWindowAdd();
    }

    @FXML
    private void handleAddButton(ActionEvent actionEvent) {
        GUIUtils.removeWrongInput(FieldId);
        GUIUtils.removeWrongInput(FieldDescriere);
        GUIUtils.removeWrongInput(FakeFieldDeadline);

        int id = -1;
        try {
            id = Integer.parseInt(FieldId.getText());
        } catch (NumberFormatException e) {
            GUIUtils.setWrongInput(FieldId, "Id-ul nu este valid");
        }

        String descriere = FieldDescriere.getText();
        int deadline = -1;

        if(FieldDeadline.getValue() != null) {
            deadline = Utils.getWeekFromDate(FieldDeadline.getValue());
        }

        try {
            controller.handleAddTema(id, descriere, deadline);

            controller.handleCloseWindowAdd();
            GUIUtils.showDialogMessage(Alert.AlertType.INFORMATION, "Adaugare cu succes", "Tema a fost adaugata cu success!", controller.StackPaneContent);
        } catch (ValidationException | RepositoryException e) {
            if (e.getMessage().toLowerCase().contains("id-ul"))
                GUIUtils.setWrongInput(FieldId, "Id-ul nu este valid");
            if (e.getMessage().toLowerCase().contains("descrierea"))
                GUIUtils.setWrongInput(FieldDescriere, "Descrierea nu este valida");
            if (e.getMessage().toLowerCase().contains("deadline-ul"))
                GUIUtils.setWrongInput(FakeFieldDeadline, "Deadline-ul nu este valid");
        }
    }

    private void handleEditButton(ActionEvent actionEvent) {
        GUIUtils.removeWrongInput(FakeFieldDeadline);

        int id = Integer.parseInt(FieldId.getText());

        int deadline = -1;
        if(FieldDeadline.getValue() != null)
            deadline = Utils.getWeekFromDate(FieldDeadline.getValue());

        try {
            controller.handleEditTema(id, deadline);
            controller.handleCloseWindowAdd();
            GUIUtils.showDialogMessage(Alert.AlertType.CONFIRMATION, "Modificare cu succes", "Tema a fost modificata cu success!", controller.StackPaneContent);
        } catch (ValidationException e) {
            if (e.getMessage().toLowerCase().contains("deadline"))
                GUIUtils.setWrongInput(FakeFieldDeadline, e.getMessage());
            else {
                controller.handleCloseWindowAdd();
                GUIUtils.showDialogMessage(Alert.AlertType.ERROR, "Eroare", e.getMessage(), controller.StackPaneContent);
            }
        }
    }
}
