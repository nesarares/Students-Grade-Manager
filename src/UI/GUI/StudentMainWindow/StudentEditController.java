package UI.GUI.StudentMainWindow;

import Domain.Student;
import Service.StudentService;
import Utils.GUIUtils;
import Validator.ValidationException;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

public class StudentEditController extends AbstractStudentEventController {
    public StudentEditController(StudentService service, StudentController controller) {
        super(service, controller);
    }

    @Override
    public void handleEvent(ActionEvent actionEvent) {
        try {
            int id = Integer.parseInt(view.field_id.getText());
            String nume = view.field_nume.getText();

            int grupa = -1;
            try {
                grupa = Integer.parseInt(view.field_grupa.getText());
            } catch (NumberFormatException e) {
                setWrongInput(view.field_grupa, "Grupa este invalida.");
            }

            String email = view.field_email.getText();
            String profesor = view.field_profesor.getText();

            Student student = new Student(id, nume, grupa, email, profesor);

            service.update(student);

            handleCloseWindow(null);
            GUIUtils.showDialogMessage(Alert.AlertType.CONFIRMATION, "Modificare cu succes", "Studentul a fost modificat cu success!", controllerStudent.view.stack_pane_content);
        } catch (ValidationException e) {
            view.main_pane.setVgap(43d);
            if(e.getMessage().toLowerCase().contains("nume"))
                setWrongInput(view.field_nume, "Numele nu este valid.");
            if(e.getMessage().toLowerCase().contains("email"))
                setWrongInput(view.field_email, "Email-ul nu este valid.");
            if(e.getMessage().toLowerCase().contains("grupa"))
                setWrongInput(view.field_grupa, "Grupa este invalida.");
            if(e.getMessage().toLowerCase().contains("prof"))
                setWrongInput(view.field_profesor, "Profesorul nu este valid.");
        }
    }
}
