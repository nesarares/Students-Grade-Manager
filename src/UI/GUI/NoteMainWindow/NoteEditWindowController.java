package UI.GUI.NoteMainWindow;

import Domain.Nota;
import Repository.RepositoryException;
import Utils.GUIUtils;
import Utils.Utils;
import Validator.ValidationException;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;

public class NoteEditWindowController implements Initializable {

    private NoteController controller;
    private Nota nota;

    @FXML
    JFXTextField fieldStudent;
    @FXML
    JFXTextField fieldTema;
    @FXML
    JFXTextField fieldNota;
    @FXML
    JFXTextField fakeFieldSaptamana;
    @FXML
    JFXDatePicker fieldSaptamana;
    @FXML
    TextArea textAreaObservatii;
    @FXML
    JFXButton editButton;

    public void setController(NoteController controller) {
        this.controller = controller;
    }

    public void setNota(Nota nota) {
        this.nota = nota;
        fieldStudent.setText(controller.service.findStudent(nota).get().getNume());
        fieldTema.setText(controller.service.findTema(nota).get().getDescriere());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // De adaugat nume student si descriere tema
        // + completat camp nota
    }

    void resetView() {
        GUIUtils.removeWrongInput(fieldNota);
        GUIUtils.removeWrongInput(fakeFieldSaptamana);

        fieldNota.clear();
        fieldSaptamana.getEditor().clear();
        textAreaObservatii.clear();
    }

    @FXML
    private void handleButtonEdit(ActionEvent actionEvent) {
        GUIUtils.removeWrongInput(fieldNota);
        GUIUtils.removeWrongInput(fakeFieldSaptamana);

        double notaNoua = -1;
        try {
            notaNoua = Double.parseDouble(fieldNota.getText());
        } catch (NumberFormatException e) {
            GUIUtils.setWrongInput(fieldNota, "Nota nu este valida.");
        }

        int saptamana = -1;
        if(fieldSaptamana.getValue() != null)
            saptamana = Utils.getWeekFromDate(fieldSaptamana.getValue());
        else
            GUIUtils.setWrongInput(fakeFieldSaptamana, "Data de predare nu este valida");

        String observatii = "";
        if(textAreaObservatii.getText() != null)
            observatii = textAreaObservatii.getText();

        try {
            controller.handleEditNota(nota.getId(), nota.getId_student(), nota.getId_tema(), notaNoua, saptamana, observatii);

            controller.handleCloseWindowAdd();
            GUIUtils.showDialogMessage(Alert.AlertType.INFORMATION, "Modificare cu succes", "Nota a fost modificata cu success!", controller.stackPaneContent);
        } catch (ValidationException e) {
            System.out.println("S-a aruncat exceptie");
            if (e.getMessage().toLowerCase().contains("nota"))
                GUIUtils.setWrongInput(fieldNota, "Nota nu este valida");
            if (e.getMessage().toLowerCase().contains("saptamana"))
                GUIUtils.setWrongInput(fakeFieldSaptamana, "Data de predare nu este valida");
        }
    }

    @FXML
    private void handleCancelButton(ActionEvent actionEvent) {
        controller.handleCloseWindowAdd();
    }
}
