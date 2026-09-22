package com.routineexe.view.routines;

import com.routineexe.model.Routine;
import com.routineexe.model.DayOfWeek;
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
import java.time.LocalDate;

public class RoutineListView extends TableView<Routine> {

    public RoutineListView(ObservableList<Routine> routines) {
        super(routines);
        getStyleClass().add("user-table");
        setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        VBox.setVgrow(this, Priority.ALWAYS);

        TableColumn<Routine, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(200);

        TableColumn<Routine, String> sinceCol = new TableColumn<>("Since");
        sinceCol.setCellValueFactory(cellData -> {
            LocalDate since = cellData.getValue().getSince();
            return new SimpleStringProperty(since != null ? since.toString() : "-");
        });
        sinceCol.setPrefWidth(120);

        TableColumn<Routine, String> untilCol = new TableColumn<>("Until");
        untilCol.setCellValueFactory(cellData -> {
            LocalDate until = cellData.getValue().getUntil();
            return new SimpleStringProperty(until != null ? until.toString() : "-");
        });
        untilCol.setPrefWidth(120);

        TableColumn<Routine, String> daysCol = new TableColumn<>("Days");
        daysCol.setCellValueFactory(cellData -> {
            String days = cellData.getValue().getDays().stream()
                .map(DayOfWeek::getShortName)
                .reduce((a, b) -> a + " " + b)
                .orElse("-");
            return new SimpleStringProperty(days);
        });
        daysCol.setPrefWidth(200);

        TableColumn<Routine, Routine> actionsCol = new TableColumn<>("Actions");
        actionsCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue()));
        actionsCol.setPrefWidth(160);
        actionsCol.setCellFactory(createActionCellFactory());

        getColumns().addAll(nameCol, sinceCol, untilCol, daysCol, actionsCol);

        setPlaceholder(new Label("No routines yet"));
    }

    private Callback<TableColumn<Routine, Routine>, TableCell<Routine, Routine>> createActionCellFactory() {
        return col -> new TableCell<>() {
            private final javafx.scene.control.Button editBtn = createIconButton(com.routineexe.util.FontAwesomeIcons.PEN_TO_SQUARE, "btn-icon btn-edit");
            private final javafx.scene.control.Button deleteBtn = createIconButton(com.routineexe.util.FontAwesomeIcons.TRASH, "btn-icon btn-delete");
            private final javafx.scene.control.Button assignBtn = createIconButton(com.routineexe.util.FontAwesomeIcons.DUMBBELL, "btn-icon btn-assign");
            private final HBox pane = new HBox(4, editBtn, assignBtn, deleteBtn);

            {
                pane.setAlignment(javafx.geometry.Pos.CENTER);
                editBtn.setTooltip(new Tooltip("Edit"));
                assignBtn.setTooltip(new Tooltip("Assign exercises"));
                deleteBtn.setTooltip(new Tooltip("Delete"));
                editBtn.setOnAction(e -> {
                    Routine routine = getTableView().getItems().get(getIndex());
                    getTableView().getSelectionModel().select(routine);
                    fireEvent(new RoutineActionEvent(RoutineActionEvent.EDIT, routine));
                    e.consume();
                });
                assignBtn.setOnAction(e -> {
                    Routine routine = getTableView().getItems().get(getIndex());
                    getTableView().getSelectionModel().select(routine);
                    fireEvent(new RoutineActionEvent(RoutineActionEvent.ASSIGN, routine));
                    e.consume();
                });
                deleteBtn.setOnAction(e -> {
                    Routine routine = getTableView().getItems().get(getIndex());
                    getTableView().getSelectionModel().select(routine);
                    fireEvent(new RoutineActionEvent(RoutineActionEvent.DELETE, routine));
                    e.consume();
                });
            }

            @Override
            protected void updateItem(Routine routine, boolean empty) {
                super.updateItem(routine, empty);
                if (empty || routine == null) {
                    setGraphic(null);
                } else {
                    setGraphic(pane);
                }
            }
        };
    }

    private javafx.scene.control.Button createIconButton(String iconUnicode, String styleClass) {
        Label icon = new Label(iconUnicode);
        icon.setStyle("-fx-font-family: 'Font Awesome 6 Free'; -fx-font-weight: 900; -fx-font-size: 14px; -fx-text-fill: white;");

        javafx.scene.control.Button btn = new javafx.scene.control.Button();
        btn.setGraphic(icon);
        btn.getStyleClass().addAll(styleClass.split("\\s+"));
        btn.setPrefSize(32, 32);
        btn.setPadding(javafx.geometry.Insets.EMPTY);
        return btn;
    }

    public void setOnEdit(javafx.event.EventHandler<RoutineActionEvent> handler) {
        addEventHandler(RoutineActionEvent.EDIT, handler);
    }

    public void setOnAssign(javafx.event.EventHandler<RoutineActionEvent> handler) {
        addEventHandler(RoutineActionEvent.ASSIGN, handler);
    }

    public void setOnDelete(javafx.event.EventHandler<RoutineActionEvent> handler) {
        addEventHandler(RoutineActionEvent.DELETE, handler);
    }
}