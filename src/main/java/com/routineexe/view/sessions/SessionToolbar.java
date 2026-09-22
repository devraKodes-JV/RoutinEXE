package com.routineexe.view.sessions;

import com.routineexe.util.FontAwesomeIcons;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class SessionToolbar extends VBox {

    public SessionToolbar() {
        setSpacing(12);
        setPadding(new Insets(16, 24, 16, 24));
        setStyle("-fx-background-color: #1a1a2e; -fx-border-color: transparent transparent #2a2a4a transparent;");

        HBox toolbarContent = new HBox();
        toolbarContent.setAlignment(Pos.CENTER_LEFT);
        toolbarContent.setSpacing(12);

        Label title = new Label("Sessions");
        title.getStyleClass().add("dashboard-title");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        toolbarContent.getChildren().addAll(title, spacer);
        getChildren().add(toolbarContent);
    }
}