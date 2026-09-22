package com.routineexe.view.categories;

import com.routineexe.model.Category;
import com.routineexe.util.FontAwesomeIcons;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class CategoryListView extends TableView<Category> {

    public CategoryListView(ObservableList<Category> categories) {
        super(categories);
        getStyleClass().add("user-table");
        setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        VBox.setVgrow(this, Priority.ALWAYS);

        TableColumn<Category, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(400);

        TableColumn<Category, Category> actionsCol = new TableColumn<>("Actions");
        actionsCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue()));
        actionsCol.setPrefWidth(120);
        actionsCol.setCellFactory(createActionCellFactory());

        getColumns().addAll(nameCol, actionsCol);

        // Show placeholder when empty
        setPlaceholder(new Label("No categories yet"));
    }

    private Callback<TableColumn<Category, Category>, TableCell<Category, Category>> createActionCellFactory() {
        return col -> new TableCell<>() {
            private final javafx.scene.control.Button editBtn = createIconButton(FontAwesomeIcons.PEN_TO_SQUARE, "btn-icon btn-edit");
            private final javafx.scene.control.Button deleteBtn = createIconButton(FontAwesomeIcons.TRASH, "btn-icon btn-delete");
            private final HBox pane = new HBox(4, editBtn, deleteBtn);

            {
                pane.setAlignment(javafx.geometry.Pos.CENTER);
                editBtn.setTooltip(new Tooltip("Edit"));
                deleteBtn.setTooltip(new Tooltip("Delete"));
                editBtn.setOnAction(e -> {
                    Category category = getTableView().getItems().get(getIndex());
                    getTableView().getSelectionModel().select(category);
                    fireEvent(new CategoryActionEvent(CategoryActionEvent.EDIT, category));
                    e.consume();
                });
                deleteBtn.setOnAction(e -> {
                    Category category = getTableView().getItems().get(getIndex());
                    getTableView().getSelectionModel().select(category);
                    fireEvent(new CategoryActionEvent(CategoryActionEvent.DELETE, category));
                    e.consume();
                });
            }

            @Override
            protected void updateItem(Category category, boolean empty) {
                super.updateItem(category, empty);
                if (empty || category == null) {
                    setGraphic(null);
                } else {
                    setGraphic(pane);
                }
            }
        };
    }

    private javafx.scene.control.Button createIconButton(String iconUnicode, String styleClass) {
        Label icon = new Label(iconUnicode);
        icon.setStyle("-fx-font-family: 'Font Awesome 6 Free'; -fx-font-weight: 900; -fx-font-size: 14px;");

        javafx.scene.control.Button btn = new javafx.scene.control.Button();
        btn.setGraphic(icon);
        btn.getStyleClass().addAll(styleClass.split("\\s+"));
        btn.setPrefSize(32, 32);
        btn.setPadding(javafx.geometry.Insets.EMPTY);
        return btn;
    }

    public void setOnEdit(javafx.event.EventHandler<CategoryActionEvent> handler) {
        addEventHandler(CategoryActionEvent.EDIT, handler);
    }

    public void setOnDelete(javafx.event.EventHandler<CategoryActionEvent> handler) {
        addEventHandler(CategoryActionEvent.DELETE, handler);
    }
}