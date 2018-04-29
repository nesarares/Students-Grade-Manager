package UI.GUI.StudentMainWindow;

import Domain.Student;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.util.Callback;

public class StudentView {
    private StudentController controller;

    private AnchorPane main_pane;
    AnchorPane content_pane;
    StackPane stack_pane_content;
    StackPane overlay_pane;
    private GridPane main_grid;

    Pagination pagination = new Pagination();
    int rows_per_page;
    JFXTextField search = new JFXTextField();
    TableView<Student> table = new TableView<>();
    private double row_height = 40;
    ChoiceBox rows_choice_box = new ChoiceBox();

    private class CheckBoxCell extends TableCell<Student, Void> {
        CheckBox box = new CheckBox();

        {
            box.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) table.getSelectionModel().select(getTableRow().getIndex());
                else table.getSelectionModel().clearSelection(getTableRow().getIndex());
            });
        }

        @Override
        protected void updateItem(Void item, boolean empty) {
            super.updateItem(item, empty);
            setGraphic(empty ? null : box);
        }

        public CheckBox getBox() {
            return this.box;
        }
    }

    public StudentView(StudentController controller) {
        this.controller = controller;
        initView();
    }

    private void initView() {
        main_pane = new AnchorPane();
        main_pane.setId("Main");

        content_pane = new AnchorPane();

        // CREATE MAIN GRID PANE
        main_grid = new GridPane();
        main_grid.setId("MainGrid");

        // TITLE
        Label title = createLabel("Operatii Studenti", "Header");

        // TABLE
        initTable();

        // SEARCH BOX AND BUTTONS BOX
        VBox search_box = initSearchBox();
        HBox buttons_box = initButtonsBox();

        // PAGINATION BOX
        Pane pagination_box = initPaginationBox();

        // ADD THE COMPONENTS
        main_grid.add(search_box, 0, 0);
        main_grid.add(pagination_box, 0, 1);
        main_grid.add(buttons_box, 0, 2);

        // STYLE THE MAIN PANE
        ColumnConstraints c1 = new ColumnConstraints();
        c1.setHgrow(Priority.ALWAYS);

        // STYLE BASED ON RESOLUTION
        if (Screen.getPrimary().getVisualBounds().getWidth() < 1100)
            setRows_per_page("5");

        GridPane.setHalignment(title, HPos.CENTER);
        main_grid.setAlignment(Pos.CENTER);
        main_grid.setPadding(new Insets(20, 20, 20, 20));
        main_grid.setVgap(20d);
        main_grid.getColumnConstraints().addAll(c1);

        // ADD COMPONENTS TO THE CONTENT PANE

        content_pane.getChildren().addAll(title, main_grid);
        AnchorPane.setTopAnchor(title, 0d);
        AnchorPane.setLeftAnchor(title, 0d);
        AnchorPane.setRightAnchor(title, 0d);
        AnchorPane.setTopAnchor(main_grid, 115d);
        AnchorPane.setLeftAnchor(main_grid, 0d);
        AnchorPane.setRightAnchor(main_grid, 0d);
        AnchorPane.setBottomAnchor(main_grid, 0d);

        stack_pane_content = new StackPane(content_pane);
        overlay_pane = new StackPane();
//        overlay_pane.setPrefHeight(10d);
//        overlay_pane.setPrefWidth(10d);

        main_pane.getChildren().addAll(stack_pane_content, overlay_pane);
        AnchorPane.setTopAnchor(stack_pane_content, 0d);
        AnchorPane.setLeftAnchor(stack_pane_content, 0d);
        AnchorPane.setRightAnchor(stack_pane_content, 0d);
        AnchorPane.setTopAnchor(overlay_pane, 0d);
        AnchorPane.setLeftAnchor(overlay_pane, 0d);
    }

    public void setRows_per_page(String rows) {
        if(! rows.toLowerCase().equals("auto")) {
            this.rows_per_page = Integer.valueOf(rows);

            rows_choice_box.setValue(rows);

            int numOfPages = 1;
            if (controller.getModel().size() % rows_per_page == 0) {
                numOfPages = controller.getModel().size() / rows_per_page;
            } else if (controller.getModel().size() > rows_per_page) {
                numOfPages = controller.getModel().size() / rows_per_page + 1;
            }
            pagination.setPageCount(numOfPages);

            pagination.setPageFactory(controller::createPage);

            // PAGINATION HEIGHT
            double table_height = 60 + rows_per_page * row_height;
            table.setMinHeight(table_height);
            table.setMaxHeight(table_height);
            pagination.setMinHeight(table_height + 80);
            pagination.setMaxHeight(table_height + 80);
        } else {

        }
    }

    private Pane initPaginationBox() {
        setRows_per_page("8");
        StackPane pagination_box = new StackPane(pagination);

        Label label = new Label("Randuri pe pagina:");
        rows_choice_box.setItems(FXCollections.observableArrayList("5", "8", "10", "15"));
        rows_choice_box.setValue("8");
        rows_choice_box.setId("RowsChoiceBox");

        rows_choice_box.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> setRows_per_page(newValue.toString())));

        pagination_box.getChildren().addAll(rows_choice_box, label);

        StackPane.setAlignment(rows_choice_box, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(rows_choice_box, new Insets(0, 10, 20, 0));

        StackPane.setAlignment(label, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(label, new Insets(0, 80, 30, 0));


        pagination.currentPageIndexProperty().addListener((obs, oldValue, newValue) -> table.refresh());

        return pagination_box;
    }

    private VBox initSearchBox() {
        VBox search_box = new VBox();
        search_box.setFillWidth(true);

        search_box.setId("SearchBox");
//        Label search_label = new Label("Search by keyword");
//        search_label.setId("SearchLabel");

        GridPane hbox = new GridPane();
        hbox.setAlignment(Pos.CENTER);
        hbox.setHgap(20d);

        Label search_icon = new Label();
        FontAwesomeIconView icon = new FontAwesomeIconView();
        icon.setIcon(FontAwesomeIcon.SEARCH);
        search_icon.setGraphic(icon);

        search_icon.setId("SearchIcon");
        search_icon.setTooltip(new Tooltip("Cautare dupa cuvinte cheie: " +
                "\n- cuvant" +
                "\n- Nume: cuvant" +
                "\n- Grupa: cuvant" +
                "\n- Prof: cuvant"));

        search.setId("SearchBar");
        search.setPromptText("Cautare dupa cuvant cheie");
        search.setLabelFloat(true);
        search.textProperty().addListener((obs, old_text, new_text) -> controller.handleSearchBar());
        hbox.add(search_icon, 0, 0);
        hbox.add(search, 1, 0);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPrefWidth(20d);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.ALWAYS);

        hbox.getColumnConstraints().addAll(col1, col2);

        search_box.getChildren().addAll(hbox);

        return search_box;
    }

    private HBox initButtonsBox() {
        HBox buttons_box = new HBox();

        Button buttonAdd = createButton("Add");
        Button buttonDeleteSelected = createButton("Delete");
        Button buttonClear = createButton("Clear");

        buttonAdd.setOnAction(controller::handleButtonAdd);
        buttonDeleteSelected.setOnAction(controller::handleButtonDeleteSelected);
        buttonClear.setOnAction(controller::handleButtonClear);

        buttons_box.setId("ButtonActionBottom");
        buttons_box.setSpacing(20d);
        buttons_box.setAlignment(Pos.CENTER_RIGHT);
        buttons_box.getChildren().addAll(buttonAdd, buttonDeleteSelected, buttonClear);

        return buttons_box;
    }

    private void initTable() {
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        TableColumn<Student, Void> column_check = new TableColumn<>("");
        TableColumn<Student, String> column_id = new TableColumn<>("Nr. Mat.");
        TableColumn<Student, String> column_nume = new TableColumn<>("Nume");
        TableColumn<Student, String> column_grupa = new TableColumn<>("Grupa");
        TableColumn<Student, String> column_email = new TableColumn<>("Email");
        TableColumn<Student, String> column_profesor = new TableColumn<>("Prof. Indrumator");
        TableColumn<Student, Void> column_action = new TableColumn<>("Actiune");

        column_check.setPrefWidth(30d);
        column_id.setPrefWidth(100d);
        column_grupa.setPrefWidth(100d);
        column_action.setPrefWidth(120d);
        column_nume.prefWidthProperty().bind(
                table.widthProperty()
                .subtract(column_check.widthProperty())
                .subtract(column_id.widthProperty())
                .subtract(column_grupa.widthProperty())
                .subtract(column_profesor.widthProperty())
                .subtract(column_action.widthProperty())
                .subtract(4)
                .divide(3)
                .multiply(2)
        );

        column_email.prefWidthProperty().bind(
                column_nume.prefWidthProperty()
                .divide(2)
        );

        column_check.setCellFactory(param -> new CheckBoxCell());
        column_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        column_nume.setCellValueFactory(new PropertyValueFactory<>("nume"));
        column_grupa.setCellValueFactory(new PropertyValueFactory<>("grupa"));
        column_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        column_profesor.setCellValueFactory(new PropertyValueFactory<>("prof_indrumator"));
        column_action.setCellFactory(param -> new TableCell<>() {
            private Button editButton = new Button();
            private Button deleteButton = new Button();

            HBox pane = new HBox(editButton, deleteButton);

            {
                pane.setAlignment(Pos.CENTER);
                pane.setSpacing(10d);

                FontAwesomeIconView icon_edit = new FontAwesomeIconView();
                icon_edit.setIcon(FontAwesomeIcon.PENCIL);
                icon_edit.setFont(new Font(20));

                FontAwesomeIconView icon_delete = new FontAwesomeIconView();
                icon_delete.setIcon(FontAwesomeIcon.TRASH_ALT);
                icon_delete.setFont(new Font(21));

                editButton.setId("ActionButton");
                editButton.setTooltip(new Tooltip("Editare Student"));
                editButton.setGraphic(icon_edit);

                deleteButton.setId("ActionButton");
                deleteButton.setTooltip(new Tooltip("Stergere Student"));
                deleteButton.setGraphic(icon_delete);

                editButton.setOnAction(new EventHandler<>() {
                    @Override
                    public void handle(ActionEvent event) {
                        Student student = getTableRow().getItem();
                        controller.handleButtonEdit(student);
                    }
                });

                deleteButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        Student student = getTableRow().getItem();
                        controller.handleButtonDelete(student);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                setGraphic(empty ? null : pane);
            }
        });

        column_nume.setSortType(TableColumn.SortType.ASCENDING);

        table.getColumns().addAll(column_check, column_id, column_nume, column_grupa, column_email, column_profesor, column_action);
        table.setItems(controller.getModel());
        table.getSortOrder().add(column_nume);

        if (Screen.getPrimary().getVisualBounds().getWidth() < 1024) {
            table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        }

        addTooltipToColumnCells(column_email);
        addTooltipToColumnCells(column_nume);
        addTooltipToColumnCells(column_profesor);
    }

    private <T> void addTooltipToColumnCells(TableColumn<Student, T> column) {
        Callback<TableColumn<Student, T>, TableCell<Student,T>> existingCellFactory
                = column.getCellFactory();

        column.setCellFactory(c -> {
            TableCell<Student, T> cell = existingCellFactory.call(c);

            Tooltip tooltip = new Tooltip();
            tooltip.textProperty().bind(cell.itemProperty().asString());
            cell.setTooltip(tooltip);

            return cell ;
        });
    }

    public Pane getView() {
        return main_pane;
    }

    private Label createLabel(String descriere, String id) {
        Label label = new Label(descriere);
        label.setId(id);
        label.setAlignment(Pos.CENTER);
        return label;
    }

    private Button createButton(String type) {
        Button button = new Button();
        FontAwesomeIconView icon = new FontAwesomeIconView();
        MaterialDesignIconView icon_material = new MaterialDesignIconView();

        switch (type) {
            case "Add":
                icon_material.setIcon(MaterialDesignIcon.PLUS);
                button.setGraphic(icon_material);

                button.setId("ButtonAdd");
                button.setTooltip(new Tooltip("Adaugare"));
                break;
            case "Delete":
                icon.setIcon(FontAwesomeIcon.TRASH_ALT);
                button.setGraphic(icon);

                button.setId("ButtonDelete");
                button.setTooltip(new Tooltip("Stergere selectate"));
                break;
            case "Clear":
                icon_material.setIcon(MaterialDesignIcon.ARROW_LEFT);
                button.setGraphic(icon_material);

                button.setId("ButtonClear");
                button.setTooltip(new Tooltip("Resetare elemente"));
                break;
        }
        return button;
    }
}
