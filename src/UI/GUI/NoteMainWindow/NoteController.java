package UI.GUI.NoteMainWindow;

import Domain.Nota;
import Repository.RepositoryException;
import Service.NotaService;
import Service.StudentService;
import Service.TemaService;
import UI.GUI.AbstractController;
import Utils.*;
import Utils.Transitions.FadeInUpTransition;
import Utils.Transitions.FadeOutDownTransition;
import Validator.ValidationException;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXRippler;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class NoteController extends AbstractController implements Observer<Nota> {
    NotaService service;
    StudentService studentService;
    TemaService temaService;

    private final ObservableList<Nota> model = FXCollections.observableArrayList();

    private AnchorPane addView;
    private AnchorPane editView;
    private NoteAddWindowController addController;
    private NoteEditWindowController editController;

    @FXML
    JFXTextField searchBar;

    @FXML
    StackPane stackPaneContent;
    @FXML
    AnchorPane contentPane;
    @FXML
    StackPane overlayPane;

    @FXML
    Group overlayGroup;

    @FXML
    TableView<Nota> table;

    @FXML
    Pagination pagination;
    private int rowsPerPage = 8;

    @FXML
    ChoiceBox<Integer> choiceBoxRowConut;

    @FXML
    TableColumn<Nota, String> columnStudent;
    @FXML
    TableColumn<Nota, String> columnTema;
    @FXML
    TableColumn<Nota, String> columnNota;
    @FXML
    TableColumn<Nota, Integer> columnSaptamanaPredare;
    @FXML
    TableColumn<Nota, Void> columnActiune;

    public void setService(NotaService service) {
        this.service = service;
        model.setAll(service.filtreazaNoteKeyword("", service.getAll()));
        setPaginationPages();
    }

    public void setServices(StudentService studentService, TemaService temaService) {
        this.temaService = temaService;
        this.studentService = studentService;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("NoteAddWindow.fxml"));
        try {
            addView = loader.load();
            addController = loader.getController();
            addController.setController(this);
        } catch (IOException e) {
            System.out.println("Eroare la incarcarea view-ului de adaugare.");
        }

        FXMLLoader loader2 = new FXMLLoader(getClass().getResource("NoteEditWindow.fxml"));
        try {
            editView = loader2.load();
            editController = loader2.getController();
            editController.setController(this);
        } catch (IOException e) {
            System.out.println("Eroare la incarcarea view-ului de editare.");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();

        searchBar.textProperty().addListener((obs, oldValue, newValue) -> handleSearchBar());

        pagination.setPageFactory(this::createPage);
        choiceBoxRowConut.getItems().setAll(5,8,10,15);
        choiceBoxRowConut.setValue(8);
        handleComboBoxRowCount(null);

        choiceBoxRowConut.getSelectionModel().selectedItemProperty().addListener((
                (observable, oldValue, newValue) -> handleComboBoxRowCount(null)));
    }

    @Override
    public void notifyEvent(ListEvent<Nota> event) {
        searchBar.clear();
        model.setAll(service.filtreazaNoteKeyword("", event.getList()));

        setPaginationPages();
        pagination.setPageFactory(this::createPage);
    }

    @FXML
    private void handleSearchBar() {
        String keyword = searchBar.getText().toLowerCase();
        model.setAll(service.filtreazaNoteKeyword(keyword, service.getAll()));

        setPaginationPages();
        pagination.setPageFactory(this::createPage);
    }

    @FXML
    private void handleComboBoxRowCount(ActionEvent actionEvent) {
        rowsPerPage = choiceBoxRowConut.getValue();

        choiceBoxRowConut.setValue(rowsPerPage);

        setPaginationPages();
        pagination.setPageFactory(this::createPage);

        // PAGINATION HEIGHT
        double row_height = 40;
        double table_height = 60 + rowsPerPage * row_height;
        table.setMinHeight(table_height);
        table.setMaxHeight(table_height);
        pagination.setMinHeight(table_height + 80);
        pagination.setMaxHeight(table_height + 80);
    }

    private void setPaginationPages() {
        int numOfPages = 1;
        if (model.size() % rowsPerPage == 0) {
            numOfPages = model.size() / rowsPerPage;
        } else if (model.size() > rowsPerPage) {
            numOfPages = model.size() / rowsPerPage + 1;
        }

        pagination.setPageCount(numOfPages);
    }

    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, model.size());
        table.setItems(FXCollections.observableList(model.subList(fromIndex, toIndex)));

        return new BorderPane(table);
    }

    private void initTable() {
        columnStudent.prefWidthProperty().bind(
                table.widthProperty()
                        .subtract(columnNota.widthProperty())
                        .subtract(columnActiune.widthProperty())
                        .subtract(columnSaptamanaPredare.widthProperty())
                        .subtract(4)
                        .divide(2)
        );

        columnTema.prefWidthProperty().bind(columnStudent.prefWidthProperty());

        columnStudent.setCellValueFactory(cellData -> {
            Nota currentItem = cellData.getValue();
            String numeStudent = service.findStudent(currentItem).get().getNume();
            return new ReadOnlyStringWrapper(numeStudent);
        });

        columnTema.setCellValueFactory(cellData -> {
            Nota currentItem = cellData.getValue();
            String descriereTema = service.findTema(currentItem).get().getDescriere();
            return new ReadOnlyStringWrapper(descriereTema);
        });

        columnSaptamanaPredare.setCellValueFactory(new PropertyValueFactory<>("saptamana_predare"));
        columnNota.setCellValueFactory(new PropertyValueFactory<>("nota"));

        columnActiune.setCellFactory(param -> new TableCell<>() {
            private Button editButton = new Button();
            private Button deleteButton = new Button();
            HBox pane = new HBox(editButton, deleteButton);

            {
                FontAwesomeIconView icon_edit = new FontAwesomeIconView();
                icon_edit.setIcon(FontAwesomeIcon.PENCIL);
                icon_edit.setFont(new Font(20));

                FontAwesomeIconView icon_delete = new FontAwesomeIconView();
                icon_delete.setIcon(FontAwesomeIcon.TRASH_ALT);
                icon_delete.setFont(new Font(21));

                pane.setAlignment(Pos.CENTER);
                pane.setSpacing(10d);
                editButton.setId("ActionButton");
                editButton.setTooltip(new Tooltip("Editare Nota"));
                editButton.setGraphic(icon_edit);

                deleteButton.setId("ActionButton");
                deleteButton.setTooltip(new Tooltip("Stergere Nota"));
                deleteButton.setGraphic(icon_delete);

                editButton.setOnAction(event -> {
                    Nota nota = getTableRow().getItem();
                    handleButtonEdit(nota);
                });

                deleteButton.setOnAction(event -> {
                    Nota nota = getTableRow().getItem();
                    handleButtonDelete(nota);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });

        table.setItems(model);
    }

    private void handleButtonDelete(Nota nota) {
        ConfirmationDialog confirmationDialog = new ConfirmationDialog("Stergere nota", "Sunteti sigur ca vreti sa stergeti aceasta nota?");
        confirmationDialog.setButtonOkHandler((p) -> {
            Nota removed = service.remove(nota.getId());
            if (removed == null)
                GUIUtils.showDialogMessage(Alert.AlertType.WARNING,"Atentie", "Nu a fost stearsa nota cu acel ID.", stackPaneContent);
            else
                GUIUtils.showDialogMessage(Alert.AlertType.INFORMATION,"Stergere cu success", "Nota a fost stearsa cu success!", stackPaneContent);

        });
        confirmationDialog.show(stackPaneContent);
    }

    private void handleButtonEdit(Nota nota) {
        if(editView != null) {
            editController.resetView();
            editController.setNota(nota);
            contentPane.effectProperty().setValue(new GaussianBlur(10));
            contentPane.setDisable(true);

            AnchorPane.setTopAnchor(overlayPane, 0d);
            AnchorPane.setLeftAnchor(overlayPane, 0d);
            AnchorPane.setRightAnchor(overlayPane, 0d);
            AnchorPane.setBottomAnchor(overlayPane, 0d);

            overlayGroup.getChildren().add(editView);
            new FadeInUpTransition(overlayGroup);
        }
    }

    @FXML
    private void handleButtonAdd(ActionEvent actionEvent) {
        if(addView != null) {
            addController.resetView();
            contentPane.effectProperty().setValue(new GaussianBlur(10));
            contentPane.setDisable(true);

            AnchorPane.setTopAnchor(overlayPane, 0d);
            AnchorPane.setLeftAnchor(overlayPane, 0d);
            AnchorPane.setBottomAnchor(overlayPane, 0d);
            AnchorPane.setRightAnchor(overlayPane, 0d);

            overlayGroup.getChildren().add(addView);
            new FadeInUpTransition(overlayGroup);
        }
    }

    void handleCloseWindowAdd() {
        new FadeOutDownTransition(overlayGroup).setOnFinished((p) -> {
            overlayGroup.getChildren().remove(0);

            contentPane.setCache(true);
            contentPane.setCacheHint(CacheHint.SPEED);
            contentPane.effectProperty().setValue(null);
            contentPane.setDisable(false);
            contentPane.setCache(false);

            AnchorPane.setBottomAnchor(overlayPane, null);
            AnchorPane.setRightAnchor(overlayPane, null);
        });
    }

    void handleAddNota(int idStudent, int idTema, double notaN, int saptamanaPredare, String observatii) throws ValidationException, RepositoryException {
        Nota nota = new Nota(0, idStudent, idTema, notaN, saptamanaPredare);
        service.add(nota, observatii);
    }

    void handleEditNota(int id, int idStudent, int idTema, double notaNoua, int saptamanaPredare, String observatii) throws ValidationException {
        Nota notaModificare = new Nota(id, idStudent, idTema, notaNoua, saptamanaPredare);
        service.update(notaModificare, observatii);
    }
}
