package UI.GUI.NoteMainWindow;

import Domain.Student;
import Domain.Tema;
import Repository.RepositoryException;
import Utils.GUIUtils;
import Utils.ListEvent;
import Utils.Observer;
import Utils.Utils;
import Validator.ValidationException;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.util.Callback;

import java.net.URL;
import java.util.ResourceBundle;

public class NoteAddWindowController implements Initializable {
    @FXML
    JFXTextField searchBarStudent;
    @FXML
    JFXTextField searchBarTema;
    @FXML
    JFXTextField fieldNota;
    @FXML
    JFXTextField fakeFieldSaptamana;
    @FXML
    JFXTextField fakeFieldStudent;
    @FXML
    JFXTextField fakeFieldTema;
    @FXML
    JFXDatePicker fieldSaptamana;
    @FXML
    TextArea textAreaObservatii;

    @FXML
    ListView<Student> listViewStudenti;
    @FXML
    ObservableList<Student> modelStudenti = FXCollections.observableArrayList();

    @FXML
    ListView<Tema> listViewTema;
    @FXML
    ObservableList<Tema> modelTeme = FXCollections.observableArrayList();

    private NoteController controller;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        searchBarStudent.textProperty().addListener((o, n, m) -> handleSearchBarStudent(null));
        searchBarTema.textProperty().addListener((o, n, m) -> handleSearchBarTema(null));
    }

    public void setController(NoteController controller) {
        this.controller = controller;
        initLists();
    }

    @FXML
    private void handleSearchBarStudent(ActionEvent actionEvent) {
        String student = searchBarStudent.getText().toLowerCase();
        modelStudenti.setAll(controller.studentService.filtreazaStudentiKeyword("nume: " + student, controller.studentService.getAll()));
    }

    @FXML
    private void handleSearchBarTema(ActionEvent actionEvent) {
        String tema = searchBarTema.getText().toLowerCase();
        modelTeme.setAll(controller.temaService.filtreazaTemeKeyword("descriere: " + tema, controller.temaService.getAll()));
    }

    void resetView() {
        updateModels();

        GUIUtils.removeWrongInput(fieldNota);
        GUIUtils.removeWrongInput(fakeFieldSaptamana);
        GUIUtils.removeWrongInput(fakeFieldStudent);
        GUIUtils.removeWrongInput(fakeFieldTema);

        fieldSaptamana.getEditor().clear();
        fieldNota.clear();
        searchBarTema.clear();
        searchBarStudent.clear();
        textAreaObservatii.clear();
    }

    void updateModels() {
        searchBarStudent.clear();
        searchBarTema.clear();
        modelStudenti.setAll(controller.studentService.getAll());
        modelTeme.setAll(controller.temaService.getAll());
    }

    private void initLists() {

        updateModels();

        listViewTema.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Tema> call(ListView<Tema> param) {
                return new ListCell<>() {

                    @Override
                    protected void updateItem(Tema item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText(null);
                            setGraphic(null);
                        }
                        else {
                            setText(item.getDescriere());
                            setGraphic(null);
                        }
                    }
                };
            }
        });

        listViewStudenti.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Student> call(ListView<Student> param) {
                return new ListCell<>() {

                    @Override
                    protected void updateItem(Student item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText(null);
                            setGraphic(null);
                        }
                        else {
                            setText(item.getNume());
                            setGraphic(null);
                        }
                    }
                };
            }
        });

        listViewStudenti.setItems(modelStudenti);
        listViewTema.setItems(modelTeme);
    }

    @FXML
    private void handleButtonAdd(ActionEvent actionEvent) {
        GUIUtils.removeWrongInput(fieldNota);
        GUIUtils.removeWrongInput(fakeFieldSaptamana);
        GUIUtils.removeWrongInput(fakeFieldStudent);
        GUIUtils.removeWrongInput(fakeFieldTema);

        int idStudent = -1;
        if (listViewStudenti.getSelectionModel().getSelectedItem() != null)
            idStudent = listViewStudenti.getSelectionModel().getSelectedItem().getId();
        else
            GUIUtils.setWrongInput(fakeFieldStudent, "Trebuie selectat un student");

        int idTema = -1;
        if (listViewTema.getSelectionModel().getSelectedItem() != null)
            idTema = listViewTema.getSelectionModel().getSelectedItem().getId();
        else
            GUIUtils.setWrongInput(fakeFieldTema, "Trebuie selectata o tema");

        double nota = -1;
        try {
            nota = Double.parseDouble(fieldNota.getText());
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
            controller.handleAddNota(idStudent, idTema, nota, saptamana, observatii);

            controller.handleCloseWindowAdd();
            GUIUtils.showDialogMessage(Alert.AlertType.INFORMATION, "Adaugare cu succes", "Nota a fost adaugata cu success!", controller.stackPaneContent);
        } catch (ValidationException | RepositoryException e) {
            if (e.getMessage().toLowerCase().contains("are deja o nota")){
                GUIUtils.showDialogMessage(Alert.AlertType.ERROR, "Adaugare nereusita", "Studentul are deja o nota la aceasta tema.", controller.overlayPane);
            }
            else {
                if (e.getMessage().toLowerCase().contains("student"))
                    GUIUtils.setWrongInput(fakeFieldStudent, "Trebuie selectat un student");
                if (e.getMessage().toLowerCase().contains("tema"))
                    GUIUtils.setWrongInput(fakeFieldTema, "Trebuie selectata o tema");
                if (e.getMessage().toLowerCase().contains("nota"))
                    GUIUtils.setWrongInput(fieldNota, "Nota nu este valida");
                if (e.getMessage().toLowerCase().contains("saptamana"))
                    GUIUtils.setWrongInput(fakeFieldSaptamana, "Data de predare nu este valida");
            }
        }
    }

    @FXML
    private void handleButtonClose(ActionEvent actionEvent) {
        controller.handleCloseWindowAdd();
    }
}
