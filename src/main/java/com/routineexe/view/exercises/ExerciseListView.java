package com.routineexe.view.exercises;

import com.routineexe.model.Category;
import com.routineexe.model.Exercise;
import com.routineexe.util.FontAwesomeIcons;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class ExerciseListView extends TableView<Exercise> {

    private static final int PAGE_SIZE = 10;

    private final FilteredList<Exercise> filteredList;
    private final ObservableList<Exercise> masterList;
    private Category categoryFilter = null;
    private String searchFilter = "";
    private int currentPage = 0;
    private int totalPages = 0;

    private final Label pageLabel = new Label();
    private final Button prevBtn = createPageButton("‹");
    private final Button nextBtn = createPageButton("›");

    public ExerciseListView(ObservableList<Exercise> exercises) {
        super(exercises);
        getStyleClass().add("user-table");
        setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        VBox.setVgrow(this, Priority.ALWAYS);

        this.masterList = exercises;
        this.filteredList = new FilteredList<>(exercises, e -> true);
        super.setItems(filteredList);

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

        updatePagination();
    }

    public void setCategoryFilter(Category category) {
        this.categoryFilter = category;
        this.currentPage = 0;
        applyFilters();
    }

    public void setSearchFilter(String search) {
        this.searchFilter = search != null ? search.trim().toLowerCase() : "";
        this.currentPage = 0;
        applyFilters();
    }

    private void applyFilters() {
        filteredList.setPredicate(exercise -> {
            boolean matchesSearch = searchFilter.isEmpty()
                    || (exercise.getName() != null && exercise.getName().toLowerCase().contains(searchFilter));
            boolean matchesCategory = categoryFilter == null
                    || (exercise.getCategory() != null && exercise.getCategory().getId().equals(categoryFilter.getId()));
            return matchesSearch && matchesCategory;
        });
        updatePagination();
        showCurrentPage();
    }

    private void showCurrentPage() {
        int fromIndex = currentPage * PAGE_SIZE;
        int toIndex = Math.min(fromIndex + PAGE_SIZE, filteredList.size());
        ObservableList<Exercise> pageItems = FXCollections.emptyObservableList();
        if (fromIndex < filteredList.size()) {
            pageItems = FXCollections.observableArrayList(filteredList.subList(fromIndex, toIndex));
        }
        super.setItems(pageItems);
    }

    private void updatePagination() {
        totalPages = (int) Math.ceil((double) filteredList.size() / PAGE_SIZE);
        if (totalPages == 0) totalPages = 1;

        pageLabel.setText((currentPage + 1) + " / " + totalPages);
        prevBtn.setDisable(currentPage == 0);
        nextBtn.setDisable(currentPage >= totalPages - 1);
    }

    public void previousPage() {
        if (currentPage > 0) {
            currentPage--;
            showCurrentPage();
            updatePagination();
        }
    }

    public void nextPage() {
        if (currentPage < totalPages - 1) {
            currentPage++;
            showCurrentPage();
            updatePagination();
        }
    }

    public HBox getPaginationBar() {
        HBox paginationBar = new HBox(10, prevBtn, pageLabel, nextBtn);
        paginationBar.setAlignment(Pos.CENTER);
        paginationBar.setPadding(new javafx.geometry.Insets(12, 0, 0, 0));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        paginationBar.getChildren().add(1, spacer);

        return paginationBar;
    }

    private Button createPageButton(String text) {
        Button btn = new Button(text);
        btn.getStyleClass().add("btn-icon");
        btn.setPrefSize(32, 32);
        return btn;
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
