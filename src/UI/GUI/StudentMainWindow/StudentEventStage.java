package UI.GUI.StudentMainWindow;

import Domain.Student;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class StudentEventStage {
    private AbstractStudentEventController controller;
    private String button_desc;
    private Student student;

    StackPane stack_pane_event;

    JFXTextField field_id = new JFXTextField();
    JFXTextField field_nume = new JFXTextField();
    JFXTextField field_grupa = new JFXTextField();
    JFXTextField field_email = new JFXTextField();
    JFXTextField field_profesor = new JFXTextField();

    GridPane main_pane = new GridPane();

    public StudentEventStage(AbstractStudentEventController controller, String button_desc, Student student) {
        this.controller = controller;
        this.button_desc = button_desc;
        this.student = student;

        initView();
    }

    private void initView() {
        AnchorPane main = new AnchorPane();
        stack_pane_event = new StackPane();

        Label title = initTitle();
        GridPane gridPane = initGridPane();
        HBox buttonsBox = initButtons();

        main.getChildren().addAll(title, gridPane, buttonsBox);

        AnchorPane.setTopAnchor(title, 0d);
        AnchorPane.setLeftAnchor(title, 0d);
        AnchorPane.setRightAnchor(title, 0d);
        AnchorPane.setTopAnchor(gridPane, 150d);
        AnchorPane.setLeftAnchor(gridPane, 30d);
        AnchorPane.setRightAnchor(gridPane, 30d);
        AnchorPane.setBottomAnchor(buttonsBox, 20d);
        AnchorPane.setRightAnchor(buttonsBox, 30d);

        int width = 400;
        int height = 600;

        stack_pane_event.setPrefWidth(width);
        stack_pane_event.setPrefHeight(height);
        stack_pane_event.setMinWidth(width);
        stack_pane_event.setMinHeight(height);
        stack_pane_event.setMaxWidth(width);
        stack_pane_event.setMaxHeight(height);

        Rectangle background = new Rectangle(0, 0, width, height);
        background.setFill(Color.WHITESMOKE);
        background.setStyle("-fx-stroke: black; -fx-stroke-width: 1px");

        stack_pane_event.getChildren().addAll(background, main);
        stack_pane_event.setStyle("-fx-stroke: black");

        stack_pane_event.getStylesheets().add(getClass().getResource("../stylesheet.css").toString());
    }

    private Label initTitle() {
        Label label = new Label();
        label.setId("WindowTitle");

        if(button_desc.equals("Adauga"))
            label.setText("Adaugare Student");
        else
            label.setText("Modificare Student");

        return label;
    }

    private GridPane initGridPane() {
        main_pane.setId("AddPane");

        field_id.setPromptText("Nr. matricol");
        field_nume.setPromptText("Nume");
        field_grupa.setPromptText("Grupa");
        field_email.setPromptText("E-mail");
        field_profesor.setPromptText("Profesor indrumator");

        main_pane.add(field_id, 0, 0);
        main_pane.add(field_nume, 0, 1);
        main_pane.add(field_grupa, 0, 2);
        main_pane.add(field_email, 0, 3);
        main_pane.add(field_profesor, 0, 4);

        if(student != null) {
            field_id.setText(student.getId().toString());
            field_id.setDisable(true);

            field_nume.setText(student.getNume());
            field_grupa.setText(student.getGrupa().toString());
            field_email.setText(student.getEmail());
            field_profesor.setText(student.getProf_indrumator());
        }

        ColumnConstraints c1 = new ColumnConstraints();
        c1.setHgrow(Priority.ALWAYS);
        c1.setHalignment(HPos.CENTER);

        main_pane.getColumnConstraints().addAll(c1);
        main_pane.setVgap(25d);
        main_pane.setHgap(15d);

        return main_pane;
    }

    private HBox initButtons() {
        HBox box = new HBox();
        JFXButton button_event = new JFXButton(button_desc);
        JFXButton button_cancel = new JFXButton("Anulare");

        button_event.setOnAction(controller::handleEvent);
        button_cancel.setOnAction(controller::handleCloseWindow);

        box.getChildren().addAll(button_event, button_cancel);
        box.setSpacing(15d);
        return box;
    }

    public Pane getView() {
        return stack_pane_event;
    }
}
