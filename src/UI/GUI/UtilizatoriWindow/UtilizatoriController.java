package UI.GUI.UtilizatoriWindow;

import Domain.User;
import Repository.RepositoryException;
import Service.UserService;
import Utils.*;
import Utils.Transitions.FadeOutDownTransition;
import Validator.ValidationException;
import com.jfoenix.controls.*;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ResourceBundle;

public class UtilizatoriController implements Initializable, Observer<User> {
    UserService service;
    private final ObservableList<User> model = FXCollections.observableArrayList();

    @FXML
    StackPane stackPaneContent;

    @FXML
    AnchorPane contentPopup;

    @FXML
    JFXTextField textFieldParolaImplicita;
    @FXML
    JFXTextField searchBar;
    @FXML
    Button buttonAdd;

    @FXML
    TableView<User> table;
    @FXML
    TableColumn<User, String> columnUser;
    @FXML
    TableColumn<User, User.UserType> columnTip;
    @FXML
    TableColumn<User, Void> columnAction;

    JFXPopup popup = new JFXPopup();
    @FXML
    JFXComboBox<User.UserType> comboBoxTip;
    @FXML
    JFXTextField textFieldNewUser;
    @FXML
    JFXTextField fakeFieldTip;
    @FXML
    JFXButton buttonAddUser;
    @FXML
    Button buttonCloseAddUser;

    public void setService(UserService service) {
        this.service = service;
        model.setAll(service.getAll());
    }

    @Override
    public void notifyEvent(ListEvent<User> event) {
        searchBar.clear();
        model.setAll(event.getList());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        textFieldParolaImplicita.setText(Utils.defaultPassword);
        initTable();

        searchBar.textProperty().addListener((o, n, m) -> handleSearchBar());

        popup.setPopupContent(contentPopup);
        comboBoxTip.getItems().addAll(User.UserType.admin, User.UserType.profesor);
    }

    private void handleSearchBar() {
        model.setAll(service.filtreazaUseriKeyword(searchBar.getText(), service.getAll()));
    }

    private void initTable() {
        columnUser.prefWidthProperty().bind(
                table.widthProperty()
                        .subtract(columnAction.widthProperty())
                        .divide(3)
                        .multiply(2)
                        .subtract(10)
        );
        columnTip.prefWidthProperty().bind(
                columnUser.prefWidthProperty().divide(2)
        );

        columnUser.setCellValueFactory(new PropertyValueFactory<>("username"));
        columnTip.setCellValueFactory(new PropertyValueFactory<>("type"));

        columnAction.setCellFactory(param -> new TableCell<>() {
            private Button resetPasswordButton = new Button();
            private Button deleteButton = new Button();
            HBox pane = new HBox(resetPasswordButton, deleteButton);

            {
                FontAwesomeIconView icon_reset = new FontAwesomeIconView();
                icon_reset.setIcon(FontAwesomeIcon.REFRESH);
                icon_reset.setFont(new Font(20));

                FontAwesomeIconView icon_delete = new FontAwesomeIconView();
                icon_delete.setIcon(FontAwesomeIcon.TRASH_ALT);
                icon_delete.setFont(new Font(21));

                pane.setAlignment(Pos.CENTER);
                pane.setSpacing(10d);
                resetPasswordButton.setId("ActionButton");
                resetPasswordButton.setTooltip(new Tooltip("Resetare Parola"));
                resetPasswordButton.setGraphic(icon_reset);

                deleteButton.setId("ActionButton");
                deleteButton.setTooltip(new Tooltip("Stergere Utilizaror"));
                deleteButton.setGraphic(icon_delete);

                resetPasswordButton.setOnAction(event -> {
                    User user = getTableRow().getItem();
                    handleButtonReset(user);
                });

                deleteButton.setOnAction(event -> {
                    User user = getTableRow().getItem();
                    handleButtonDelete(user);
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

    private void handleButtonReset(User user) {
        ConfirmationDialog confirmationDialog = new ConfirmationDialog("Resetare parola", "Sunteti sigur ca vreti sa resetati parola utilizatorului " + user.getUsername() + "?");
        confirmationDialog.setButtonOkHandler((p) -> {
            try {
                service.resetPassword(user);
            } catch (ValidationException e) {
                GUIUtils.showDialogMessage(Alert.AlertType.INFORMATION,"Modificare esuata", "Utilizatorului " + user.getUsername() + " nu i s-a putut reseta parola.", stackPaneContent);
            }
            GUIUtils.showDialogMessage(Alert.AlertType.INFORMATION,"Modificare cu success", "Utilizatorului " + user.getUsername() + " i-a fost modificata parola cu success!", stackPaneContent);
        });
        confirmationDialog.show(stackPaneContent);
    }

    private void handleButtonDelete(User user) {
        ConfirmationDialog confirmationDialog = new ConfirmationDialog("Stergere utilizator", "Sunteti sigur ca vreti sa stergeti acest utilizator?");
        confirmationDialog.setButtonOkHandler((p) -> {
            User removed = service.remove(user.getId());
            if (removed == null)
                GUIUtils.showDialogMessage(Alert.AlertType.WARNING,"Atentie", "Nu a fost sters utilizatorul cu acel ID.", stackPaneContent);
            else
                GUIUtils.showDialogMessage(Alert.AlertType.INFORMATION,"Stergere cu success", "Utilizatorul a fost stears cu success!", stackPaneContent);
        });
        confirmationDialog.show(stackPaneContent);
    }

    @FXML
    private void handleButtonAdd(ActionEvent actionEvent) {
        contentPopup.setVisible(true);
        popup.show(buttonAdd, JFXPopup.PopupVPosition.BOTTOM, JFXPopup.PopupHPosition.RIGHT);

        GUIUtils.removeWrongInput(textFieldNewUser);
        GUIUtils.removeWrongInput(fakeFieldTip);
        comboBoxTip.setValue(null);
    }

    @FXML
    private void handleButtonAddUser(ActionEvent actionEvent) {
        GUIUtils.removeWrongInput(textFieldNewUser);
        GUIUtils.removeWrongInput(fakeFieldTip);

        String user = textFieldNewUser.getText();
        User.UserType type = comboBoxTip.getValue();

        try {
            service.addUser(user, type);
            handleButtonCloseWindowAdd(null);
            GUIUtils.showDialogMessage(Alert.AlertType.INFORMATION, "Adaugare cu succes", "Utilizatorul a fost adaugat cu success!", stackPaneContent);
        } catch (ValidationException | RepositoryException e) {
            if (e.getMessage().toLowerCase().contains("user"))
                GUIUtils.setWrongInput(textFieldNewUser, "Utilizatorul nu este valid");
            if (e.getMessage().toLowerCase().contains("tip"))
                GUIUtils.setWrongInput(fakeFieldTip, "Tipul nu este valid");
        }
    }

    @FXML
    private void handleButtonCloseWindowAdd(ActionEvent actionEvent) {
        popup.hide();
    }
}
