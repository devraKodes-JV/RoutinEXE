package com.routineexe.view.sessions;

import com.routineexe.model.Session;
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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SessionListView extends TableView<Session> {

    public SessionListView(ObservableList<Session> sessions) {
        super(sessions);
        getStyleClass().add("user-table");
        setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        VBox.setVgrow(this, Priority.ALWAYS);

        TableColumn<Session, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(cellData -> {
            LocalDate date = cellData.getValue().getDate();
            return new SimpleStringProperty(date != null ? date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "-");
        });
        dateCol.setPrefWidth(120);

        TableColumn<Session, String> dayCol = new TableColumn<>("Day");
        dayCol.setCellValueFactory(cellData -> {
            String day = cellData.getValue().getDayOfWeek() != null ? cellData.getValue().getDayOfWeek().getFullName() : "-";
            return new SimpleStringProperty(day);
        });
        dayCol.setPrefWidth(100);

        TableColumn<Session, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(cellData -> {
            Session session = cellData.getValue();
            return new SimpleStringProperty(session.getSessionDayDone() ? "Done" : "Pending");
        });
        statusCol.setPrefWidth(100);
        statusCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    if ("Done".equals(item)) {
                        setStyle("-fx-text-fill: #4caf50; -fx-font-weight: 600;");
                    } else {
                        setStyle("-fx-text-fill: #ff9800; -fx-font-weight: 600;");
                    }
                }
            }
        });

        TableColumn<Session, Session> actionsCol = new TableColumn<>("Actions");
        actionsCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue()));
        actionsCol.setPrefWidth(160);
        actionsCol.setCellFactory(createActionCellFactory());

        getColumns().addAll(dateCol, dayCol, statusCol, actionsCol);

        setPlaceholder(new Label("No sessions yet"));
    }

    private Callback<TableColumn<Session, Session>, TableCell<Session, Session>> createActionCellFactory() {
        return col -> new TableCell<>() {
            private final javafx.scene.control.Button editBtn = createIconButton(FontAwesomeIcons.PEN_TO_SQUARE, "btn-icon btn-edit");
            private final javafx.scene.control.Button deleteBtn = createIconButton(FontAwesomeIcons.TRASH, "btn-icon btn-delete");
            private final javafx.scene.control.Button toggleBtn = createIconButton(FontAwesomeIcons.CHECK_CIRCLE, "btn-icon btn-toggle");
            private final HBox pane = new HBox(4, editBtn, toggleBtn, deleteBtn);

            {
                pane.setAlignment(javafx.geometry.Pos.CENTER);
                editBtn.setTooltip(new Tooltip("Edit"));
                toggleBtn.setTooltip(new Tooltip("Toggle done"));
                deleteBtn.setTooltip(new Tooltip("Delete"));
                editBtn.setOnAction(e -> {
                    Session session = getTableView().getItems().get(getIndex());
                    getTableView().getSelectionModel().select(session);
                    fireEvent(new SessionActionEvent(SessionActionEvent.EDIT, session));
                    e.consume();
                });
                toggleBtn.setOnAction(e -> {
                    Session session = getTableView().getItems().get(getIndex());
                    getTableView().getSelectionModel().select(session);
                    fireEvent(new SessionActionEvent(SessionActionEvent.TOGGLE_DONE, session));
                    e.consume();
                });
                deleteBtn.setOnAction(e -> {
                    Session session = getTableView().getItems().get(getIndex());
                    getTableView().getSelectionModel().select(session);
                    fireEvent(new SessionActionEvent(SessionActionEvent.DELETE, session));
                    e.consume();
                });
            }

            @Override
            protected void updateItem(Session session, boolean empty) {
                super.updateItem(session, empty);
                if (empty || session == null) {
                    setGraphic(null);
                } else {
                    toggleBtn.setText(session.getSessionDayDone() ? FontAwesomeIcons.CHECK_CIRCLE : FontAwesomeIcons.CIRCLE);
                    setGraphic(pane);
                }
            }
        };
    }

    private javafx.scene.control.Button createIconButton(String iconUnicode, String styleClass) {
        javafx.scene.control.Label icon = new javafx.scene.control.Label(iconUnicode);
        icon.setStyle("-fx-font-family: 'Font Awesome 6 Free'; -fx-font-weight: 900; -fx-font-size: 14px; -fx-text-fill: white;");

        javafx.scene.control.Button btn = new javafx.scene.control.Button();
        btn.setGraphic(icon);
        btn.getStyleClass().addAll(styleClass.split("\\s+"));
        btn.setPrefSize(32, 32);
        btn.setPadding(javafx.geometry.Insets.EMPTY);
        return btn;
    }

    public void setOnEdit(javafx.event.EventHandler<SessionActionEvent> handler) {
        addEventHandler(SessionActionEvent.EDIT, handler);
    }

    public void setOnDelete(javafx.event.EventHandler<SessionActionEvent> handler) {
        addEventHandler(SessionActionEvent.DELETE, handler);
    }

    public void setOnToggleDone(javafx.event.EventHandler<SessionActionEvent> handler) {
        addEventHandler(SessionActionEvent.TOGGLE_DONE, handler);
    }
}