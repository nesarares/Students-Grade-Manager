package UI.GUI.StudentMainWindow;

import Domain.Student;
import Service.StudentService;
import Utils.ConfirmationDialog;
import Utils.GUIUtils;
import Utils.ListEvent;
import Utils.Observer;
import Utils.Transitions.FadeInUpTransition;
import Utils.Transitions.FadeOutDownTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class StudentController implements Observer<Student> {
    private StudentService service;
    private ObservableList<Student> model;
    StudentView view;

    private Pane view_add;

    public StudentController(StudentService service) {
        this.service = service;
        model = FXCollections.observableArrayList(service.filtreazaStudentiKeyword("", service.getAll()));
    }

    public ObservableList<Student> getModel() {
        return model;
    }

    public void setView(StudentView view) {
        this.view = view;
    }

    public Node createPage(int pageIndex) {

        int fromIndex = pageIndex * view.rows_per_page;
        int toIndex = Math.min(fromIndex + view.rows_per_page, model.size());
        view.table.setItems(FXCollections.observableList(model.subList(fromIndex, toIndex)));

        return new BorderPane(view.table);
    }

    public void setPaginationPages() {
        int numOfPages = 1;
        if (model.size() % view.rows_per_page == 0) {
            numOfPages = model.size() / view.rows_per_page;
        } else if (model.size() > view.rows_per_page) {
            numOfPages = model.size() / view.rows_per_page + 1;
        }
        view.pagination.setPageCount(numOfPages);
    }

    @Override
    public void notifyEvent(ListEvent<Student> event) {
        String keyword = view.search.getText();

        model.setAll(service.filtreazaStudentiKeyword(keyword, event.getList()));

        setPaginationPages();
        view.pagination.setPageFactory(this::createPage);

        view.table.refresh();
    }

    public void handleSearchBar() {
        String keyword = view.search.getText();
        model.setAll(service.filtreazaStudentiKeyword(keyword, service.getAll()));

        setPaginationPages();
        view.pagination.setPageFactory(this::createPage);
    }

    public void handleButtonAdd(ActionEvent actionEvent) {
        StudentAddController controller = new StudentAddController(service, this);
        StudentEventStage stage = new StudentEventStage(controller, "Adauga", null);
        controller.setView(stage);

        view_add = stage.getView();

        view.content_pane.effectProperty().setValue(new GaussianBlur(10));
        view.content_pane.setDisable(true);

        AnchorPane.setTopAnchor(view.overlay_pane, 0d);
        AnchorPane.setLeftAnchor(view.overlay_pane, 0d);
        AnchorPane.setBottomAnchor(view.overlay_pane, 0d);
        AnchorPane.setRightAnchor(view.overlay_pane, 0d);

        view.overlay_pane.getChildren().add(view_add);
        new FadeInUpTransition(view_add);
    }

    public void handleButtonEdit(Student student) {
        StudentEditController controller = new StudentEditController(service, this);
        StudentEventStage stage = new StudentEventStage(controller, "Modifica", student);
        controller.setView(stage);

        view_add = stage.getView();

        view.content_pane.effectProperty().setValue(new GaussianBlur(10));
        view.content_pane.setDisable(true);

        AnchorPane.setTopAnchor(view.overlay_pane, 0d);
        AnchorPane.setLeftAnchor(view.overlay_pane, 0d);
        AnchorPane.setBottomAnchor(view.overlay_pane, 0d);
        AnchorPane.setRightAnchor(view.overlay_pane, 0d);

        view.overlay_pane.getChildren().add(view_add);
        new FadeInUpTransition(view_add);
    }

    void handleCloseWindowEvent() {
        new FadeOutDownTransition(view_add).setOnFinished((p) -> {
            view.overlay_pane.getChildren().remove(0);

            view.content_pane.effectProperty().setValue(null);
            view.content_pane.setDisable(false);

            AnchorPane.setBottomAnchor(view.overlay_pane, null);
            AnchorPane.setRightAnchor(view.overlay_pane, null);
        });
    }

    public void handleButtonDelete(Student student) {
        ConfirmationDialog confirmationDialog = new ConfirmationDialog("Stergere student", "Sunteti sigur ca vreti sa stergeti acest student?");
        confirmationDialog.setButtonOkHandler((p) -> {
            Student removed = service.remove(student.getId());
            if (removed == null)
                GUIUtils.showDialogMessage(Alert.AlertType.WARNING, "Atentie", "Nu a fost sters studentul cu acel ID.", view.stack_pane_content);
            else
                GUIUtils.showDialogMessage(Alert.AlertType.INFORMATION, "Stergere cu success", "Studentul a fost sters cu success!", view.stack_pane_content);
        });
        confirmationDialog.show(view.stack_pane_content);
    }

    public void handleButtonDeleteSelected(ActionEvent actionEvent) {
        if (view.table.getSelectionModel().getSelectedItems().size() == 0)
            GUIUtils.showDialogMessage(Alert.AlertType.WARNING, "Atentie", "Nu au fost selectati studenti!", view.stack_pane_content);
        else {
            service.removeMultiple(view.table.getSelectionModel().getSelectedItems());
            handleButtonClear(null);
            GUIUtils.showDialogMessage(Alert.AlertType.INFORMATION, "Stergere cu success", "Studentii selectati au fost stersi cu success!", view.stack_pane_content);
        }
    }

    public void handleButtonClear(ActionEvent actionEvent) {
        view.search.clear();
        view.table.getSelectionModel().clearSelection();
        view.table.refresh();
    }
}
