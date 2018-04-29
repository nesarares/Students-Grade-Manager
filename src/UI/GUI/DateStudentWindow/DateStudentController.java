package UI.GUI.DateStudentWindow;

import Domain.Nota;
import Domain.Student;
import Service.NotaService;
import Service.StudentService;
import Service.TemaService;
import UI.GUI.AbstractController;
import Utils.ListEvent;
import Utils.Observer;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class DateStudentController extends AbstractController implements Observer<Nota> {
    private NotaService service;
    private TemaService temaService;
    private StudentService studentService;

    private String username;
    private Student student;

    private final ObservableList<Nota> model = FXCollections.observableArrayList();
    private final ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

    @FXML
    Label labelTitle;

    @FXML
    TableView<Nota> table;
    @FXML
    TableColumn<Nota, String> columnTema;
    @FXML
    TableColumn<Nota, String> columnNota;
    @FXML
    TableColumn<Nota, String> columnSaptamana;
    @FXML
    TableColumn<Nota, String> columnDeadline;

    @FXML
    Label labelMedie;
    @FXML
    Label labelMedieGrupa;
    @FXML
    PieChart chartNote;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();
        chartNote.setData(pieChartData);
    }

    public void setService(NotaService service) {
        this.service = service;
    }

    public void setServices(StudentService studentService, TemaService temaService) {
        this.studentService = studentService;
        this.temaService = temaService;
    }

    private void setDetails() {
        labelMedie.setText(String.format("%.2f", service.getMedia(student.getNume())));
        labelMedieGrupa.setText(String.format("%.2f", service.getMedia(student.getGrupa())));

        if (service.getMedia(student.getNume()) < 5)
            labelMedie.setStyle("-fx-text-fill: red");

        Map<Integer, Integer> noteStudent = new HashMap<>();
        for (Nota nota : service.filtreazaNoteKeyword("student: " + student.getNume(), service.getAll())) {
            Double n = nota.getNota();
            int nf = ((int) Math.floor(n));
            if (nf < 5) {
                noteStudent.putIfAbsent(1, 0);
                noteStudent.put(1, noteStudent.get(1) + 1);
            } else {
                noteStudent.putIfAbsent(nf, 0);
                noteStudent.put(nf, noteStudent.get(nf) + 1);
            }
        }

        for (Integer nota : noteStudent.keySet()) {
            if (nota == 1)
                pieChartData.add(new PieChart.Data("1-5", noteStudent.get(nota)));
            else if (nota == 10)
                pieChartData.add(new PieChart.Data("10", noteStudent.get(nota)));
            else
                pieChartData.add(new PieChart.Data(nota.toString() + "-" + (nota+1), noteStudent.get(nota)));
        }
    }

    public void setUsername(String username) {
        this.username = username;

        student = studentService.find(username);
        if (student != null) {
            labelTitle.setText("Date personale: " + student.getNume());
        }

        setDetails();
        model.setAll(service.filtreazaNoteKeyword("student: " + student.getNume(), service.getAll()));
    }

    @Override
    public void notifyEvent(ListEvent<Nota> event) {
        setDetails();
        model.setAll(service.filtreazaNoteKeyword("student: " + student.getNume(), event.getList()));
    }

    private void initTable() {
        columnTema.prefWidthProperty().bind(
                table.widthProperty()
                        .subtract(columnSaptamana.widthProperty())
                        .subtract(columnNota.widthProperty())
                        .subtract(columnDeadline.widthProperty())
                        .subtract(5)
        );

        columnTema.setCellValueFactory(cellData -> {
            Nota currentItem = cellData.getValue();
            String descriereTema = service.findTema(currentItem).get().getDescriere();
            return new ReadOnlyStringWrapper(descriereTema);
        });

        columnSaptamana.setCellValueFactory(new PropertyValueFactory<>("saptamana_predare"));
        columnNota.setCellValueFactory(new PropertyValueFactory<>("nota"));


        columnDeadline.setCellValueFactory(cellData -> {
            Nota currentItem = cellData.getValue();
            String deadline = String.valueOf(service.findTema(currentItem).get().getDeadline());
            return new ReadOnlyStringWrapper(deadline);
        });

        table.setItems(model);
    }
}
