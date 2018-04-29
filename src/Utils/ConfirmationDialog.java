package Utils;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;

import static Utils.GUIUtils.initMessageView;

public class ConfirmationDialog extends JFXDialog {
    AnchorPane root;
    EventHandler buttonOkHandler = (p) -> close();
    JFXButton buttonOK;

    public ConfirmationDialog(String header, String text) {
        super();
        setBackground(Background.EMPTY);
        root = initMessageView(Alert.AlertType.CONFIRMATION, header, text, this);

        HBox buttonBox = new HBox();
        JFXButton buttonClose = new JFXButton("Anulare");
        buttonOK = new JFXButton("OK");

        buttonClose.setStyle("-fx-padding: 0.7em 1em;" +
                "    -fx-font-size: 14px;" +
                "-fx-background-color: transparent;" +
                "-fx-text-fill: black");
        buttonOK.setStyle("-fx-padding: 0.7em 1em;" +
                "    -fx-font-size: 14px;" +
                "-fx-background-color: transparent;" +
                "-fx-text-fill: black");

        buttonClose.setOnAction((p) -> close());
        buttonOK.setOnAction(this::handleButtonOk);

        buttonBox.getChildren().addAll(buttonOK, buttonClose);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setSpacing(20d);

        root.getChildren().add(buttonBox);

        root.getChildren().remove(0);
        root.getChildren().remove(0);

        AnchorPane.setTopAnchor(root.getChildren().get(0), 70d);
        AnchorPane.setBottomAnchor(root.getChildren().get(0), 65d);
        AnchorPane.setBottomAnchor(buttonBox, 20d);
        AnchorPane.setRightAnchor(buttonBox, 20d);
        setContent(root);
    }

    private void handleButtonOk(ActionEvent actionEvent) {
        buttonOkHandler.handle(null);
        close();
    }

    public void setButtonOkHandler(EventHandler buttonOkHandler) {
        this.buttonOkHandler = buttonOkHandler;
    }
}