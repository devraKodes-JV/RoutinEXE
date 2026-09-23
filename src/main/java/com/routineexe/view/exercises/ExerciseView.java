package com.routineexe.view.exercises;

import com.routineexe.model.Category;
import com.routineexe.model.Exercise;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

public class ExerciseView extends VBox {

    private final Label title = new Label("Exercises");
    private final ExerciseListView exerciseListView;
    private final ExerciseToolbar toolbar;
    private HBox titleBar;
    private HBox filterBar;
    private final boolean showHeader;
    private ComboBox<Category> categoryFilter;
    private TextField searchField;
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

            Button createBtn = toolbar.getCreateButton();

            HBox titleContent = new HBox(title, spacer, createBtn);
            titleContent.setAlignment(Pos.CENTER_LEFT);
            titleContent.setPadding(new Insets(0, 24, 0, 24));
            titleContent.setPrefHeight(56);
            titleContent.setMinHeight(56);
            titleContent.setMaxHeight(56);
            titleContent.setStyle("-fx-background-color: #1a1a2e; -fx-border-color: transparent transparent #2a2a4a transparent;");
            titleBar = titleContent;

            categoryFilter = new ComboBox<>();
            categoryFilter.setPromptText("All categories");
            categoryFilter.setPrefWidth(180);
            categoryFilter.setConverter(new StringConverter<Category>() {
                @Override
                public String toString(Category category) {
                    return category == null ? "All categories" : category.getName();
                }

                @Override
                public Category fromString(String string) {
                    return null;
                }
            });
            categoryFilter.setStyle("-fx-background-color: #0f0f1a; -fx-text-fill: #e8e8f0; -fx-font-size: 13px; -fx-pref-height: 32px; -fx-cursor: hand;");
            categoryFilter.valueProperty().addListener((obs, oldVal, newVal) -> {
                exerciseListView.setCategoryFilter(newVal);
            });

            filterLabel = new Label("Filter:");
            filterLabel.setStyle("-fx-text-fill: #c0c0d0; -fx-font-size: 13px;");

            HBox filterLeft = new HBox(6, filterLabel, categoryFilter);
            filterLeft.setAlignment(Pos.CENTER_LEFT);

            searchField = new TextField();
            searchField.setPromptText("Search exercises...");
            searchField.setPrefWidth(200);
            searchField.setStyle("-fx-background-color: #0f0f1a; -fx-text-fill: #e8e8f0; -fx-font-size: 13px; -fx-pref-height: 32px; -fx-prompt-text-fill: #606078;");
            searchField.textProperty().addListener((obs, oldVal, newVal) -> {
                exerciseListView.setSearchFilter(newVal);
            });

            filterBar = new HBox(12, filterLeft, searchField);
            filterBar.setAlignment(Pos.CENTER_LEFT);
            filterBar.setPadding(new Insets(12, 24, 12, 24));
            HBox.setHgrow(searchField, Priority.ALWAYS);

            VBox contentWrapper = new VBox(exerciseListView);
            contentWrapper.setPadding(new Insets(0, 24, 0, 24));
            VBox.setVgrow(contentWrapper, Priority.ALWAYS);
            VBox.setVgrow(exerciseListView, Priority.ALWAYS);

            HBox paginationBar = exerciseListView.getPaginationBar();

            setSpacing(0);
            setPadding(new Insets(0));

            getChildren().addAll(titleBar, filterBar, contentWrapper, paginationBar);
            VBox.setVgrow(this, Priority.ALWAYS);
            VBox.setVgrow(contentWrapper, Priority.ALWAYS);
            VBox.setVgrow(exerciseListView, Priority.ALWAYS);
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

    public HBox getTitleBar() {
        return titleBar;
    }
}
