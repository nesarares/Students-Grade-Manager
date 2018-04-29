package UI.GUI.StudentMainWindow;

import Service.StudentService;
import Utils.GUIUtils;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;

public abstract class AbstractStudentEventController {
    protected StudentService service;
    protected StudentEventStage view;
    protected StudentController controllerStudent;

    public AbstractStudentEventController(StudentService service, StudentController controller) {
        this.service = service;
        this.controllerStudent = controller;
    }

    public void setView(StudentEventStage view) {
        this.view = view;
    }

    public abstract void handleEvent (ActionEvent actionEvent);

    public void handleCloseWindow (ActionEvent actionEvent) {
        controllerStudent.handleCloseWindowEvent();
    }

    protected void setWrongInput(JFXTextField textField, String message) {
        GUIUtils.setWrongInput(textField, message);
    }

    protected void removeWrongInput(JFXTextField textField) {
        GUIUtils.removeWrongInput(textField);
    }
}

