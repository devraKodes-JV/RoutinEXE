package com.routineexe.view.sessions;

import com.routineexe.model.Session;
import com.routineexe.model.Routine;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SessionFormDialog extends Dialog<Session> {

    private final ComboBox<Routine> routineCombo = new ComboBox<>();
    private final DatePicker datePicker = new DatePicker();
    private final ButtonType createBtn = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
    private final ButtonType updateBtn = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
    private Session editingSession = null;

    public SessionFormDialog(List<Routine> routines) {
        this(null, routines);
    }

    public SessionFormDialog(Session sessionToEdit, List<Routine> routines) {
        this.editingSession = sessionToEdit;
        initStyle(StageStyle.UNDECORATED);
        setTitle(sessionToEdit == null ? "Create Session" : "Edit Session");
        setResizable(true);
        getDialogPane().getStyleClass().add("dialog-pane");
        getDialogPane().getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        boolean isEdit = sessionToEdit != null;
        ButtonType submitBtn = isEdit ? updateBtn : createBtn;
        getDialogPane().getButtonTypes().addAll(submitBtn, ButtonType.CANCEL);

        VBox content = new VBox();
        content.setSpacing(0);
        content.setPadding(new Insets(0));

        HBox customHeader = createCustomHeader(isEdit);
        GridPane grid = createFormGrid(routines);

        content.getChildren().addAll(customHeader, grid);
        getDialogPane().setContent(content);

        getDialogPane().sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.setFill(null);
            }
        });

        Button submitButton = (Button) getDialogPane().lookupButton(submitBtn);
        submitButton.getStyleClass().addAll("btn", "btn-primary");
        submitButton.setDisable(true);

        datePicker.valueProperty().addListener(e -> {
            submitButton.setDisable(datePicker.getValue() == null || routineCombo.getValue() == null);
        });

        routineCombo.valueProperty().addListener(e -> {
            submitButton.setDisable(datePicker.getValue() == null || routineCombo.getValue() == null);
        });

        Button cancelButton = (Button) getDialogPane().lookupButton(ButtonType.CANCEL);
        cancelButton.getStyleClass().addAll("btn", "btn-secondary");

        if (isEdit) {
            loadSessionData(sessionToEdit);
        }

        // Initial validation
        submitButton.setDisable(datePicker.getValue() == null || routineCombo.getValue() == null);

        setResultConverter(buttonType -> {
            if (buttonType == submitBtn && isValid()) {
                return getSessionData();
            }
            return null;
        });
    }

    private HBox createCustomHeader(boolean isEdit) {
        Label titleLabel = new Label(isEdit ? "Edit Session" : "Create Session");
        titleLabel.getStyleClass().add("dialog-title");

        HBox header = new HBox(titleLabel);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setSpacing(12);
        header.setPadding(new Insets(16, 20, 16, 20));
        header.setStyle("-fx-background-color: #161628; -fx-border-color: transparent transparent #2a2a4a transparent; -fx-background-radius: 0; -fx-border-radius: 0;");
        HBox.setHgrow(titleLabel, Priority.ALWAYS);

        return header;
    }

    private GridPane createFormGrid(List<Routine> routines) {
        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(12);
        grid.setPadding(new Insets(20));

        routineCombo.setItems(javafx.collections.FXCollections.observableArrayList(routines));
        routineCombo.setPromptText("Select routine");
        routineCombo.getStyleClass().add("combo-box");
        routineCombo.setCellFactory(lv -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(Routine item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });
        routineCombo.setButtonCell(new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(Routine item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });

        // Set minimum date to today
        datePicker.setDayCellFactory(picker -> new javafx.scene.control.DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
            }
        });
        datePicker.setPromptText("Date");
        datePicker.getStyleClass().add("date-picker");

        // Row 0: Routine
        Label routineLabel = new Label("Routine *");
        routineLabel.getStyleClass().add("form-label");
        grid.add(routineLabel, 0, 0);
        grid.add(routineCombo, 1, 0);
        GridPane.setHgrow(routineCombo, Priority.ALWAYS);
        GridPane.setColumnSpan(routineCombo, 2);

        // Row 1: Date
        Label dateLabel = new Label("Date *");
        dateLabel.getStyleClass().add("form-label");
        grid.add(dateLabel, 0, 1);
        grid.add(datePicker, 1, 1);
        GridPane.setHgrow(datePicker, Priority.ALWAYS);
        GridPane.setColumnSpan(datePicker, 2);

        return grid;
    }

    private void loadSessionData(Session session) {
        routineCombo.getItems().stream()
            .filter(r -> r.getId().equals(session.getRoutineId()))
            .findFirst()
            .ifPresent(routineCombo.getSelectionModel()::select);
        if (session.getDate() != null) {
            datePicker.setValue(session.getDate());
        }
    }

    public Session getSessionData() {
        Session s = new Session();
        s.setRoutineId(routineCombo.getValue() != null ? routineCombo.getValue().getId() : null);
        s.setDate(datePicker.getValue());
        if (s.getDate() != null) {
            s.setDayOfWeekIndex(s.getDate().getDayOfWeek().getValue() - 1);
        }
        return s;
    }

    public boolean isValid() {
        return datePicker.getValue() != null && routineCombo.getValue() != null;
    }
}