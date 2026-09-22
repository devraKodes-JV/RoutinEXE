package com.routineexe.view.routines;

import com.routineexe.model.Routine;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class RoutineView extends VBox {

    private final Label title = new Label("Routines");
    private final RoutineListView routineListView;
    private final RoutineToolbar toolbar;
    private HBox headerRow;
    private final boolean showHeader;

    public RoutineView(ObservableList<Routine> routines, RoutineListView routineListView, RoutineToolbar toolbar) {
        this(routines, routineListView, toolbar, true);
    }

    public RoutineView(ObservableList<Routine> routines, RoutineListView routineListView, RoutineToolbar toolbar, boolean showHeader) {
        this.routineListView = routineListView;
        this.toolbar = toolbar;
        this.showHeader = showHeader;

        title.getStyleClass().add("title");
        getStyleClass().add("root");

        if (showHeader) {
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            headerRow = new HBox(title, spacer, toolbar.getCreateButton());
            headerRow.setAlignment(Pos.CENTER);
            headerRow.setPadding(new Insets(0, 24, 0, 24));
            headerRow.setPrefHeight(56);
            headerRow.setMinHeight(56);
            headerRow.setMaxHeight(56);
            headerRow.setStyle("-fx-background-color: #1a1a2e; -fx-border-color: transparent transparent #2a2a4a transparent;");

            VBox contentWrapper = new VBox(routineListView);
            contentWrapper.setPadding(new Insets(24));
            VBox.setVgrow(contentWrapper, Priority.ALWAYS);
            VBox.setVgrow(routineListView, Priority.ALWAYS);

            setSpacing(0);
            setPadding(new Insets(0));

            getChildren().addAll(headerRow, contentWrapper);
            VBox.setVgrow(this, Priority.ALWAYS);
        } else {
            setSpacing(0);
            setPadding(new Insets(24));
            getChildren().add(routineListView);
            VBox.setVgrow(this, Priority.ALWAYS);
            VBox.setVgrow(routineListView, Priority.ALWAYS);
        }
    }

    public RoutineListView getRoutineListView() {
        return routineListView;
    }

    public RoutineToolbar getToolbar() {
        return toolbar;
    }

    public HBox getHeaderRow() {
        return headerRow;
    }
}