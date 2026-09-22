package com.routineexe.view.users;

import com.routineexe.model.User;
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
import javafx.util.Callback;

public class UserListView extends TableView<User> {

    public UserListView(ObservableList<User> users) {
        super(users);
        getStyleClass().add("user-table");
        setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        TableColumn<User, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        usernameCol.setPrefWidth(180);

        TableColumn<User, String> ageCol = new TableColumn<>("Age");
        ageCol.setCellValueFactory(cellData -> {
            Integer age = cellData.getValue().getAge().orElse(null);
            return new SimpleStringProperty(age != null ? age.toString() : "-");
        });
        ageCol.setPrefWidth(80);

        TableColumn<User, String> heightCol = new TableColumn<>("Height (cm)");
        heightCol.setCellValueFactory(cellData -> {
            Double height = cellData.getValue().getHeight().orElse(null);
            return new SimpleStringProperty(height != null ? String.format("%.1f", height) : "-");
        });
        heightCol.setPrefWidth(100);

        TableColumn<User, String> weightCol = new TableColumn<>("Weight (kg)");
        weightCol.setCellValueFactory(cellData -> {
            Double weight = cellData.getValue().getWeight().orElse(null);
            return new SimpleStringProperty(weight != null ? String.format("%.1f", weight) : "-");
        });
        weightCol.setPrefWidth(100);

        TableColumn<User, User> actionsCol = new TableColumn<>("Actions");
        actionsCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue()));
        actionsCol.setPrefWidth(120);
        actionsCol.setCellFactory(createActionCellFactory());

        getColumns().addAll(usernameCol, ageCol, heightCol, weightCol, actionsCol);
        setPrefHeight(300);
    }

    private Callback<TableColumn<User, User>, TableCell<User, User>> createActionCellFactory() {
        return col -> new TableCell<>() {
            private final javafx.scene.control.Button editBtn = createIconButton(FontAwesomeIcons.PEN_TO_SQUARE, "btn-icon btn-edit");
            private final javafx.scene.control.Button deleteBtn = createIconButton(FontAwesomeIcons.TRASH, "btn-icon btn-delete");
            private final javafx.scene.layout.HBox pane = new javafx.scene.layout.HBox(4, editBtn, deleteBtn);

            {
                pane.setAlignment(javafx.geometry.Pos.CENTER);
                editBtn.setTooltip(new Tooltip("Edit"));
                deleteBtn.setTooltip(new Tooltip("Delete"));
                editBtn.setOnAction(e -> {
                    User user = getTableView().getItems().get(getIndex());
                    fireEvent(new UserActionEvent(UserActionEvent.EDIT, user));
                    e.consume();
                });
                deleteBtn.setOnAction(e -> {
                    User user = getTableView().getItems().get(getIndex());
                    fireEvent(new UserActionEvent(UserActionEvent.DELETE, user));
                    e.consume();
                });
            }

            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                if (empty || user == null) {
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
}