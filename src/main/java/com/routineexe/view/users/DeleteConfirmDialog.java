package com.routineexe.view.users;

import com.routineexe.model.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;

public class DeleteConfirmDialog extends Dialog<ButtonType> {

    public DeleteConfirmDialog(User user) {
        initStyle(StageStyle.UNDECORATED);
        setTitle("Delete User");
        getDialogPane().getStyleClass().add("dialog-pane");
        getDialogPane().getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        VBox content = new VBox();
        content.setSpacing(0);
        content.setPadding(new Insets(0));

        HBox customHeader = createCustomHeader();
        VBox body = createBody(user);

        content.getChildren().addAll(customHeader, body);
        getDialogPane().setContent(content);

        getDialogPane().sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.setFill(null);
            }
        });

        Button okButton = (Button) getDialogPane().lookupButton(ButtonType.OK);
        okButton.getStyleClass().addAll("btn", "btn-danger");
        okButton.setText("Delete");

        Button cancelButton = (Button) getDialogPane().lookupButton(ButtonType.CANCEL);
        cancelButton.getStyleClass().addAll("btn", "btn-secondary");
        cancelButton.setText("Cancel");
    }

    private HBox createCustomHeader() {
        Label titleLabel = new Label("Delete User");
        titleLabel.getStyleClass().add("dialog-title");

        HBox header = new HBox(titleLabel);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setSpacing(12);
        header.setPadding(new Insets(16, 20, 16, 20));
        header.setStyle("-fx-background-color: #161628; -fx-border-color: transparent transparent #2a2a4a transparent; -fx-background-radius: 0; -fx-border-radius: 0;");
        HBox.setHgrow(titleLabel, Priority.ALWAYS);

        return header;
    }

    private VBox createBody(User user) {
        VBox body = new VBox(16);
        body.setPadding(new Insets(24));
        body.setStyle("-fx-background-color: #1a1a2e;");

        Label message = new Label("Are you sure you want to delete \"" + user.getUsername() + "\"?");
        message.setStyle("-fx-font-size: 14px; -fx-text-fill: #c0c0d0; -fx-wrap-text: true;");

        Label warning = new Label("This action cannot be undone.");
        warning.setStyle("-fx-font-size: 13px; -fx-text-fill: #e53935; -fx-font-weight: 500;");

        body.getChildren().addAll(message, warning);
        return body;
    }
}