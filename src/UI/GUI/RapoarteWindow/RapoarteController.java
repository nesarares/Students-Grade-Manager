package UI.GUI.RapoarteWindow;

import Service.ReportService;
import Utils.GUIUtils;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Separator;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class RapoarteController implements Initializable {
    private ReportService service;

    @FXML
    StackPane stackPaneContent;
    @FXML
    JFXCheckBox checkBoxMedia;
    @FXML
    JFXCheckBox checkBoxStudentiExamen;
    @FXML
    JFXCheckBox checkBoxGraficNote;
    @FXML
    JFXCheckBox checkBoxGraficGrupa;
    @FXML
    JFXComboBox<String> comboBoxGrupa;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        comboBoxGrupa.getItems().add("Toate grupele");
        comboBoxGrupa.setValue("Toate grupele");
    }

    public void setService(ReportService reportService) {
        this.service = reportService;

        service.getStudentService().getGrupe().forEach(g -> comboBoxGrupa.getItems().add(String.valueOf(g)));
    }

    @FXML
    private void handleButtonClear(ActionEvent actionEvent) {
        comboBoxGrupa.setValue("Toate grupele");
        checkBoxGraficGrupa.setSelected(false);
        checkBoxGraficNote.setSelected(false);
        checkBoxMedia.setSelected(false);
        checkBoxStudentiExamen.setSelected(false);
    }

    @FXML
    private void handleButtonGenerate(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Salvare raport.");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF (*.pdf)", "*.pdf");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setInitialFileName("Raport");

        String userDirectoryString = System.getProperty("user.home");
        File userDirectory = new File(userDirectoryString);
        if(userDirectory.canRead()) {
            fileChooser.setInitialDirectory(userDirectory);
        }

        File file = fileChooser.showSaveDialog(comboBoxGrupa.getScene().getWindow());
        if (file != null) {
            try {
                String grupa = comboBoxGrupa.getValue();
                service.generateReport(file, grupa, checkBoxMedia.isSelected(), checkBoxStudentiExamen.isSelected(), checkBoxGraficNote.isSelected(), checkBoxGraficGrupa.isSelected());
                GUIUtils.showDialogMessage(Alert.AlertType.INFORMATION, "Succes!", "Raportul a fost generat cu succes!", stackPaneContent);
            } catch (Exception ex) {
                GUIUtils.showDialogMessage(Alert.AlertType.ERROR, "Eroare", ex.getMessage(), stackPaneContent);
            }
        }
    }
}
