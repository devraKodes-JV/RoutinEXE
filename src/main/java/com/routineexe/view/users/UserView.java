package com.routineexe.view.users;

import com.routineexe.model.User;
import com.routineexe.util.FontAwesomeIcons;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class UserView extends VBox {

    private final ImageView title = new ImageView();
    private final UserListView userListView;
    private final Button createUserBtn;

    public UserView(ObservableList<User> users, UserListView userListView) {
        this.userListView = userListView;

        // Create User button
        Label createIcon = new Label(FontAwesomeIcons.PLUS);
        createIcon.setStyle("-fx-font-family: 'Font Awesome 6 Free'; -fx-font-weight: 900; -fx-font-size: 14px; -fx-padding: 0 8 0 0;");
        Label createLabel = new Label("Create User");
        HBox btnContent = new HBox(createIcon, createLabel);
        btnContent.setAlignment(Pos.CENTER);
        btnContent.setSpacing(4);

        createUserBtn = new Button();
        createUserBtn.getStyleClass().addAll("btn", "btn-primary");
        createUserBtn.setDefaultButton(true);
        createUserBtn.setGraphic(btnContent);

        // Header row with title and button (justify-content-between using spacer)
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox headerRow = new HBox(title, spacer, createUserBtn);
        headerRow.setAlignment(Pos.CENTER);
        headerRow.setPadding(new Insets(0, 24, 0, 24));
        headerRow.setPrefHeight(56);
        headerRow.setMinHeight(56);
        headerRow.setMaxHeight(56);
        headerRow.setStyle("-fx-background-color: #1a1a2e; -fx-border-color: transparent transparent #2a2a4a transparent;");

        // Content wrapper with max-width and margins
        VBox contentWrapper = new VBox(userListView);
        contentWrapper.setMaxWidth(1200);
        contentWrapper.setStyle("-fx-background-color: transparent;");
        HBox.setHgrow(contentWrapper, Priority.ALWAYS);
        contentWrapper.setPadding(new Insets(24, 0, 24, 0));

        HBox contentContainer = new HBox(contentWrapper);
        contentContainer.setAlignment(Pos.TOP_CENTER);
        HBox.setHgrow(contentContainer, Priority.ALWAYS);
        VBox.setVgrow(contentContainer, Priority.ALWAYS);

         title.setImage(new Image(getClass().getResourceAsStream("/rountinexe-name.png")));
         title.setFitHeight(32);
         title.setPreserveRatio(true);
         title.getStyleClass().add("title");
         getStyleClass().add("root");
        setSpacing(0);
        setPadding(new Insets(0));

        getChildren().addAll(headerRow, contentContainer);
    }

    public UserListView getUserListView() {
        return userListView;
    }

    public Button getCreateButton() {
        return createUserBtn;
    }
}