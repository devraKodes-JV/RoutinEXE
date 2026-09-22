package com.routineexe.view.exercises;

import com.routineexe.model.Exercise;
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

public class ExerciseListView extends TableView<Exercise> {

    public ExerciseListView(ObservableList<Exercise> exercises) {
        super(exercises);
        getStyleClass().add("user-table");
        setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        VBox.setVgrow(this, Priority.ALWAYS);

        TableColumn<Exercise, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(200);

        TableColumn<Exercise, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(cellData -> {
            String catName = cellData.getValue().getCategory() != null 
                    ? cellData.getValue().getCategory().getName() : "-";
            return new SimpleStringProperty(catName);
        });
        categoryCol.setPrefWidth(180);

        TableColumn<Exercise, String> timeBasedCol = new TableColumn<>("Time Based");
        timeBasedCol.setCellValueFactory(cellData -> {
            String val = cellData.getValue().isTimeBased() ? "Yes" : "No";
            return new SimpleStringProperty(val);
        });
        timeBasedCol.setPrefWidth(100);

        TableColumn<Exercise, Exercise> actionsCol = new TableColumn<>("Actions");
        actionsCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue()));
        actionsCol.setPrefWidth(120);
        actionsCol.setCellFactory(createActionCellFactory());

        getColumns().addAll(nameCol, categoryCol, timeBasedCol, actionsCol);

        setPlaceholder(new Label("No exercises yet"));
    }

    private Callback<TableColumn<Exercise, Exercise>, TableCell<Exercise, Exercise>> createActionCellFactory() {
        return col -> new TableCell<>() {
            private final javafx.scene.control.Button editBtn = createIconButton(FontAwesomeIcons.PEN_TO_SQUARE, "btn-icon btn-edit");
            private final javafx.scene.control.Button deleteBtn = createIconButton(FontAwesomeIcons.TRASH, "btn-icon btn-delete");
            private final HBox pane = new HBox(4, editBtn, deleteBtn);

            {
                pane.setAlignment(javafx.geometry.Pos.CENTER);
                editBtn.setTooltip(new Tooltip("Edit"));
                deleteBtn.setTooltip(new Tooltip("Delete"));
                editBtn.setOnAction(e -> {
                    Exercise exercise = getTableView().getItems().get(getIndex());
                    getTableView().getSelectionModel().select(exercise);
                    fireEvent(new ExerciseActionEvent(ExerciseActionEvent.EDIT, exercise));
                    e.consume();
                });
                deleteBtn.setOnAction(e -> {
                    Exercise exercise = getTableView().getItems().get(getIndex());
                    getTableView().getSelectionModel().select(exercise);
                    fireEvent(new ExerciseActionEvent(ExerciseActionEvent.DELETE, exercise));
                    e.consume();
                });
            }

            @Override
            protected void updateItem(Exercise exercise, boolean empty) {
                super.updateItem(exercise, empty);
                if (empty || exercise == null) {
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

    public void setOnEdit(javafx.event.EventHandler<ExerciseActionEvent> handler) {
        addEventHandler(ExerciseActionEvent.EDIT, handler);
    }

    public void setOnDelete(javafx.event.EventHandler<ExerciseActionEvent> handler) {
        addEventHandler(ExerciseActionEvent.DELETE, handler);
    }
}