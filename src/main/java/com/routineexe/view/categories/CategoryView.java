package com.routineexe.view.categories;

import com.routineexe.model.Category;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class CategoryView extends VBox {

    private final Label title = new Label("Categories");
    private final CategoryListView categoryListView;
    private final CategoryToolbar toolbar;
    private HBox headerRow;
    private final boolean showHeader;

    public CategoryView(ObservableList<Category> categories, CategoryListView categoryListView, CategoryToolbar toolbar) {
        this(categories, categoryListView, toolbar, true);
    }

    public CategoryView(ObservableList<Category> categories, CategoryListView categoryListView, CategoryToolbar toolbar, boolean showHeader) {
        this.categoryListView = categoryListView;
        this.toolbar = toolbar;
        this.showHeader = showHeader;

        title.getStyleClass().add("title");
        getStyleClass().add("root");

        if (showHeader) {
            // Header row with title and button (justify-content-between using spacer)
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            headerRow = new HBox(title, spacer, toolbar.getCreateButton());
            headerRow.setAlignment(Pos.CENTER);
            headerRow.setPadding(new Insets(0, 24, 0, 24));
            headerRow.setPrefHeight(56);
            headerRow.setMinHeight(56);
            headerRow.setMaxHeight(56);
            headerRow.setStyle("-fx-background-color: #1a1a2e; -fx-border-color: transparent transparent #2a2a4a transparent;");

            // Content wrapper with proper margins
            VBox contentWrapper = new VBox(categoryListView);
            contentWrapper.setPadding(new Insets(24));
            VBox.setVgrow(contentWrapper, Priority.ALWAYS);
            VBox.setVgrow(categoryListView, Priority.ALWAYS);

            setSpacing(0);
            setPadding(new Insets(0));

            getChildren().addAll(headerRow, contentWrapper);
            VBox.setVgrow(this, Priority.ALWAYS);
        } else {
            // Content only - for use inside DashboardLayout
            setSpacing(0);
            setPadding(new Insets(24));
            getChildren().add(categoryListView);
            VBox.setVgrow(this, Priority.ALWAYS);
            VBox.setVgrow(categoryListView, Priority.ALWAYS);
        }
    }

    public CategoryListView getCategoryListView() {
        return categoryListView;
    }

    public CategoryToolbar getToolbar() {
        return toolbar;
    }

    public HBox getHeaderRow() {
        return headerRow;
    }
}