package Utils;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;

import java.io.IOException;

public class GUIUtils {

    public static void showErrorMessage(String text) {
        Alert message = new Alert(Alert.AlertType.ERROR);
        message.setTitle("Eroare");
        message.setContentText(text);
        message.showAndWait();
    }

    public static void showMessage(Alert.AlertType type, String header, String text) {
        Alert message = new Alert(type);
        message.setHeaderText(header);
        message.setContentText(text);
        message.showAndWait();
    }

    public static void showDialogMessage(Alert.AlertType type, String header, String text, StackPane parent) {
        JFXDialog dialog = new JFXDialog();
        dialog.setBackground(Background.EMPTY);

        AnchorPane content = initMessageView(type, header, text, dialog);

        dialog.setContent(content);
        dialog.show(parent);
    }

    static AnchorPane initMessageView(Alert.AlertType type, String header, String text, JFXDialog parent) {
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefWidth(400);
        anchorPane.setPrefHeight(220);

        JFXButton button = new JFXButton();
        button.setStyle("-fx-padding: 0.7em 1em;" +
                "    -fx-font-size: 14px;" +
                "-fx-background-color: transparent");
        MaterialDesignIconView icon = new MaterialDesignIconView();
        icon.setIcon(MaterialDesignIcon.CLOSE);
        button.setGraphic(icon);

        button.setOnAction((p) -> parent.close());
        button.setDisableVisualFocus(true);

        Label title = new Label(header);
        title.setStyle("-fx-font-family: 'Roboto';" +
                "-fx-font-size: 26px");

        FontAwesomeIconView iconAlert = new FontAwesomeIconView();
        if (type == Alert.AlertType.WARNING)
            iconAlert.setIcon(FontAwesomeIcon.WARNING);
        else if (type == Alert.AlertType.CONFIRMATION)
            iconAlert.setIcon(FontAwesomeIcon.QUESTION_CIRCLE);
        else if (type == Alert.AlertType.INFORMATION)
            iconAlert.setIcon(FontAwesomeIcon.INFO_CIRCLE);
        else if (type == Alert.AlertType.ERROR)
            iconAlert.setIcon(FontAwesomeIcon.CLOSE);
        iconAlert.setSize("30px");

        Label content = new Label(text);
        content.setAlignment(Pos.CENTER_LEFT);
        content.setStyle("-fx-font-family: 'Roboto';" +
                "-fx-font-size: 15px;" +
                "-fx-text-fill: #6f6f6f");
        content.setWrapText(true);

        Line line = new Line(20d, 78d, 380d, 78d);
        line.setStyle("-fx-stroke: #c2c2c2");

        anchorPane.getChildren().addAll(line, button, content, title, iconAlert);
        AnchorPane.setTopAnchor(button, 20d);
        AnchorPane.setRightAnchor(button, 20d);
        AnchorPane.setTopAnchor(iconAlert, 27d);
        AnchorPane.setLeftAnchor(iconAlert, 25d);
        AnchorPane.setTopAnchor(title, 25d);
        AnchorPane.setLeftAnchor(title, 65d);
        AnchorPane.setLeftAnchor(content, 25d);
        AnchorPane.setRightAnchor(content, 25d);
        AnchorPane.setTopAnchor(content, 90d);
        AnchorPane.setBottomAnchor(content, 20d);

        return anchorPane;
    }

    public static void setWrongInput(JFXTextField textField, String message) {
        textField.clear();
        RequiredFieldValidator validator = new RequiredFieldValidator();
        validator.setMessage(message);
        validator.setIcon(new FontAwesomeIconView(FontAwesomeIcon.WARNING, "10px"));
        textField.getValidators().add(validator);
        textField.validate();
    }

    public static void setWrongInput(JFXPasswordField textField, String message) {
        textField.clear();
        RequiredFieldValidator validator = new RequiredFieldValidator();
        validator.setMessage(message);
        validator.setIcon(new FontAwesomeIconView(FontAwesomeIcon.WARNING, "10px"));
        textField.getValidators().add(validator);
        textField.validate();
    }

    public static void removeWrongInput(JFXTextField textField) {
        textField.resetValidation();
        if(textField.getValidators().size() != 0)
            textField.getValidators().remove(0);
        textField.setTooltip(null);
    }

    public static void removeWrongInput(JFXPasswordField textField) {
        textField.resetValidation();
        if(textField.getValidators().size() != 0)
            textField.getValidators().remove(0);
        textField.setTooltip(null);
    }
}
