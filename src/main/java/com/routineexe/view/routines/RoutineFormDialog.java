package com.routineexe.view.routines;

import com.routineexe.model.DayOfWeek;
import com.routineexe.model.Routine;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;

import java.time.LocalDate;
import java.util.EnumSet;
import java.util.Set;

public class RoutineFormDialog extends Dialog<Routine> {

    private final TextField nameField = new TextField();
    private final DatePicker sincePicker = new DatePicker();
    private final DatePicker untilPicker = new DatePicker();
    private final CheckBox[] dayCheckboxes = new CheckBox[7];
    private final ButtonType createBtn = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
    private final ButtonType updateBtn = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
    private Routine editingRoutine = null;

    public RoutineFormDialog() {
        this(null);
    }

    public RoutineFormDialog(Routine routineToEdit) {
        this.editingRoutine = routineToEdit;
        initStyle(StageStyle.UNDECORATED);
        setTitle(routineToEdit == null ? "Create Routine" : "Edit Routine");
        setResizable(true);
        getDialogPane().getStyleClass().add("dialog-pane");
        getDialogPane().getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        boolean isEdit = routineToEdit != null;
        ButtonType submitBtn = isEdit ? updateBtn : createBtn;
        getDialogPane().getButtonTypes().addAll(submitBtn, ButtonType.CANCEL);

        VBox content = new VBox();
        content.setSpacing(0);
        content.setPadding(new Insets(0));

        HBox customHeader = createCustomHeader(isEdit);
        GridPane grid = createFormGrid();

        content.getChildren().addAll(customHeader, grid);
        getDialogPane().setContent(content);

        getDialogPane().sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.setFill(null);
            }
        });

        Button cancelButton = (Button) getDialogPane().lookupButton(ButtonType.CANCEL);
        cancelButton.getStyleClass().addAll("btn", "btn-secondary");

        if (isEdit) {
            loadRoutineData(routineToEdit);
        }

        // Initial validation
        updateSubmitButton();

        setResultConverter(buttonType -> {
            if (buttonType == submitBtn && isValid()) {
                return getRoutineData();
            }
            return null;
        });
    }

    private HBox createCustomHeader(boolean isEdit) {
        Label titleLabel = new Label(isEdit ? "Edit Routine" : "Create Routine");
        titleLabel.getStyleClass().add("dialog-title");

        HBox header = new HBox(titleLabel);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setSpacing(12);
        header.setPadding(new Insets(16, 20, 16, 20));
        header.setStyle("-fx-background-color: #161628; -fx-border-color: transparent transparent #2a2a4a transparent; -fx-background-radius: 0; -fx-border-radius: 0;");
        HBox.setHgrow(titleLabel, Priority.ALWAYS);

        return header;
    }

    private GridPane createFormGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(12);
        grid.setPadding(new Insets(20));

        nameField.setPromptText("Routine name");
        nameField.getStyleClass().add("text-field");

        // Set minimum date for sincePicker to today
        sincePicker.setDayCellFactory(picker -> new javafx.scene.control.DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
            }
        });
        sincePicker.setPromptText("Since");
        sincePicker.getStyleClass().add("date-picker");

        untilPicker.setPromptText("Until (optional)");
        untilPicker.getStyleClass().add("date-picker");

        // Day checkboxes
        DayOfWeek[] days = DayOfWeek.values();
        String[] dayLabels = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        HBox daysBox = new HBox(8);
        daysBox.setAlignment(Pos.CENTER_LEFT);
        
        for (int i = 0; i < days.length; i++) {
            CheckBox cb = new CheckBox(dayLabels[i]);
            cb.getStyleClass().add("day-checkbox");
            cb.setTextFill(javafx.scene.paint.Color.WHITE);
            dayCheckboxes[i] = cb;
            daysBox.getChildren().add(cb);
        }

        // Row 0: Name (full width)
        Label nameLabel = new Label("Name *");
        nameLabel.getStyleClass().add("form-label");
        grid.add(nameLabel, 0, 0);
        grid.add(nameField, 1, 0);
        GridPane.setHgrow(nameField, Priority.ALWAYS);
        GridPane.setColumnSpan(nameField, 2);

        // Row 1: Since and Until
        Label sinceLabel = new Label("Since *");
        sinceLabel.getStyleClass().add("form-label");
        Label untilLabel = new Label("Until");
        untilLabel.getStyleClass().add("form-label");

        grid.add(sinceLabel, 0, 1);
        grid.add(sincePicker, 1, 1);
        grid.add(untilLabel, 2, 1);
        grid.add(untilPicker, 3, 1);
        GridPane.setHgrow(sincePicker, Priority.ALWAYS);
        GridPane.setHgrow(untilPicker, Priority.ALWAYS);

        // Row 2: Days
        Label daysLabel = new Label("Days");
        daysLabel.getStyleClass().add("form-label");
        grid.add(daysLabel, 0, 2);
        grid.add(daysBox, 1, 2, 3, 1);

        // Add date validation listeners
        sincePicker.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                // Update untilPicker minimum date to be after since
                untilPicker.setDayCellFactory(picker -> new javafx.scene.control.DateCell() {
                    @Override
                    public void updateItem(LocalDate date, boolean empty) {
                        super.updateItem(date, empty);
                        setDisable(empty || date.isBefore(newVal.plusDays(1)));
                    }
                });
                // Clear until if it's now invalid
                if (untilPicker.getValue() != null && !untilPicker.getValue().isAfter(newVal)) {
                    untilPicker.setValue(null);
                }
            }
            updateSubmitButton();
        });

        untilPicker.valueProperty().addListener((obs, oldVal, newVal) -> {
            updateSubmitButton();
        });

        nameField.textProperty().addListener(e -> updateSubmitButton());

        return grid;
    }

    private void updateSubmitButton() {
        Button submitButton = (Button) getDialogPane().lookupButton(editingRoutine != null ? updateBtn : createBtn);
        submitButton.getStyleClass().addAll("btn", "btn-primary");
        boolean valid = !nameField.getText().trim().isEmpty() 
            && sincePicker.getValue() != null 
            && (untilPicker.getValue() == null || untilPicker.getValue().isAfter(sincePicker.getValue()));
        submitButton.setDisable(!valid);
    }

    private void loadRoutineData(Routine routine) {
        nameField.setText(routine.getName());
        routine.getSinceOptional().ifPresent(sincePicker::setValue);
        routine.getUntilOptional().ifPresent(untilPicker::setValue);
        
        Set<DayOfWeek> days = routine.getDays();
        DayOfWeek[] allDays = DayOfWeek.values();
        for (int i = 0; i < allDays.length; i++) {
            dayCheckboxes[i].setSelected(days.contains(allDays[i]));
        }
    }

    public Routine getRoutineData() {
        Routine r = new Routine();
        r.setName(nameField.getText().trim());
        r.setSince(sincePicker.getValue());
        r.setUntil(untilPicker.getValue());
        
        Set<DayOfWeek> selectedDays = EnumSet.noneOf(DayOfWeek.class);
        DayOfWeek[] allDays = DayOfWeek.values();
        for (int i = 0; i < allDays.length; i++) {
            if (dayCheckboxes[i].isSelected()) {
                selectedDays.add(allDays[i]);
            }
        }
        r.setDays(selectedDays);
        return r;
    }

    public boolean isValid() {
        LocalDate since = sincePicker.getValue();
        LocalDate until = untilPicker.getValue();
        return !nameField.getText().trim().isEmpty() 
            && since != null 
            && (until == null || until.isAfter(since));
    }
}