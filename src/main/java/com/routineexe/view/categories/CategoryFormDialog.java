package com.routineexe.view.categories;

import com.routineexe.model.Category;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import javafx.stage.StageStyle;

public class CategoryFormDialog extends Dialog<Category> {

    private final TextField nameField = new TextField();
    private final ButtonType createBtn = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
    private final ButtonType updateBtn = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
    private Category editingCategory = null;

    public CategoryFormDialog() {
        this(null);
    }

    public CategoryFormDialog(Category categoryToEdit) {
        this.editingCategory = categoryToEdit;
        initStyle(StageStyle.UNDECORATED);
        setTitle(categoryToEdit == null ? "Create Category" : "Edit Category");
        setResizable(true);
        getDialogPane().getStyleClass().add("dialog-pane");
        getDialogPane().getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        boolean isEdit = categoryToEdit != null;
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

        Button submitButton = (Button) getDialogPane().lookupButton(submitBtn);
        submitButton.getStyleClass().addAll("btn", "btn-primary");
        submitButton.setDisable(true);

        nameField.textProperty().addListener(e -> {
            submitButton.setDisable(nameField.getText().trim().isEmpty());
        });

        Button cancelButton = (Button) getDialogPane().lookupButton(ButtonType.CANCEL);
        cancelButton.getStyleClass().addAll("btn", "btn-secondary");

        if (isEdit) {
            loadCategoryData(categoryToEdit);
        }

        setResultConverter(buttonType -> {
            if (buttonType == submitBtn && isValid()) {
                return getCategoryData();
            }
            return null;
        });
    }

    private HBox createCustomHeader(boolean isEdit) {
        Label titleLabel = new Label(isEdit ? "Edit Category" : "Create Category");
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

        nameField.setPromptText("Enter category name");
        nameField.getStyleClass().add("text-field");

        grid.add(new Label("Name *"), 0, 0);
        grid.add(nameField, 1, 0);
        GridPane.setHgrow(nameField, Priority.ALWAYS);

        return grid;
    }

    private void loadCategoryData(Category category) {
        nameField.setText(category.getName());
    }

    public Category getCategoryData() {
        Category c = new Category();
        c.setName(nameField.getText().trim());
        return c;
    }

    public boolean isValid() {
        return !nameField.getText().trim().isEmpty();
    }
}