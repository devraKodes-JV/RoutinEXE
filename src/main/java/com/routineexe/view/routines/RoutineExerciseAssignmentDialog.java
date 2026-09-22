package com.routineexe.view.routines;

import com.routineexe.database.RoutineExerciseDAO;
import com.routineexe.model.DayOfWeek;
import com.routineexe.model.Exercise;
import com.routineexe.model.Routine;
import com.routineexe.model.RoutineExercise;
import com.routineexe.util.FontAwesomeIcons;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;
import javafx.stage.StageStyle;

import java.util.List;

public class RoutineExerciseAssignmentDialog extends Dialog<Void> {

    private final Routine routine;
    private final ObservableList<Exercise> exercises;
    private final RoutineExerciseDAO routineExerciseDAO;
    private final TableView<RoutineExercise> exerciseTable = new TableView<>();
    private final ObservableList<RoutineExercise> routineExercises = FXCollections.observableArrayList();
    private final ObservableList<RoutineExercise> filteredExercises = FXCollections.observableArrayList();
    private DayOfWeek currentFilterDay = null;

    public RoutineExerciseAssignmentDialog(Routine routine, ObservableList<Exercise> exercises, RoutineExerciseDAO routineExerciseDAO) {
        this.routine = routine;
        this.exercises = exercises;
        this.routineExerciseDAO = routineExerciseDAO;
        
        initStyle(StageStyle.UNDECORATED);
        setTitle("Assign Exercises to " + routine.getName());
        setResizable(true);
        getDialogPane().getStyleClass().add("dialog-pane");
        getDialogPane().getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);

        VBox content = new VBox();
        content.setSpacing(0);
        content.setPadding(new Insets(0));

        HBox customHeader = createCustomHeader();
        VBox body = createBody();

        content.getChildren().addAll(customHeader, body);
        getDialogPane().setContent(content);

        getDialogPane().sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.setFill(null);
            }
        });

        Button closeButton = (Button) getDialogPane().lookupButton(ButtonType.CLOSE);
        closeButton.getStyleClass().addAll("btn", "btn-secondary");
        closeButton.setText("Done");

        loadRoutineExercises();
    }

    private HBox createCustomHeader() {
        Label titleLabel = new Label("Assign Exercises: " + routine.getName());
        titleLabel.getStyleClass().add("dialog-title");

        HBox header = new HBox(titleLabel);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setSpacing(12);
        header.setPadding(new Insets(16, 20, 16, 20));
        header.setStyle("-fx-background-color: #161628; -fx-border-color: transparent transparent #2a2a4a transparent; -fx-background-radius: 0; -fx-border-radius: 0;");
        HBox.setHgrow(titleLabel, Priority.ALWAYS);

        return header;
    }

    private VBox createBody() {
        VBox body = new VBox(16);
        body.setPadding(new Insets(24));
        body.setStyle("-fx-background-color: #1a1a2e;");

        // Assignment form
        GridPane formGrid = new GridPane();
        formGrid.setHgap(12);
        formGrid.setVgap(12);
        formGrid.setPadding(new Insets(0, 0, 16, 0));

        ComboBox<Exercise> exerciseCombo = new ComboBox<>(exercises);
        exerciseCombo.setPromptText("Select exercise");
        exerciseCombo.getStyleClass().add("combo-box");
        exerciseCombo.setCellFactory(lv -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(Exercise item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });
        exerciseCombo.setButtonCell(new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(Exercise item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });

        ComboBox<DayOfWeek> dayCombo = new ComboBox<>();
        dayCombo.setItems(FXCollections.observableArrayList(routine.getDays()));
        dayCombo.setPromptText("Select day");
        dayCombo.getStyleClass().add("combo-box");
        dayCombo.setCellFactory(lv -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(DayOfWeek item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getFullName());
            }
        });
        dayCombo.setButtonCell(new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(DayOfWeek item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getFullName());
            }
        });

        // Filter table when day changes
        dayCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            currentFilterDay = newVal;
            filterTableByDay();
        });

        TextField setsField = new TextField();
        setsField.setPromptText("Sets");
        setsField.getStyleClass().add("text-field");
        setsField.setPrefWidth(80);

        TextField repsField = new TextField();
        repsField.setPromptText("Reps");
        repsField.getStyleClass().add("text-field");
        repsField.setPrefWidth(80);

        Button addBtn = new Button("Add");
        addBtn.getStyleClass().addAll("btn", "btn-primary");
        addBtn.setOnAction(e -> {
            Exercise exercise = exerciseCombo.getValue();
            DayOfWeek day = dayCombo.getValue();
            String setsText = setsField.getText().trim();
            String repsText = repsField.getText().trim();

            if (exercise == null || day == null || setsText.isEmpty() || repsText.isEmpty()) {
                return;
            }

            try {
                int sets = Integer.parseInt(setsText);
                int reps = Integer.parseInt(repsText);

                RoutineExercise re = new RoutineExercise();
                re.setRoutine(routine);
                re.setExercise(exercise);
                re.setDayOfWeek(day);
                re.setSets(sets);
                re.setReps(reps);

                long id = routineExerciseDAO.insert(re);
                re.setId(id);
                routineExercises.add(re);
                filterTableByDay(); // Refresh filtered list
                
                exerciseCombo.getSelectionModel().clearSelection();
                dayCombo.getSelectionModel().clearSelection();
                setsField.clear();
                repsField.clear();
            } catch (NumberFormatException ex) {
                // Invalid number
            } catch (Exception ex) {
                // Handle error
            }
        });

        // Row 0: Exercise, Day
        Label exerciseLabel = new Label("Exercise *");
        exerciseLabel.getStyleClass().add("form-label");
        Label dayLabel = new Label("Day *");
        dayLabel.getStyleClass().add("form-label");

        formGrid.add(exerciseLabel, 0, 0);
        formGrid.add(exerciseCombo, 1, 0);
        formGrid.add(dayLabel, 2, 0);
        formGrid.add(dayCombo, 3, 0);
        GridPane.setHgrow(exerciseCombo, Priority.ALWAYS);
        GridPane.setHgrow(dayCombo, Priority.ALWAYS);

        // Row 1: Sets, Reps, Add button
        Label setsLabel = new Label("Sets *");
        setsLabel.getStyleClass().add("form-label");
        Label repsLabel = new Label("Reps *");
        repsLabel.getStyleClass().add("form-label");

        formGrid.add(setsLabel, 0, 1);
        formGrid.add(setsField, 1, 1);
        formGrid.add(repsLabel, 2, 1);
        formGrid.add(repsField, 3, 1);
        formGrid.add(addBtn, 4, 1);

        // Table of assigned exercises
        Label tableTitle = new Label("Assigned Exercises");
        tableTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: 600; -fx-text-fill: #e8e8f0;");

        setupExerciseTable();

        body.getChildren().addAll(formGrid, tableTitle, exerciseTable);
        VBox.setVgrow(exerciseTable, Priority.ALWAYS);

        return body;
    }

private void setupExerciseTable() {
        // Use filtered list for display
        exerciseTable.setItems(filteredExercises);
        exerciseTable.getStyleClass().add("user-table");
        exerciseTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        TableColumn<RoutineExercise, String> exerciseCol = new TableColumn<>("Exercise");
        exerciseCol.setCellValueFactory(cellData -> {
            String name = cellData.getValue().getExercise() != null
                ? cellData.getValue().getExercise().getName() : "-";
            return new SimpleStringProperty(name);
        });
        exerciseCol.setPrefWidth(200);

        TableColumn<RoutineExercise, String> dayCol = new TableColumn<>("Day");
        dayCol.setCellValueFactory(cellData -> {
            DayOfWeek day = cellData.getValue().getDayOfWeek();
            return new SimpleStringProperty(day != null ? day.getFullName() : "-");
        });
        dayCol.setPrefWidth(120);

        TableColumn<RoutineExercise, String> setsCol = new TableColumn<>("Sets");
        setsCol.setCellValueFactory(cellData -> {
            Integer sets = cellData.getValue().getSets();
            return new SimpleStringProperty(sets != null ? sets.toString() : "-");
        });
        setsCol.setPrefWidth(80);

        // Reps/Time column - shows "X reps" or "Ys" for time-based
        TableColumn<RoutineExercise, String> repsTimeCol = new TableColumn<>("Reps / Time (seconds)");
        repsTimeCol.setCellValueFactory(cellData -> {
            RoutineExercise re = cellData.getValue();
            Exercise exercise = re.getExercise();
            if (exercise != null && exercise.isTimeBased()) {
                Integer reps = re.getReps();
                return new SimpleStringProperty(reps != null ? reps + "s" : "-");
            } else {
                Integer reps = re.getReps();
                return new SimpleStringProperty(reps != null ? reps.toString() : "-");
            }
        });
        repsTimeCol.setPrefWidth(120);

        TableColumn<RoutineExercise, RoutineExercise> actionsCol = new TableColumn<>("Actions");
        actionsCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue()));
        actionsCol.setPrefWidth(100);
        actionsCol.setCellFactory(col -> new TableCell<>() {
            private final javafx.scene.control.Button deleteBtn = createIconButton(com.routineexe.util.FontAwesomeIcons.TRASH, "btn-icon btn-delete");

            {
                deleteBtn.setOnAction(e -> {
                    RoutineExercise re = getTableView().getItems().get(getIndex());
                    try {
                        if (routineExerciseDAO.delete(re.getId())) {
                            routineExercises.remove(re);
                            filterTableByDay(); // Refresh filtered list
                        }
                    } catch (Exception ex) {
                        // Handle error
                    }
                    e.consume();
                });
            }

            @Override
            protected void updateItem(RoutineExercise item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteBtn);
                }
            }
        });

        exerciseTable.getColumns().addAll(exerciseCol, dayCol, setsCol, repsTimeCol, actionsCol);
        exerciseTable.setPlaceholder(new Label("No exercises assigned yet"));
    }

    private void filterTableByDay() {
        filteredExercises.clear();
        if (currentFilterDay == null) {
            filteredExercises.addAll(routineExercises);
        } else {
            for (RoutineExercise re : routineExercises) {
                if (currentFilterDay.equals(re.getDayOfWeek())) {
                    filteredExercises.add(re);
                }
            }
        }
    }

    private void loadRoutineExercises() {
        try {
            List<RoutineExercise> list = routineExerciseDAO.fetchByRoutine(routine.getId());
            routineExercises.setAll(list);
            filterTableByDay();
        } catch (Exception ex) {
            // Handle error
        }
    }

    private javafx.scene.control.Button createIconButton(String iconUnicode, String styleClass) {
        Label icon = new Label(iconUnicode);
        icon.setStyle("-fx-font-family: 'Font Awesome 6 Free'; -fx-font-weight: 900; -fx-font-size: 14px; -fx-text-fill: white;");

        javafx.scene.control.Button btn = new javafx.scene.control.Button();
        btn.setGraphic(icon);
        btn.getStyleClass().addAll(styleClass.split("\\s+"));
        btn.setPrefSize(32, 32);
        btn.setPadding(javafx.geometry.Insets.EMPTY);
        return btn;
    }
}