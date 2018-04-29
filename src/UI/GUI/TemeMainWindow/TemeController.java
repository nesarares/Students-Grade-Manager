package UI.GUI.TemeMainWindow;

import Domain.Tema;
import Repository.RepositoryException;
import Service.TemaService;
import UI.GUI.AbstractController;
import Utils.ConfirmationDialog;
import Utils.Transitions.FadeInUpTransition;
import Utils.GUIUtils;
import Utils.ListEvent;
import Utils.Observer;
import Utils.Transitions.FadeOutDownTransition;
import Validator.ValidationException;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRippler;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TemeController extends AbstractController implements Observer<Tema> {
    private TemaService service;

    private final ObservableList<Tema> model = FXCollections.observableArrayList();
    private Parent addView = null;
    private TemeEventWindowController eventController;

    @FXML
    StackPane StackPaneContent;
    @FXML
    AnchorPane contentPane;
    @FXML
    StackPane overlayPane;
    @FXML
    Group overlayGroup;
    @FXML
    JFXTextField searchBar;

    @FXML
    Pagination pagination;
    private int rowsPerPage = 8;

    @FXML
    TableView<Tema> table;
    @FXML
    TableColumn<Tema, String> ColumnId;
    @FXML
    TableColumn<Tema, String> ColumnDescriere;
    @FXML
    TableColumn<Tema, String> ColumnDeadline;
    @FXML
    TableColumn<Tema, Void> ColumnAction;

    @FXML
    ChoiceBox<Integer> ComboBoxRowCount;

    public void setService(TemaService service) {
        this.service = service;
        model.setAll(service.filtreazaTemeKeyword("", service.getAll()));
        setPaginationPages();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();
        searchBar.textProperty().addListener((obs, old_text, new_text) -> handleSearchBar());

        pagination.setPageFactory(this::createPage);
        ComboBoxRowCount.getItems().setAll(5,8,10,15);
        ComboBoxRowCount.setValue(8);
        handleComboBoxRowCount(null);

        ComboBoxRowCount.getSelectionModel().selectedItemProperty().addListener((
                (observable, oldValue, newValue) -> handleComboBoxRowCount(null)));

        FXMLLoader loader = new FXMLLoader(getClass().getResource("TemeEventWindowView.fxml"));
        try {
            addView = loader.load();

            eventController = loader.getController();
            eventController.setController(this);
        } catch (IOException e) {
            System.out.println("Eroare la incarcarea view-ului de adaugare");
        }

    }

    @Override
    public void notifyEvent(ListEvent<Tema> event) {
        searchBar.clear();
        model.setAll(service.filtreazaTemeKeyword("", event.getList()));

        setPaginationPages();
        pagination.setPageFactory(this::createPage);
    }

    @FXML
    private void handleSearchBar() {
        String keyword = searchBar.getText();
        model.setAll(service.filtreazaTemeKeyword(keyword, service.getAll()));

        setPaginationPages();
        pagination.setPageFactory(this::createPage);
    }

    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, model.size());
        table.setItems(FXCollections.observableList(model.subList(fromIndex, toIndex)));

        return new BorderPane(table);
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

    @FXML
    private void handleComboBoxRowCount(ActionEvent actionEvent) {
        rowsPerPage = ComboBoxRowCount.getValue();

        ComboBoxRowCount.setValue(rowsPerPage);

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

    private void initTable() {
        ColumnDescriere.prefWidthProperty().bind(
                table.widthProperty()
                        .subtract(ColumnId.widthProperty())
                        .subtract(ColumnDeadline.widthProperty())
                        .subtract(ColumnAction.widthProperty())
                        .subtract(5)
        );

        ColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        ColumnDescriere.setCellValueFactory(new PropertyValueFactory<>("descriere"));
        ColumnDeadline.setCellValueFactory(cellData -> {
            Tema current_item = cellData.getValue();
            return new ReadOnlyStringWrapper("Saptamana " + current_item.getDeadline());
        });

        ColumnAction.setCellFactory(param -> new TableCell<>() {
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
                editButton.setTooltip(new Tooltip("Editare Tema"));
                editButton.setGraphic(icon_edit);

                deleteButton.setId("ActionButton");
                deleteButton.setTooltip(new Tooltip("Stergere Tema"));
                deleteButton.setGraphic(icon_delete);

                editButton.setOnAction(event -> {
                    Tema tema = getTableRow().getItem();
                    handleButtonEdit(tema);
                });

                deleteButton.setOnAction(event -> {
                    Tema tema = getTableRow().getItem();
                    handleButtonDelete(tema);
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

    private void handleButtonEdit(Tema tema) {
        if (addView != null) {
            eventController.setEditWindow(tema);
            loadWindowAdd();
        }
    }

    private void handleButtonDelete(Tema tema) {
        ConfirmationDialog confirmationDialog = new ConfirmationDialog("Stergere tema", "Sunteti sigur ca vreti sa stergeti aceasta tema?");
        confirmationDialog.setButtonOkHandler((p) -> {
            Tema removed = service.remove(tema.getId());
            if (removed == null)
                GUIUtils.showDialogMessage(Alert.AlertType.WARNING,"Atentie", "Nu a fost stearsa tema cu acel ID.", StackPaneContent);
            else
                GUIUtils.showDialogMessage(Alert.AlertType.INFORMATION,"Stergere cu success", "Tema a fost stearsa cu success!", StackPaneContent);

        });
        confirmationDialog.show(StackPaneContent);
    }

    @FXML
    private void handleButtonAdd(ActionEvent actionEvent) {
        if (addView != null) {
            eventController.setAddWindow();
            loadWindowAdd();
        }
    }

    private void loadWindowAdd() {
        if (addView != null) {
            contentPane.effectProperty().setValue(new GaussianBlur(10));
            contentPane.setDisable(true);

            AnchorPane.setTopAnchor(overlayPane, 0d);
            AnchorPane.setLeftAnchor(overlayPane, 0d);
            AnchorPane.setRightAnchor(overlayPane, 0d);
            AnchorPane.setBottomAnchor(overlayPane, 0d);


            overlayGroup.getChildren().add(addView);
            new FadeInUpTransition(overlayGroup);
        }
    }

    void handleAddTema(int id, String descriere, int deadline) throws ValidationException, RepositoryException {
        service.add(new Tema(id, descriere, deadline));
    }

    void handleEditTema(int id, int deadline) throws ValidationException {
        service.updateDeadline(id, deadline);
    }

    @FXML
    void handleCloseWindowAdd() {
        new FadeOutDownTransition(overlayGroup).setOnFinished((p) -> {
            overlayGroup.getChildren().remove(0);

            contentPane.effectProperty().setValue(null);
            contentPane.setDisable(false);

            AnchorPane.setRightAnchor(overlayPane, null);
            AnchorPane.setBottomAnchor(overlayPane, null);
        });
    }
}
