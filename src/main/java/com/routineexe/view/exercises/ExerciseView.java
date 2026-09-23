package com.routineexe.view.exercises;

import com.routineexe.model.Category;
import com.routineexe.model.Exercise;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class ExerciseView extends VBox {

    private final Label title = new Label("Exercises");
    private final ExerciseListView exerciseListView;
    private final ExerciseToolbar toolbar;
    private HBox headerRow;
    private final boolean showHeader;
    private ComboBox<Category> categoryFilter;
    private Label filterLabel;

    public ExerciseView(ObservableList<Exercise> exercises, ExerciseListView exerciseListView, ExerciseToolbar toolbar) {
        this(exercises, exerciseListView, toolbar, true);
    }

    public ExerciseView(ObservableList<Exercise> exercises, ExerciseListView exerciseListView, ExerciseToolbar toolbar, boolean showHeader) {
        this.exerciseListView = exerciseListView;
        this.toolbar = toolbar;
        this.showHeader = showHeader;

        title.getStyleClass().add("title");
        getStyleClass().add("root");

        if (showHeader) {
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            categoryFilter = new ComboBox<>();
            categoryFilter.setPromptText("All categories");
            categoryFilter.setPrefWidth(180);
            categoryFilter.valueProperty().addListener((obs, oldVal, newVal) -> {
                exerciseListView.setCategoryFilter(newVal);
            });

            filterLabel = new Label("Filter:");
            filterLabel.setStyle("-fx-text-fill: #c0c0d0; -fx-font-size: 13px;");

            HBox filterBox = new HBox(6, filterLabel, categoryFilter);
            filterBox.setAlignment(Pos.CENTER_LEFT);

            headerRow = new HBox(title, filterBox, spacer, toolbar.getCreateButton());
            headerRow.setAlignment(Pos.CENTER);
            headerRow.setPadding(new Insets(0, 24, 0, 24));
            headerRow.setPrefHeight(56);
            headerRow.setMinHeight(56);
            headerRow.setMaxHeight(56);
            headerRow.setStyle("-fx-background-color: #1a1a2e; -fx-border-color: transparent transparent #2a2a4a transparent;");

            VBox contentWrapper = new VBox(exerciseListView);
            contentWrapper.setPadding(new Insets(24));
            VBox.setVgrow(contentWrapper, Priority.ALWAYS);
            VBox.setVgrow(exerciseListView, Priority.ALWAYS);

            HBox paginationBar = exerciseListView.getPaginationBar();

            setSpacing(0);
            setPadding(new Insets(0));

            getChildren().addAll(headerRow, contentWrapper, paginationBar);
            VBox.setVgrow(this, Priority.ALWAYS);
        } else {
            setSpacing(0);
            setPadding(new Insets(24));
            getChildren().add(exerciseListView);
            VBox.setVgrow(this, Priority.ALWAYS);
            VBox.setVgrow(exerciseListView, Priority.ALWAYS);
        }
    }

    public void setCategoryOptions(ObservableList<Category> categories) {
        if (categoryFilter != null) {
            categoryFilter.getItems().setAll(categories);
            categoryFilter.getItems().add(0, null);
        }
    }

    public ExerciseListView getExerciseListView() {
        return exerciseListView;
    }

    public ExerciseToolbar getToolbar() {
        return toolbar;
    }

    public HBox getHeaderRow() {
        return headerRow;
    }
}