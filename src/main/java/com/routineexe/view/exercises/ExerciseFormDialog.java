package com.routineexe.view.exercises;

import com.routineexe.model.Category;
import com.routineexe.model.Exercise;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;

public class ExerciseFormDialog extends Dialog<Exercise> {

    private final TextField nameField = new TextField();
    private final ComboBox<Category> categoryCombo = new ComboBox<>();
    private final TextArea descriptionArea = new TextArea();
    private final CheckBox timeBasedCheckBox = new CheckBox("Time Based");
    private final ButtonType createBtn = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
    private final ButtonType updateBtn = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
    private Exercise editingExercise = null;

    public ExerciseFormDialog(ObservableList<Category> categories) {
        this(null, categories);
    }

    public ExerciseFormDialog(Exercise exerciseToEdit, ObservableList<Category> categories) {
        this.editingExercise = exerciseToEdit;
        initStyle(StageStyle.UNDECORATED);
        setTitle(exerciseToEdit == null ? "Create Exercise" : "Edit Exercise");
        setResizable(true);
        getDialogPane().getStyleClass().add("dialog-pane");
        getDialogPane().getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        boolean isEdit = exerciseToEdit != null;
        ButtonType submitBtn = isEdit ? updateBtn : createBtn;
        getDialogPane().getButtonTypes().addAll(submitBtn, ButtonType.CANCEL);

        VBox content = new VBox();
        content.setSpacing(0);
        content.setPadding(new Insets(0));

        HBox customHeader = createCustomHeader(isEdit);
        GridPane grid = createFormGrid(categories);

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

        nameField.textProperty().addListener(e -> {
            submitButton.setDisable(nameField.getText().trim().isEmpty());
        });

        Button cancelButton = (Button) getDialogPane().lookupButton(ButtonType.CANCEL);
        cancelButton.getStyleClass().addAll("btn", "btn-secondary");

        if (isEdit) {
            loadExerciseData(exerciseToEdit);
        }

        setResultConverter(buttonType -> {
            if (buttonType == submitBtn && isValid()) {
                return getExerciseData();
            }
            return null;
        });
    }

    private HBox createCustomHeader(boolean isEdit) {
        Label titleLabel = new Label(isEdit ? "Edit Exercise" : "Create Exercise");
        titleLabel.getStyleClass().add("dialog-title");

        HBox header = new HBox(titleLabel);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setSpacing(12);
        header.setPadding(new Insets(16, 20, 16, 20));
        header.setStyle("-fx-background-color: #161628; -fx-border-color: transparent transparent #2a2a4a transparent; -fx-background-radius: 0; -fx-border-radius: 0;");
        HBox.setHgrow(titleLabel, Priority.ALWAYS);

        return header;
    }

    private GridPane createFormGrid(ObservableList<Category> categories) {
        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(12);
        grid.setPadding(new Insets(20));

        nameField.setPromptText("Exercise name");
        nameField.getStyleClass().add("text-field");

        categoryCombo.setItems(categories);
        categoryCombo.setPromptText("Select category");
        categoryCombo.getStyleClass().add("combo-box");
        categoryCombo.setCellFactory(lv -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(Category item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });
        categoryCombo.setButtonCell(new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(Category item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });

        descriptionArea.setPromptText("Description (optional)");
        descriptionArea.getStyleClass().add("text-area");
        descriptionArea.setPrefRowCount(3);
        descriptionArea.setWrapText(true);

        timeBasedCheckBox.setTextFill(javafx.scene.paint.Color.WHITE);
        timeBasedCheckBox.setStyle("-fx-font-size: 14px;");

        // Row 0: Name and Category in same row
        Label nameLabel = new Label("Name *");
        nameLabel.getStyleClass().add("form-label");
        Label categoryLabel = new Label("Category");
        categoryLabel.getStyleClass().add("form-label");

        grid.add(nameLabel, 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(categoryLabel, 2, 0);
        grid.add(categoryCombo, 3, 0);
        GridPane.setHgrow(nameField, Priority.ALWAYS);
        GridPane.setHgrow(categoryCombo, Priority.ALWAYS);

        // Row 1: Description (full width)
        Label descLabel = new Label("Description");
        descLabel.getStyleClass().add("form-label");
        grid.add(descLabel, 0, 1);
        grid.add(descriptionArea, 1, 1, 3, 1);
        GridPane.setHgrow(descriptionArea, Priority.ALWAYS);

        // Row 2: Time Based checkbox
        Label timeBasedLabel = new Label("Time Based");
        timeBasedLabel.getStyleClass().add("form-label");
        grid.add(timeBasedLabel, 0, 2);
        grid.add(timeBasedCheckBox, 1, 2);

        return grid;
    }

    private void loadExerciseData(Exercise exercise) {
        nameField.setText(exercise.getName());
        if (exercise.getCategory() != null) {
            categoryCombo.getSelectionModel().select(exercise.getCategory());
        }
        exercise.getDescriptionOptional().ifPresent(descriptionArea::setText);
        timeBasedCheckBox.setSelected(exercise.isTimeBased());
    }

    public Exercise getExerciseData() {
        Exercise e = new Exercise();
        e.setName(nameField.getText().trim());
        e.setCategory(categoryCombo.getValue());
        e.setDescription(descriptionArea.getText().trim().isEmpty() ? null : descriptionArea.getText().trim());
        e.setTimeBased(timeBasedCheckBox.isSelected());
        return e;
    }

    public boolean isValid() {
        return !nameField.getText().trim().isEmpty();
    }
}