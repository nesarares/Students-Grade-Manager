package UI.GUI.StudentMainWindow;

import Domain.Student;
import Repository.RepositoryException;
import Service.StudentService;
import Utils.GUIUtils;
import Validator.ValidationException;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

public class StudentAddController extends AbstractStudentEventController {

    public StudentAddController(StudentService service, StudentController controller) {
        super(service, controller);
    }

    @Override
    public void handleEvent (ActionEvent actionEvent) {
        try {
            removeWrongInput(view.field_id);
            removeWrongInput(view.field_nume);
            removeWrongInput(view.field_grupa);
            removeWrongInput(view.field_email);
            removeWrongInput(view.field_profesor);

            int id = -1;
            try {
                id = Integer.parseInt(view.field_id.getText());
            } catch (NumberFormatException e) {
                setWrongInput(view.field_id, "Numarul matricol nu este valid.");
            }

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

            service.add(student);

            handleCloseWindow(null);
            GUIUtils.showDialogMessage(Alert.AlertType.CONFIRMATION, "Adaugare cu succes", "Studentul a fost adaugat cu success!", controllerStudent.view.stack_pane_content);
        } catch (ValidationException e) {
            view.main_pane.setVgap(43d);
            if(e.getMessage().toLowerCase().startsWith("id"))
                setWrongInput(view.field_id, "Numarul matricol nu este valid.");
            if(e.getMessage().toLowerCase().contains("nume"))
                setWrongInput(view.field_nume, "Numele nu este valid.");
            if(e.getMessage().toLowerCase().contains("email"))
                setWrongInput(view.field_email, "Email-ul nu este valid.");
            if(e.getMessage().toLowerCase().contains("grupa"))
                setWrongInput(view.field_grupa, "Grupa este invalida.");
            if(e.getMessage().toLowerCase().contains("prof"))
                setWrongInput(view.field_profesor, "Profesorul nu este valid.");
        } catch (RepositoryException e) {
            GUIUtils.showDialogMessage(Alert.AlertType.ERROR, "Adaugare esuata", "Mai exista un student cu acelasi numar matricol.", view.stack_pane_event);
        }
    }
}
