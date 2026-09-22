package com.routineexe.view.sessions;

import com.routineexe.database.RoutineExerciseDAO;
import com.routineexe.database.SessionDAO;
import com.routineexe.database.SessionExerciseDAO;
import com.routineexe.model.DayOfWeek;
import com.routineexe.model.Exercise;
import com.routineexe.model.Routine;
import com.routineexe.model.RoutineExercise;
import com.routineexe.model.Session;
import com.routineexe.model.SessionExercise;
import com.routineexe.util.FontAwesomeIcons;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class SessionExerciseAssignmentDialog extends Dialog<Void> {

    private final Session session;
    private final ObservableList<Exercise> exercises;
    private final SessionDAO sessionDAO;
    private final SessionExerciseDAO sessionExerciseDAO;
    private final RoutineExerciseDAO routineExerciseDAO;
    private final Routine routine;
    private final TableView<SessionExercise> exerciseTable = new TableView<>();
    private final ObservableList<SessionExercise> sessionExercises = FXCollections.observableArrayList();
    private final SimpleBooleanProperty readOnly = new SimpleBooleanProperty();

    public SessionExerciseAssignmentDialog(Session session, Routine routine, ObservableList<Exercise> exercises,
            SessionDAO sessionDAO, SessionExerciseDAO sessionExerciseDAO, RoutineExerciseDAO routineExerciseDAO) {
        this.session = session;
        this.routine = routine;
        this.exercises = exercises;
        this.sessionDAO = sessionDAO;
        this.sessionExerciseDAO = sessionExerciseDAO;
        this.routineExerciseDAO = routineExerciseDAO;
        readOnly.set(!session.getDate().equals(LocalDate.now()));

        initStyle(StageStyle.UNDECORATED);
        setTitle("Session: " + routine.getName() + " - " + session.getDate().format(java.time.format.DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy")));
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
        closeButton.setText("Close");

        loadSessionExercises();
        updateSaveButton();
    }

    private HBox createCustomHeader() {
        Label titleLabel = new Label("Session: " + routine.getName());
        titleLabel.getStyleClass().add("dialog-title");

        Label dateLabel = new Label(session.getDate().format(java.time.format.DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy")));
        dateLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #808090;");

        VBox titleBox = new VBox(2, titleLabel, dateLabel);

        HBox header = new HBox(titleBox);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setSpacing(12);
        header.setPadding(new Insets(16, 20, 16, 20));
        header.setStyle("-fx-background-color: #161628; -fx-border-color: transparent transparent #2a2a4a transparent; -fx-background-radius: 0; -fx-border-radius: 0;");
        HBox.setHgrow(titleBox, Priority.ALWAYS);

        return header;
    }

    private VBox createBody() {
        VBox body = new VBox(16);
        body.setPadding(new Insets(24));
        body.setStyle("-fx-background-color: #1a1a2e;");

        Label tableTitle = new Label("Today's Exercises");
        tableTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: 600; -fx-text-fill: #e8e8f0;");

        setupExerciseTable();

        HBox buttonBar = new HBox(12);
        buttonBar.setAlignment(Pos.CENTER_RIGHT);
        buttonBar.setPadding(new Insets(16, 0, 0, 0));

        saveBtn = new Button("Save Session");
        saveBtn.getStyleClass().addAll("btn", "btn-primary");
        saveBtn.setOnAction(e -> {
            try {
                sessionDAO.updateDayDone(session.getId(), true);
            } catch (Exception ex) {
                // Handle error
            }
            closeButton.fire();
        });

        Button closeBtn = new Button("Close");
        closeBtn.getStyleClass().addAll("btn", "btn-secondary");
        closeBtn.setOnAction(e -> closeButton.fire());

        buttonBar.getChildren().addAll(saveBtn, closeBtn);

        body.getChildren().addAll(tableTitle, exerciseTable, buttonBar);
        VBox.setVgrow(exerciseTable, Priority.ALWAYS);

        this.closeButton = (Button) getDialogPane().lookupButton(ButtonType.CLOSE);
        closeButton.setText("");
        closeButton.setVisible(false);
        closeButton.setManaged(false);

        return body;
    }

    private Button closeButton;
    private Button saveBtn;

    private void setupExerciseTable() {
        exerciseTable.setItems(sessionExercises);
        exerciseTable.getStyleClass().add("user-table");
        exerciseTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        exerciseTable.setEditable(true);

        TableColumn<SessionExercise, String> exerciseCol = new TableColumn<>("Exercise");
        exerciseCol.setCellValueFactory(cellData -> {
            SessionExercise se = cellData.getValue();
            Exercise exercise = findExercise(se.getExerciseId());
            String name = exercise != null ? exercise.getName() : "-";
            return new SimpleStringProperty(name);
        });
        exerciseCol.setPrefWidth(200);

        TableColumn<SessionExercise, String> planCol = new TableColumn<>("Plan (sets x reps/time)");
        planCol.setCellValueFactory(cellData -> {
            SessionExercise se = cellData.getValue();
            RoutineExercise re = findRoutineExercise(se.getExerciseId());
            if (re == null) return new SimpleStringProperty("-");

            Exercise exercise = findExercise(se.getExerciseId());
            if (exercise != null && exercise.isTimeBased()) {
                return new SimpleStringProperty(re.getSets() + " x " + re.getReps() + "s");
            } else {
                return new SimpleStringProperty(re.getSets() + " x " + re.getReps());
            }
        });
        planCol.setPrefWidth(160);

        TableColumn<SessionExercise, Integer> setsCol = new TableColumn<>("Sets");
        setsCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getSets()));
        setsCol.setPrefWidth(80);
        setsCol.setCellFactory(col -> new TableCell<>() {
            private final TextField textField = new TextField();
            {
                textField.setStyle("-fx-background-color: #0f0f1a; -fx-border-color: #3a3a5a; -fx-border-radius: 4; -fx-background-radius: 4; -fx-text-fill: #e8e8f0; -fx-font-size: 13px; -fx-padding: 4 8; -fx-pref-width: 60px;");
                textField.setOnAction(e -> {
                    String text = textField.getText().trim();
                    if (!text.isEmpty()) {
                        try {
                            int value = Integer.parseInt(text);
                            SessionExercise se = getTableView().getItems().get(getIndex());
                            if (se != null && !se.getDone() && !readOnly.get()) {
                                se.setSets(value);
                                saveSingleExercise(se);
                            }
                        } catch (NumberFormatException ex) {
                            // ignore
                        }
                    }
                });
                textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
                    if (!newVal) {
                        try {
                            String text = textField.getText().trim();
                            if (!text.isEmpty()) {
                                int value = Integer.parseInt(text);
                                SessionExercise se = getTableView().getItems().get(getIndex());
                                if (se != null && !se.getDone() && !readOnly.get()) {
                                    se.setSets(value);
                                    saveSingleExercise(se);
                                }
                            }
                        } catch (NumberFormatException ex) {
                            // ignore
                        }
                    }
                });
            }
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    textField.setText(item != null ? item.toString() : "");
                    SessionExercise se = getTableView().getItems().get(getIndex());
                    textField.disableProperty().bind(Bindings.or(se.doneProperty(), readOnly));
                    setGraphic(textField);
                }
            }
        });

        TableColumn<SessionExercise, Integer> repsTimeCol = new TableColumn<>("Reps / Time (s)");
        repsTimeCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getReps()));
        repsTimeCol.setPrefWidth(120);
        repsTimeCol.setCellFactory(col -> new TableCell<>() {
            private final TextField textField = new TextField();
            {
                textField.setStyle("-fx-background-color: #0f0f1a; -fx-border-color: #3a3a5a; -fx-border-radius: 4; -fx-background-radius: 4; -fx-text-fill: #e8e8f0; -fx-font-size: 13px; -fx-padding: 4 8; -fx-pref-width: 80px;");
                textField.setOnAction(e -> {
                    String text = textField.getText().trim();
                    if (!text.isEmpty()) {
                        try {
                            int value = Integer.parseInt(text);
                            SessionExercise se = getTableView().getItems().get(getIndex());
                            if (se != null && !se.getDone() && !readOnly.get()) {
                                se.setReps(value);
                                saveSingleExercise(se);
                            }
                        } catch (NumberFormatException ex) {
                            // ignore
                        }
                    }
                });
                textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
                    if (!newVal) {
                        try {
                            String text = textField.getText().trim();
                            if (!text.isEmpty()) {
                                int value = Integer.parseInt(text);
                                SessionExercise se = getTableView().getItems().get(getIndex());
                                if (se != null && !se.getDone() && !readOnly.get()) {
                                    se.setReps(value);
                                    saveSingleExercise(se);
                                }
                            }
                        } catch (NumberFormatException ex) {
                            // ignore
                        }
                    }
                });
            }
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    SessionExercise se = getTableView().getItems().get(getIndex());
                    Exercise exercise = findExercise(se.getExerciseId());
                    String suffix = (exercise != null && exercise.isTimeBased()) ? "s" : "";
                    textField.setText(item != null ? item + suffix : "");
                    textField.disableProperty().bind(Bindings.or(se.doneProperty(), readOnly));
                    setGraphic(textField);
                }
            }
        });

        TableColumn<SessionExercise, Double> weightCol = new TableColumn<>("Weight (kg)");
        weightCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getWeight()));
        weightCol.setPrefWidth(100);
        weightCol.setCellFactory(col -> new TableCell<>() {
            private final TextField textField = new TextField();
            {
                textField.setStyle("-fx-background-color: #0f0f1a; -fx-border-color: #3a3a5a; -fx-border-radius: 4; -fx-background-radius: 4; -fx-text-fill: #e8e8f0; -fx-font-size: 13px; -fx-padding: 4 8; -fx-pref-width: 80px;");
                textField.setOnAction(e -> {
                    SessionExercise se = getTableView().getItems().get(getIndex());
                    if (se != null && !se.getDone() && !readOnly.get()) {
                        String text = textField.getText().trim();
                        try {
                            if (text.isEmpty()) {
                                se.setWeight(null);
                            } else {
                                se.setWeight(Double.parseDouble(text));
                            }
                            saveSingleExercise(se);
                        } catch (NumberFormatException ex) {
                            // ignore
                        }
                    }
                });
                textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
                    if (!newVal) {
                        SessionExercise se = getTableView().getItems().get(getIndex());
                        if (se != null && !se.getDone() && !readOnly.get()) {
                            String text = textField.getText().trim();
                            try {
                                if (text.isEmpty()) {
                                    se.setWeight(null);
                                } else {
                                    se.setWeight(Double.parseDouble(text));
                                }
                                saveSingleExercise(se);
                            } catch (NumberFormatException ex) {
                                // ignore
                            }
                        }
                    }
                });
            }
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    SessionExercise se = getTableView().getItems().get(getIndex());
                    Exercise ex = findExercise(se.getExerciseId());
                    if (ex != null && ex.isTimeBased()) {
                        setGraphic(null);
                    } else {
                        textField.setText(item != null ? item.toString() : "");
                        textField.disableProperty().bind(Bindings.or(se.doneProperty(), readOnly));
                        setGraphic(textField);
                    }
                }
            }
        });

        TableColumn<SessionExercise, Boolean> doneCol = new TableColumn<>("Done");
        doneCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDone()));
        doneCol.setPrefWidth(80);
        doneCol.setCellFactory(col -> new TableCell<>() {
            private final CheckBox checkBox = new CheckBox();
            {
                checkBox.setStyle("-fx-text-fill: #c0c0d0;");
                checkBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
                    SessionExercise se = getTableView().getItems().get(getIndex());
                    if (se != null) {
                        if (readOnly.get() || !newVal) {
                            javafx.application.Platform.runLater(() -> checkBox.setSelected(se.getDone()));
                            return;
                        }
                        se.setDone(true);
                        try {
                            sessionExerciseDAO.update(se);
                            checkIfSessionComplete();
                            updateSaveButton();
                        } catch (Exception ex) {
                            javafx.application.Platform.runLater(() -> {
                                se.setDone(false);
                                checkBox.setSelected(false);
                            });
                        }
                    }
                });
            }
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    checkBox.setSelected(item);
                    checkBox.setDisable(readOnly.get());
                    setGraphic(checkBox);
                    setAlignment(Pos.CENTER);
                }
            }
        });

        exerciseTable.getColumns().addAll(exerciseCol, planCol, setsCol, repsTimeCol, weightCol, doneCol);
        exerciseTable.setPlaceholder(new Label("No exercises for today"));
    }

    private void saveSingleExercise(SessionExercise se) {
        try {
            sessionExerciseDAO.update(se);
        } catch (Exception ex) {
            // Handle error
        }
    }

    private void saveAllExercises() {
        for (SessionExercise se : sessionExercises) {
            try {
                sessionExerciseDAO.update(se);
            } catch (Exception ex) {
                // Handle error
            }
        }
    }

    private void checkIfSessionComplete() {
        updateSaveButton();
    }

    private void updateSaveButton() {
        if (saveBtn != null) {
            boolean allDone = sessionExercises.stream().allMatch(SessionExercise::getDone);
            saveBtn.setDisable(readOnly.get() || !allDone || (session.getSessionDayDone() != null && session.getSessionDayDone()));
        }
    }

    private void loadSessionExercises() {
        try {
            int dayOfWeekIndex = session.getDayOfWeekIndex();
            DayOfWeek dayOfWeek = DayOfWeek.fromIndex(dayOfWeekIndex);

            List<RoutineExercise> routineExercises = routineExerciseDAO.fetchByRoutine(routine.getId());
            List<RoutineExercise> todaysRoutineExercises = routineExercises.stream()
                .filter(re -> re.getDayOfWeek() != null && re.getDayOfWeek().equals(dayOfWeek))
                .collect(Collectors.toList());

            List<SessionExercise> existingSessionExercises = sessionExerciseDAO.fetchBySession(session.getId());

            if (existingSessionExercises.isEmpty()) {
                for (RoutineExercise re : todaysRoutineExercises) {
                    SessionExercise se = new SessionExercise();
                    se.setSessionId(session.getId());
                    se.setExerciseId(re.getExercise().getId());
                    se.setSets(null);
                    se.setReps(null);
                    se.setWeight(null);
                    se.setDone(false);
                    long id = sessionExerciseDAO.insert(se);
                    se.setId(id);
                    sessionExercises.add(se);
                }
            } else {
                sessionExercises.setAll(existingSessionExercises);
            }

        } catch (Exception ex) {
            // Handle error
        }
    }

    private Exercise findExercise(long exerciseId) {
        return exercises.stream()
            .filter(e -> e.getId().equals(exerciseId))
            .findFirst()
            .orElse(null);
    }

    private RoutineExercise findRoutineExercise(long exerciseId) {
        try {
            List<RoutineExercise> routineExercises = routineExerciseDAO.fetchByRoutine(routine.getId());
            int dayOfWeekIndex = session.getDayOfWeekIndex();
            DayOfWeek dayOfWeek = DayOfWeek.fromIndex(dayOfWeekIndex);
            return routineExercises.stream()
                .filter(re -> re.getExercise() != null && re.getExercise().getId().equals(exerciseId)
                    && re.getDayOfWeek() != null && re.getDayOfWeek().equals(dayOfWeek))
                .findFirst()
                .orElse(null);
        } catch (Exception ex) {
            return null;
        }
    }
}
