package com.routineexe.view.users;

import com.routineexe.model.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class UserFormDialog extends Dialog<User> {

    private final TextField usernameField = new TextField();
    private final TextField ageField = new TextField();
    private final TextField heightField = new TextField();
    private final TextField weightField = new TextField();
    private final ButtonType createBtn = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
    private final ButtonType updateBtn = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
    private User editingUser = null;

    public UserFormDialog() {
        this(null);
    }

    public UserFormDialog(User userToEdit) {
        this.editingUser = userToEdit;
        initStyle(StageStyle.UNDECORATED);
        setTitle(userToEdit == null ? "Create User" : "Edit User");
        setResizable(true);
        getDialogPane().getStyleClass().add("dialog-pane");
        getDialogPane().getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        boolean isEdit = userToEdit != null;
        ButtonType submitBtn = isEdit ? updateBtn : createBtn;
        getDialogPane().getButtonTypes().addAll(submitBtn, ButtonType.CANCEL);

        VBox content = new VBox();
        content.setSpacing(0);
        content.setPadding(new Insets(0));
        content.setMaxWidth(500);

        HBox customHeader = createCustomHeader(isEdit);
        GridPane grid = createFormGrid();

        content.getChildren().addAll(customHeader, grid);
        getDialogPane().setContent(content);

        getDialogPane().sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.setFill(null);
                Stage dlgStage = (Stage) getDialogPane().getScene().getWindow();
                if (dlgStage != null) {
                    dlgStage.setWidth(540);
                    dlgStage.setMinWidth(400);
                    dlgStage.setMaxWidth(600);
                }
            }
        });

        Button submitButton = (Button) getDialogPane().lookupButton(submitBtn);
        submitButton.getStyleClass().addAll("btn", "btn-primary");
        submitButton.setDisable(true);

        usernameField.textProperty().addListener(e -> {
            submitButton.setDisable(usernameField.getText().trim().isEmpty());
        });

        Button cancelButton = (Button) getDialogPane().lookupButton(ButtonType.CANCEL);
        cancelButton.getStyleClass().addAll("btn", "btn-secondary");

        if (isEdit) {
            loadUserData(userToEdit);
        }

        setResultConverter(buttonType -> {
            if (buttonType == submitBtn && isValid()) {
                return getUserData();
            }
            return null;
        });
    }

    private HBox createCustomHeader(boolean isEdit) {
        Label titleLabel = new Label(isEdit ? "Edit User" : "Create User");
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

        usernameField.setPromptText("Username");
        usernameField.getStyleClass().add("text-field");
        ageField.setPromptText("Age (years)");
        ageField.getStyleClass().add("text-field");
        heightField.setPromptText("Height (cm)");
        heightField.getStyleClass().add("text-field");
        weightField.setPromptText("Weight (kg)");
        weightField.getStyleClass().add("text-field");

        // Row 0: Username (full width)
        Label usernameLabel = new Label("Username *");
        usernameLabel.getStyleClass().add("form-label");
        grid.add(usernameLabel, 0, 0);
        grid.add(usernameField, 1, 0);
        GridPane.setHgrow(usernameField, Priority.ALWAYS);
        GridPane.setColumnSpan(usernameField, 2);

        // Row 1: Age, Height, Weight in same row with labels
        Label ageLabel = new Label("Age *");
        ageLabel.getStyleClass().add("form-label");
        Label heightLabel = new Label("Height (cm) *");
        heightLabel.getStyleClass().add("form-label");
        Label weightLabel = new Label("Weight (kg) *");
        weightLabel.getStyleClass().add("form-label");

        VBox ageBox = new VBox(4, ageLabel, ageField);
        VBox heightBox = new VBox(4, heightLabel, heightField);
        VBox weightBox = new VBox(4, weightLabel, weightField);

        ageBox.setPrefWidth(150);
        heightBox.setPrefWidth(150);
        weightBox.setPrefWidth(150);
        HBox.setHgrow(ageBox, Priority.ALWAYS);
        HBox.setHgrow(heightBox, Priority.ALWAYS);
        HBox.setHgrow(weightBox, Priority.ALWAYS);

        HBox fieldsRow = new HBox(12, ageBox, heightBox, weightBox);
        fieldsRow.setAlignment(Pos.CENTER_LEFT);

        grid.add(fieldsRow, 0, 1);
        GridPane.setColumnSpan(fieldsRow, 2);

        return grid;
    }

    private void loadUserData(User user) {
        usernameField.setText(user.getUsername());
        user.getAge().ifPresent(a -> ageField.setText(String.valueOf(a)));
        user.getHeight().ifPresent(h -> heightField.setText(String.format("%.1f", h)));
        user.getWeight().ifPresent(w -> weightField.setText(String.format("%.1f", w)));
    }

    public User getUserData() {
        User u = new User();
        u.setUsername(usernameField.getText().trim());
        u.setAge(parseInteger(ageField.getText()));
        u.setHeight(parseDouble(heightField.getText()));
        u.setWeight(parseDouble(weightField.getText()));
        return u;
    }

    public boolean isValid() {
        return !usernameField.getText().trim().isEmpty();
    }

    private Integer parseInteger(String text) {
        if (text == null || text.trim().isEmpty()) return null;
        try { return Integer.parseInt(text.trim()); } catch (NumberFormatException e) { return null; }
    }

    private Double parseDouble(String text) {
        if (text == null || text.trim().isEmpty()) return null;
        try { return Double.parseDouble(text.trim()); } catch (NumberFormatException e) { return null; }
    }
}