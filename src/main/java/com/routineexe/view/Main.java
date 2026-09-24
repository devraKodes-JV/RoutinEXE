package com.routineexe.view;

import com.routineexe.database.CategoryDAO;
import com.routineexe.database.ExerciseDAO;
import com.routineexe.database.RoutineDAO;
import com.routineexe.database.RoutineExerciseDAO;
import com.routineexe.database.SessionDAO;
import com.routineexe.database.SessionExerciseDAO;
import com.routineexe.database.UserDAO;
import com.routineexe.model.Category;
import com.routineexe.model.Exercise;
import com.routineexe.model.Routine;
import com.routineexe.model.Session;
import com.routineexe.model.User;
import com.routineexe.view.categories.CategoryFormDialog;
import com.routineexe.view.categories.CategoryListView;
import com.routineexe.view.categories.CategoryToolbar;
import com.routineexe.view.categories.CategoryView;
import com.routineexe.view.exercises.ExerciseFormDialog;
import com.routineexe.view.exercises.ExerciseListView;
import com.routineexe.view.exercises.ExerciseToolbar;
import com.routineexe.view.exercises.ExerciseView;
import com.routineexe.view.routines.RoutineActionEvent;
import com.routineexe.view.routines.RoutineExerciseAssignmentDialog;
import com.routineexe.view.routines.RoutineFormDialog;
import com.routineexe.view.routines.RoutineListView;
import com.routineexe.view.routines.RoutineToolbar;
import com.routineexe.view.routines.RoutineView;
import com.routineexe.view.sessions.SessionActionEvent;
import com.routineexe.view.sessions.SessionExerciseAssignmentDialog;
import com.routineexe.view.sessions.SessionFormDialog;
import com.routineexe.view.sessions.SessionListView;
import com.routineexe.view.sessions.SessionToolbar;
import com.routineexe.view.sessions.SessionView;
import com.routineexe.view.users.DeleteConfirmDialog;
import com.routineexe.view.users.UserActionEvent;
import com.routineexe.view.users.UserFormDialog;
import com.routineexe.view.users.UserListView;
import com.routineexe.view.users.UserView;
import com.routineexe.view.categories.CategoryActionEvent;
import com.routineexe.view.exercises.ExerciseActionEvent;
import com.routineexe.util.FontAwesomeIcons;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.time.LocalDate;
import java.util.List;

import java.io.InputStream;

public class Main extends Application {

    private UserDAO userDAO;
    private CategoryDAO categoryDAO;
    private ExerciseDAO exerciseDAO;
    private RoutineDAO routineDAO;
    private RoutineExerciseDAO routineExerciseDAO;
    private SessionDAO sessionDAO;
    private SessionExerciseDAO sessionExerciseDAO;
    private ObservableList<User> users;
    private ObservableList<Category> categories;
    private ObservableList<Exercise> exercises;
    private ObservableList<Routine> routines;
    private UserView userView;
    private UserListView userListView;
    private CategoryView categoryView;
    private CategoryListView categoryListView;
    private CategoryToolbar categoryToolbar;
    private ExerciseView exerciseView;
    private ExerciseListView exerciseListView;
    private ExerciseToolbar exerciseToolbar;
    private RoutineView routineView;
    private RoutineListView routineListView;
    private RoutineToolbar routineToolbar;
    private SessionView sessionView;
    private SessionListView sessionListView;
    private SessionToolbar sessionToolbar;
    private Stage primaryStage;
    private double xOffset = 0;
    private double yOffset = 0;
    private DashboardLayout dashboardLayout;
    private VBox sidebar;
    private Button activeSidebarBtn;
    private VBox mainContent;
    private VBox appRoot;
    private boolean inApp = false;
    private User currentUser;

    @Override
    public void init() {
        loadFontAwesomeFonts();
    }

    private void loadFontAwesomeFonts() {
        try (InputStream solid = getClass().getResourceAsStream("/fonts/fa-solid-900.ttf");
             InputStream regular = getClass().getResourceAsStream("/fonts/fa-regular-400.ttf");
             InputStream brands = getClass().getResourceAsStream("/fonts/fa-brands-400.ttf")) {
            if (solid != null) Font.loadFont(solid, 14);
            if (regular != null) Font.loadFont(regular, 14);
            if (brands != null) Font.loadFont(brands, 14);
        } catch (Exception e) {
            System.err.println("Failed to load Font Awesome fonts: " + e.getMessage());
        }
    }

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        userDAO = new UserDAO();
        categoryDAO = new CategoryDAO();
        exerciseDAO = new ExerciseDAO(categoryDAO);
        routineDAO = new RoutineDAO(exerciseDAO);
        routineExerciseDAO = new RoutineExerciseDAO(exerciseDAO);
        sessionDAO = new SessionDAO();
        sessionExerciseDAO = new SessionExerciseDAO(exerciseDAO);
        users = FXCollections.observableArrayList();
        categories = FXCollections.observableArrayList();
        exercises = FXCollections.observableArrayList();
        routines = FXCollections.observableArrayList();

        userListView = new UserListView(users);
        userView = new UserView(users, userListView);

        categoryListView = new CategoryListView(categories);
        categoryToolbar = new CategoryToolbar();
        categoryView = new CategoryView(categories, categoryListView, categoryToolbar);

        exerciseListView = new ExerciseListView(exercises);
        exerciseToolbar = new ExerciseToolbar();
        exerciseView = new ExerciseView(exercises, exerciseListView, exerciseToolbar);
        exerciseView.setCategoryOptions(categories);

        routineListView = new RoutineListView(routines);
        routineToolbar = new RoutineToolbar();
        routineView = new RoutineView(routines, routineListView, routineToolbar);

        sessionListView = new SessionListView(FXCollections.observableArrayList());
        sessionToolbar = new SessionToolbar();
        sessionView = new SessionView(this::openSessionForDate, sessionDAO, sessionExerciseDAO);
        sessionView.setOnStartSession(() -> openSessionForDate(LocalDate.now()));

        setupEventHandlers();
        setupWindowDrag();

        // Root layout: VBox with title bar at top, then content area
        VBox root = new VBox();
        root.setSpacing(0);
        root.getStyleClass().add("root");

        // Title bar (window controls) - always at top
        HBox titleBar = createTitleBar();
        root.getChildren().add(titleBar);

        // Main content area - starts with users view (no sidebar)
        mainContent = new VBox();
        mainContent.setStyle("-fx-background-color: #0f0f1a;");
        VBox.setVgrow(mainContent, Priority.ALWAYS);
        mainContent.getChildren().addAll(userView);
        VBox.setVgrow(userView, Priority.ALWAYS);

        root.getChildren().add(mainContent);

        Scene scene = new Scene(root, 1000, 600);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        scene.setFill(null);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle("RoutinEXE");
        stage.setScene(scene);
        stage.setMinWidth(800);
        stage.setMinHeight(500);
        stage.show();

        loadUsers();
        loadCategories();
        loadExercises();
        loadRoutines();
    }

    private HBox createTitleBar() {
        Label appTitle = new Label("RoutinEXE");
        appTitle.getStyleClass().add("window-title");

        Button minimizeBtn = new Button("—");
        minimizeBtn.getStyleClass().addAll("window-btn", "window-btn-minimize");
        minimizeBtn.setOnAction(e -> primaryStage.setIconified(true));

        Button maximizeBtn = new Button("□");
        maximizeBtn.getStyleClass().addAll("window-btn", "window-btn-maximize");
        maximizeBtn.setOnAction(e -> {
            if (primaryStage.isMaximized()) {
                primaryStage.setMaximized(false);
            } else {
                primaryStage.setMaximized(true);
            }
        });

        Button closeBtn = new Button("×");
        closeBtn.getStyleClass().addAll("window-btn", "window-btn-close");
        closeBtn.setOnAction(e -> primaryStage.close());

        HBox windowControls = new HBox(8, minimizeBtn, maximizeBtn, closeBtn);
        windowControls.setAlignment(Pos.CENTER);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox titleBar = new HBox(appTitle, spacer, windowControls);
        titleBar.setAlignment(Pos.CENTER);
        titleBar.setPadding(new Insets(0, 12, 0, 12));
        titleBar.setPrefHeight(56);
        titleBar.setMinHeight(56);
        titleBar.setMaxHeight(56);
        titleBar.setStyle("-fx-background-color: #1a1a2e; -fx-border-color: transparent transparent #2a2a4a transparent;");

        return titleBar;
    }

    private void setupWindowDrag() {
        userView.setOnMousePressed(event -> {
            xOffset = primaryStage.getX() - event.getScreenX();
            yOffset = primaryStage.getY() - event.getScreenY();
        });

        userView.setOnMouseDragged(event -> {
            if (!primaryStage.isMaximized()) {
                primaryStage.setX(event.getScreenX() + xOffset);
                primaryStage.setY(event.getScreenY() + yOffset);
            }
        });
    }

    private void setupEventHandlers() {
        userView.getCreateButton().setOnAction(e -> openCreateUserDialog());

        userListView.addEventHandler(UserActionEvent.EDIT, event -> {
            openEditUserDialog(event.getUser());
        });

        userListView.addEventHandler(UserActionEvent.DELETE, event -> {
            confirmDeleteUser(event.getUser());
        });

        userListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                showDashboard(newVal);
            }
        });

        categoryToolbar.getCreateButton().setOnAction(e -> openCreateCategoryDialog());

        categoryListView.setOnEdit(event -> {
            Category category = event.getCategory();
            openEditCategoryDialog(category);
        });

        categoryListView.setOnDelete(event -> {
            Category category = event.getCategory();
            confirmDeleteCategory(category);
        });

        exerciseToolbar.getCreateButton().setOnAction(e -> openCreateExerciseDialog());

        exerciseListView.setOnEdit(event -> {
            Exercise exercise = event.getExercise();
            openEditExerciseDialog(exercise);
        });

        exerciseListView.setOnDelete(event -> {
            Exercise exercise = event.getExercise();
            confirmDeleteExercise(exercise);
        });

        routineToolbar.getCreateButton().setOnAction(e -> openCreateRoutineDialog());

        routineListView.setOnEdit(event -> {
            Routine routine = event.getRoutine();
            openEditRoutineDialog(routine);
        });

        routineListView.setOnAssign(event -> {
            Routine routine = event.getRoutine();
            openAssignExercisesDialog(routine);
        });

        routineListView.setOnDelete(event -> {
            Routine routine = event.getRoutine();
            confirmDeleteRoutine(routine);
        });

        // Session handlers - now just using the calendar view with Start Session button
        // The button action is set in SessionView constructor
    }

    private void loadUsers() {
        try {
            users.setAll(userDAO.fetchAll());
        } catch (Exception e) {
            showError("Failed to load users: " + e.getMessage());
        }
    }

    private void loadCategories() {
        try {
            categories.setAll(categoryDAO.fetchAll());
        } catch (Exception e) {
            showError("Failed to load categories: " + e.getMessage());
        }
    }

    private void loadExercises() {
        try {
            exercises.setAll(exerciseDAO.fetchAll());
            exerciseListView.refreshPagination();
        } catch (Exception e) {
            showError("Failed to load exercises: " + e.getMessage());
        }
    }

    private void loadRoutines() {
        try {
            if (currentUser != null) {
                routines.setAll(routineDAO.fetchByUser(currentUser.getId()));
            } else {
                routines.setAll(routineDAO.fetchAll());
            }
        } catch (Exception e) {
            showError("Failed to load routines: " + e.getMessage());
        }
    }

    private void showDashboard(User user) {
        currentUser = user;
        if (inApp) return;
        inApp = true;

        loadRoutines();

        // Create sidebar for app
        sidebar = createAppSidebar();
        
        // Create app content area with sidebar
        HBox appContentArea = new HBox();
        appContentArea.setSpacing(0);
        HBox.setHgrow(appContentArea, Priority.ALWAYS);
        VBox.setVgrow(appContentArea, Priority.ALWAYS);

        VBox.setVgrow(sidebar, Priority.ALWAYS);
        
        // Dashboard layout as content
        dashboardLayout = new DashboardLayout(user, categories, exercises, routines,
                categoryToolbar, exerciseToolbar, routineToolbar,
                categoryListView, exerciseListView, routineListView,
                sessionListView, sessionToolbar, sessionView,
                sessionDAO, sessionExerciseDAO, routineDAO,
                () -> showUsersView(), () -> showCategoriesView(), () -> showExercisesView(), () -> showRoutinesView(), () -> showSessionsView(), () -> showUsersView());
        HBox.setHgrow(dashboardLayout, Priority.ALWAYS);
        VBox.setVgrow(dashboardLayout, Priority.ALWAYS);

        appContentArea.getChildren().addAll(sidebar, dashboardLayout);

        // Replace main content with app layout
        mainContent.getChildren().clear();
        mainContent.getChildren().addAll(appContentArea);
        
        // Update sidebar active state to Dashboard
        VBox navItems = (VBox) sidebar.getChildren().get(1);
        Button dashboardBtn = (Button) navItems.getChildren().get(1); // Dashboard is index 1
        setActiveSidebarButtonManually(dashboardBtn);
    }

    private void showUsersView() {
        currentUser = null;
        inApp = false;
        sidebar = null;
        
        // Back to simple users view (no sidebar)
        mainContent.getChildren().clear();
        mainContent.getChildren().addAll(userView);
        VBox.setVgrow(userView, Priority.ALWAYS);
        
        loadRoutines();
    }

private void showCategoriesView() {
        inApp = false;
        sidebar = null;

        // Standalone categories view (no sidebar, has its own header)
        mainContent.getChildren().clear();
        mainContent.getChildren().addAll(categoryView);
        VBox.setVgrow(categoryView, Priority.ALWAYS);
    }

    private void showExercisesView() {
        inApp = false;
        sidebar = null;

        // Standalone exercises view (no sidebar, has its own header)
        mainContent.getChildren().clear();
        mainContent.getChildren().addAll(exerciseView);
        VBox.setVgrow(exerciseView, Priority.ALWAYS);
    }

    private void showRoutinesView() {
        inApp = false;
        sidebar = null;

        // Standalone routines view (no sidebar, has its own header)
        mainContent.getChildren().clear();
        mainContent.getChildren().addAll(routineView);
        VBox.setVgrow(routineView, Priority.ALWAYS);
    }

    private void showSessionsView() {
        inApp = false;
        sidebar = null;

        // Standalone sessions view (no sidebar, has its own header)
        mainContent.getChildren().clear();
        mainContent.getChildren().addAll(sessionView);
        VBox.setVgrow(sessionView, Priority.ALWAYS);
    }

    private VBox createAppSidebar() {
        VBox sidebar = new VBox();
        sidebar.setStyle("-fx-background-color: #161628; -fx-border-color: transparent #2a2a4a transparent transparent; -fx-min-width: 240px; -fx-max-width: 240px;");
        sidebar.setPadding(new Insets(0));
        sidebar.setSpacing(0);
        VBox.setVgrow(sidebar, Priority.ALWAYS);

        // Header
        Label sidebarTitle = new Label("MENU");
        sidebarTitle.getStyleClass().add("sidebar-title");
        sidebarTitle.setPadding(new Insets(20, 24, 16, 24));

        // Nav items - takes available space
        VBox navItems = new VBox(4);
        navItems.setPadding(new Insets(0, 16, 16, 16));
        VBox.setVgrow(navItems, Priority.ALWAYS);

        Button overviewBtn = createSidebarNavButton(FontAwesomeIcons.HOUSE, "Overview", true, () -> {
            if (dashboardLayout != null) dashboardLayout.showOverview();
        });
        activeSidebarBtn = overviewBtn;
        navItems.getChildren().add(overviewBtn);

        Button categoriesBtn = createSidebarNavButton(FontAwesomeIcons.FOLDER, "Categories", false, () -> {
            if (dashboardLayout != null) dashboardLayout.showCategories();
        });
        navItems.getChildren().add(categoriesBtn);

        Button exercisesBtn = createSidebarNavButton(FontAwesomeIcons.DUMBBELL, "Exercises", false, () -> {
            if (dashboardLayout != null) dashboardLayout.showExercises();
        });
        navItems.getChildren().add(exercisesBtn);

        Button routinesBtn = createSidebarNavButton(FontAwesomeIcons.LIST_CHECK, "Routines", false, () -> {
            if (dashboardLayout != null) dashboardLayout.showRoutines();
        });
        navItems.getChildren().add(routinesBtn);

        Button sessionsBtn = createSidebarNavButton(FontAwesomeIcons.CALENDAR_CHECK, "Sessions", false, () -> {
            if (dashboardLayout != null) dashboardLayout.showSessions();
        });
        navItems.getChildren().add(sessionsBtn);

        // Footer with logout - always visible
        VBox footer = new VBox();
        footer.setPadding(new Insets(16, 24, 20, 24));
        footer.setStyle("-fx-border-color: #2a2a4a transparent transparent transparent; -fx-border-width: 1px 0 0 0;");

        Button logoutBtn = createSidebarNavButton(FontAwesomeIcons.RIGHT_FROM_BRACKET, "Logout", false, () -> showUsersView());
        logoutBtn.getStyleClass().add("sidebar-logout-btn");
        logoutBtn.setMaxWidth(Double.MAX_VALUE);

        footer.getChildren().add(logoutBtn);

        sidebar.getChildren().addAll(sidebarTitle, navItems, footer);
        return sidebar;
    }

    private Button createSidebarNavButton(String iconUnicode, String label, boolean active, Runnable action) {
        Label icon = new Label(iconUnicode);
        icon.setStyle("-fx-font-family: 'Font Awesome 6 Free'; -fx-font-weight: 900; -fx-font-size: 16px; -fx-text-fill: " + (active ? "#1e88e5" : "#808090") + "; -fx-min-width: 24px;");

        Label lbl = new Label(label);
        lbl.setStyle("-fx-font-size: 14px; -fx-text-fill: " + (active ? "#e8e8f0" : "#c0c0d0") + "; -fx-font-weight: " + (active ? "600" : "500") + ";");

        HBox content = new HBox(12, icon, lbl);
        content.setAlignment(Pos.CENTER_LEFT);
        content.setPadding(new Insets(10, 16, 10, 16));

        Button btn = new Button();
        btn.setGraphic(content);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.getStyleClass().add("sidebar-nav-btn");
        if (active) {
            btn.setStyle("-fx-background-color: #1e3a5f; -fx-background-radius: 6px;");
        } else {
            btn.setStyle("-fx-background-color: transparent; -fx-background-radius: 6px;");
        }
        if (action != null) {
            btn.setOnAction(e -> {
                setActiveSidebarButton(btn);
                action.run();
            });
        }
        btn.setOnMouseEntered(e -> {
            if (btn != activeSidebarBtn) btn.setStyle("-fx-background-color: #1e3a5f; -fx-background-radius: 6px;");
        });
        btn.setOnMouseExited(e -> {
            if (btn != activeSidebarBtn) btn.setStyle("-fx-background-color: transparent; -fx-background-radius: 6px;");
        });

        return btn;
    }

    private void setActiveSidebarButton(Button btn) {
        if (activeSidebarBtn != null) {
            activeSidebarBtn.setStyle("-fx-background-color: transparent; -fx-background-radius: 6px;");
            Label activeIcon = (Label) ((HBox) activeSidebarBtn.getGraphic()).getChildren().get(0);
            Label activeLabel = (Label) ((HBox) activeSidebarBtn.getGraphic()).getChildren().get(1);
            activeIcon.setStyle("-fx-font-family: 'Font Awesome 6 Free'; -fx-font-weight: 900; -fx-font-size: 16px; -fx-text-fill: #808090; -fx-min-width: 24px;");
            activeLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #c0c0d0; -fx-font-weight: 500;");
        }
        activeSidebarBtn = btn;
        btn.setStyle("-fx-background-color: #1e3a5f; -fx-background-radius: 6px;");
        Label newIcon = (Label) ((HBox) btn.getGraphic()).getChildren().get(0);
        Label newLabel = (Label) ((HBox) btn.getGraphic()).getChildren().get(1);
        newIcon.setStyle("-fx-font-family: 'Font Awesome 6 Free'; -fx-font-weight: 900; -fx-font-size: 16px; -fx-text-fill: #1e88e5; -fx-min-width: 24px;");
        newLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #e8e8f0; -fx-font-weight: 600;");
    }

    private void setActiveSidebarButtonManually(Button btn) {
        if (activeSidebarBtn != null) {
            activeSidebarBtn.setStyle("-fx-background-color: transparent; -fx-background-radius: 6px;");
            Label activeIcon = (Label) ((HBox) activeSidebarBtn.getGraphic()).getChildren().get(0);
            Label activeLabel = (Label) ((HBox) activeSidebarBtn.getGraphic()).getChildren().get(1);
            activeIcon.setStyle("-fx-font-family: 'Font Awesome 6 Free'; -fx-font-weight: 900; -fx-font-size: 16px; -fx-text-fill: #808090; -fx-min-width: 24px;");
            activeLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #c0c0d0; -fx-font-weight: 500;");
        }
        activeSidebarBtn = btn;
        btn.setStyle("-fx-background-color: #1e3a5f; -fx-background-radius: 6px;");
        Label newIcon = (Label) ((HBox) btn.getGraphic()).getChildren().get(0);
        Label newLabel = (Label) ((HBox) btn.getGraphic()).getChildren().get(1);
        newIcon.setStyle("-fx-font-family: 'Font Awesome 6 Free'; -fx-font-weight: 900; -fx-font-size: 16px; -fx-text-fill: #1e88e5; -fx-min-width: 24px;");
        newLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #e8e8f0; -fx-font-weight: 600;");
    }

    private void openCreateUserDialog() {
        UserFormDialog dialog = new UserFormDialog();
        dialog.showAndWait().ifPresent(user -> {
            try {
                long id = userDAO.insert(user);
                user.setId(id);
                users.add(0, user);
            } catch (Exception e) {
                showError("Failed to create user: " + e.getMessage());
            }
        });
    }

    private void openEditUserDialog(User user) {
        UserFormDialog dialog = new UserFormDialog(user);
        dialog.showAndWait().ifPresent(updatedUser -> {
            try {
                updatedUser.setId(user.getId());
                int idx = users.indexOf(user);
                if (idx >= 0) {
                    users.set(idx, updatedUser);
                }
            } catch (Exception e) {
                showError("Failed to update user: " + e.getMessage());
            }
        });
    }

    private void deleteUser(User user) {
        try {
            if (userDAO.delete(user.getId())) {
                users.remove(user);
            }
        } catch (Exception ex) {
            showError("Failed to delete user: " + ex.getMessage());
        }
    }

    private void confirmDeleteUser(User user) {
        DeleteConfirmDialog dialog = new DeleteConfirmDialog(user);
        dialog.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                deleteUser(user);
            }
        });
    }

    private void openCreateCategoryDialog() {
        CategoryFormDialog dialog = new CategoryFormDialog();
        dialog.showAndWait().ifPresent(category -> {
            try {
                long id = categoryDAO.insert(category);
                category.setId(id);
                categories.add(0, category);
            } catch (Exception e) {
                showError("Failed to create category: " + e.getMessage());
            }
        });
    }

    private void openEditCategoryDialog(Category category) {
        CategoryFormDialog dialog = new CategoryFormDialog(category);
        dialog.showAndWait().ifPresent(updatedCategory -> {
            try {
                updatedCategory.setId(category.getId());
                int idx = categories.indexOf(category);
                if (idx >= 0) {
                    categories.set(idx, updatedCategory);
                }
            } catch (Exception e) {
                showError("Failed to update category: " + e.getMessage());
            }
        });
    }

    private void deleteCategory(Category category) {
        try {
            if (categoryDAO.delete(category.getId())) {
                categories.remove(category);
            }
        } catch (Exception ex) {
            showError("Failed to delete category: " + ex.getMessage());
        }
    }

    private void confirmDeleteCategory(Category category) {
        javafx.scene.control.Dialog<javafx.scene.control.ButtonType> dialog = new javafx.scene.control.Dialog<>();
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.setTitle("Delete Category");
        dialog.getDialogPane().getStyleClass().add("dialog-pane");
        dialog.getDialogPane().getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        dialog.getDialogPane().getButtonTypes().addAll(javafx.scene.control.ButtonType.OK, javafx.scene.control.ButtonType.CANCEL);

        javafx.scene.layout.VBox content = new javafx.scene.layout.VBox();
        content.setSpacing(0);
        content.setPadding(new Insets(0));

        javafx.scene.layout.HBox customHeader = new javafx.scene.layout.HBox();
        javafx.scene.control.Label titleLabel = new javafx.scene.control.Label("Delete Category");
        titleLabel.getStyleClass().add("dialog-title");
        customHeader.getChildren().add(titleLabel);
        customHeader.setAlignment(Pos.CENTER_LEFT);
        customHeader.setSpacing(12);
        customHeader.setPadding(new Insets(16, 20, 16, 20));
        customHeader.setStyle("-fx-background-color: #161628; -fx-border-color: transparent transparent #2a2a4a transparent; -fx-background-radius: 0; -fx-border-radius: 0;");
        HBox.setHgrow(titleLabel, Priority.ALWAYS);

        javafx.scene.layout.VBox body = new javafx.scene.layout.VBox(16);
        body.setPadding(new Insets(24));
        body.setStyle("-fx-background-color: #1a1a2e;");

        javafx.scene.control.Label message = new javafx.scene.control.Label("Are you sure you want to delete \"" + category.getName() + "\"?");
        message.setStyle("-fx-font-size: 14px; -fx-text-fill: #c0c0d0; -fx-wrap-text: true;");

        javafx.scene.control.Label warning = new javafx.scene.control.Label("This action cannot be undone.");
        warning.setStyle("-fx-font-size: 13px; -fx-text-fill: #e53935; -fx-font-weight: 500;");

        body.getChildren().addAll(message, warning);
        content.getChildren().addAll(customHeader, body);
        dialog.getDialogPane().setContent(content);

        dialog.getDialogPane().sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.setFill(null);
            }
        });

        javafx.scene.control.Button okButton = (javafx.scene.control.Button) dialog.getDialogPane().lookupButton(javafx.scene.control.ButtonType.OK);
        okButton.getStyleClass().addAll("btn", "btn-danger");
        okButton.setText("Delete");

        javafx.scene.control.Button cancelButton = (javafx.scene.control.Button) dialog.getDialogPane().lookupButton(javafx.scene.control.ButtonType.CANCEL);
        cancelButton.getStyleClass().addAll("btn", "btn-secondary");
        cancelButton.setText("Cancel");

        dialog.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                deleteCategory(category);
            }
        });
    }

    private void openCreateExerciseDialog() {
        ExerciseFormDialog dialog = new ExerciseFormDialog(categories);
        dialog.showAndWait().ifPresent(exercise -> {
            try {
                long id = exerciseDAO.insert(exercise);
                exercise.setId(id);
                exercises.add(0, exercise);
            } catch (Exception e) {
                showError("Failed to create exercise: " + e.getMessage());
            }
        });
    }

    private void openEditExerciseDialog(Exercise exercise) {
        ExerciseFormDialog dialog = new ExerciseFormDialog(exercise, categories);
        dialog.showAndWait().ifPresent(updatedExercise -> {
            try {
                updatedExercise.setId(exercise.getId());
                if (exerciseDAO.update(updatedExercise)) {
                    int idx = exercises.indexOf(exercise);
                    if (idx >= 0) {
                        exercises.set(idx, updatedExercise);
                    }
                }
            } catch (Exception e) {
                showError("Failed to update exercise: " + e.getMessage());
            }
        });
    }

    private void deleteExercise(Exercise exercise) {
        try {
            if (exerciseDAO.delete(exercise.getId())) {
                exercises.remove(exercise);
            }
        } catch (Exception ex) {
            showError("Failed to delete exercise: " + ex.getMessage());
        }
    }

    private void confirmDeleteExercise(Exercise exercise) {
        javafx.scene.control.Dialog<javafx.scene.control.ButtonType> dialog = new javafx.scene.control.Dialog<>();
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.setTitle("Delete Exercise");
        dialog.getDialogPane().getStyleClass().add("dialog-pane");
        dialog.getDialogPane().getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        dialog.getDialogPane().getButtonTypes().addAll(javafx.scene.control.ButtonType.OK, javafx.scene.control.ButtonType.CANCEL);

        javafx.scene.layout.VBox content = new javafx.scene.layout.VBox();
        content.setSpacing(0);
        content.setPadding(new Insets(0));

        javafx.scene.layout.HBox customHeader = new javafx.scene.layout.HBox();
        javafx.scene.control.Label titleLabel = new javafx.scene.control.Label("Delete Exercise");
        titleLabel.getStyleClass().add("dialog-title");
        customHeader.getChildren().add(titleLabel);
        customHeader.setAlignment(Pos.CENTER_LEFT);
        customHeader.setSpacing(12);
        customHeader.setPadding(new Insets(16, 20, 16, 20));
        customHeader.setStyle("-fx-background-color: #161628; -fx-border-color: transparent transparent #2a2a4a transparent; -fx-background-radius: 0; -fx-border-radius: 0;");
        HBox.setHgrow(titleLabel, Priority.ALWAYS);

        javafx.scene.layout.VBox body = new javafx.scene.layout.VBox(16);
        body.setPadding(new Insets(24));
        body.setStyle("-fx-background-color: #1a1a2e;");

        javafx.scene.control.Label message = new javafx.scene.control.Label("Are you sure you want to delete \"" + exercise.getName() + "\"?");
        message.setStyle("-fx-font-size: 14px; -fx-text-fill: #c0c0d0; -fx-wrap-text: true;");

        javafx.scene.control.Label warning = new javafx.scene.control.Label("This action cannot be undone.");
        warning.setStyle("-fx-font-size: 13px; -fx-text-fill: #e53935; -fx-font-weight: 500;");

        body.getChildren().addAll(message, warning);
        content.getChildren().addAll(customHeader, body);
        dialog.getDialogPane().setContent(content);

        dialog.getDialogPane().sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.setFill(null);
            }
        });

        javafx.scene.control.Button okButton = (javafx.scene.control.Button) dialog.getDialogPane().lookupButton(javafx.scene.control.ButtonType.OK);
        okButton.getStyleClass().addAll("btn", "btn-danger");
        okButton.setText("Delete");

        javafx.scene.control.Button cancelButton = (javafx.scene.control.Button) dialog.getDialogPane().lookupButton(javafx.scene.control.ButtonType.CANCEL);
        cancelButton.getStyleClass().addAll("btn", "btn-secondary");
        cancelButton.setText("Cancel");

        dialog.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                deleteExercise(exercise);
            }
        });
    }

    private void openCreateRoutineDialog() {
        RoutineFormDialog dialog = new RoutineFormDialog();
        dialog.showAndWait().ifPresent(routine -> {
            try {
                routine.setUserId(currentUser.getId());
                long id = routineDAO.insert(routine);
                routine.setId(id);
                routines.add(0, routine);
            } catch (Exception e) {
                showError("Failed to create routine: " + e.getMessage());
            }
        });
    }

    private void openEditRoutineDialog(Routine routine) {
        RoutineFormDialog dialog = new RoutineFormDialog(routine);
        dialog.showAndWait().ifPresent(updatedRoutine -> {
            try {
                updatedRoutine.setId(routine.getId());
                updatedRoutine.setUserId(routine.getUserId());
                if (routineDAO.update(updatedRoutine)) {
                    int idx = routines.indexOf(routine);
                    if (idx >= 0) {
                        routines.set(idx, updatedRoutine);
                    }
                }
            } catch (Exception e) {
                showError("Failed to update routine: " + e.getMessage());
            }
        });
    }

    private void deleteRoutine(Routine routine) {
        try {
            // Also delete associated routine exercises
            routineExerciseDAO.deleteByRoutine(routine.getId());
            if (routineDAO.delete(routine.getId(), currentUser.getId())) {
                routines.remove(routine);
            }
        } catch (Exception ex) {
            showError("Failed to delete routine: " + ex.getMessage());
        }
    }

    private void confirmDeleteRoutine(Routine routine) {
        javafx.scene.control.Dialog<javafx.scene.control.ButtonType> dialog = new javafx.scene.control.Dialog<>();
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.setTitle("Delete Routine");
        dialog.getDialogPane().getStyleClass().add("dialog-pane");
        dialog.getDialogPane().getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        dialog.getDialogPane().getButtonTypes().addAll(javafx.scene.control.ButtonType.OK, javafx.scene.control.ButtonType.CANCEL);

        javafx.scene.layout.VBox content = new javafx.scene.layout.VBox();
        content.setSpacing(0);
        content.setPadding(new Insets(0));

        javafx.scene.layout.HBox customHeader = new javafx.scene.layout.HBox();
        javafx.scene.control.Label titleLabel = new javafx.scene.control.Label("Delete Routine");
        titleLabel.getStyleClass().add("dialog-title");
        customHeader.getChildren().add(titleLabel);
        customHeader.setAlignment(Pos.CENTER_LEFT);
        customHeader.setSpacing(12);
        customHeader.setPadding(new Insets(16, 20, 16, 20));
        customHeader.setStyle("-fx-background-color: #161628; -fx-border-color: transparent transparent #2a2a4a transparent; -fx-background-radius: 0; -fx-border-radius: 0;");
        HBox.setHgrow(titleLabel, Priority.ALWAYS);

        javafx.scene.layout.VBox body = new javafx.scene.layout.VBox(16);
        body.setPadding(new Insets(24));
        body.setStyle("-fx-background-color: #1a1a2e;");

        javafx.scene.control.Label message = new javafx.scene.control.Label("Are you sure you want to delete \"" + routine.getName() + "\"?");
        message.setStyle("-fx-font-size: 14px; -fx-text-fill: #c0c0d0; -fx-wrap-text: true;");

        javafx.scene.control.Label warning = new javafx.scene.control.Label("This action cannot be undone. All assigned exercises will also be removed.");
        warning.setStyle("-fx-font-size: 13px; -fx-text-fill: #e53935; -fx-font-weight: 500;");

        body.getChildren().addAll(message, warning);
        content.getChildren().addAll(customHeader, body);
        dialog.getDialogPane().setContent(content);

        dialog.getDialogPane().sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.setFill(null);
            }
        });

        javafx.scene.control.Button okButton = (javafx.scene.control.Button) dialog.getDialogPane().lookupButton(javafx.scene.control.ButtonType.OK);
        okButton.getStyleClass().addAll("btn", "btn-danger");
        okButton.setText("Delete");

        javafx.scene.control.Button cancelButton = (javafx.scene.control.Button) dialog.getDialogPane().lookupButton(javafx.scene.control.ButtonType.CANCEL);
        cancelButton.getStyleClass().addAll("btn", "btn-secondary");
        cancelButton.setText("Cancel");

        dialog.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                deleteRoutine(routine);
            }
        });
    }

    private void openAssignExercisesDialog(Routine routine) {
        RoutineExerciseAssignmentDialog dialog = new RoutineExerciseAssignmentDialog(routine, exercises, routineExerciseDAO);
        dialog.showAndWait();
        // Refresh routines to update any changes
        loadRoutines();
    }

    // Session methods
    private void openCreateSessionDialog() {
        try {
            List<Routine> allRoutines;
            if (currentUser != null) {
                allRoutines = routineDAO.fetchByUser(currentUser.getId());
            } else {
                allRoutines = routineDAO.fetchAll();
            }
            SessionFormDialog dialog = new SessionFormDialog(allRoutines);
            dialog.showAndWait().ifPresent(session -> {
                try {
                    long id = sessionDAO.insert(session);
                    session.setId(id);
                    // Refresh session list for the current routine
                    loadSessionsForRoutine(session.getRoutineId());
                } catch (Exception e) {
                    showError("Failed to create session: " + e.getMessage());
                }
            });
        } catch (Exception e) {
            showError("Failed to load routines: " + e.getMessage());
        }
    }

    private void openEditSessionDialog(Session session) {
        try {
            List<Routine> allRoutines;
            if (currentUser != null) {
                allRoutines = routineDAO.fetchByUser(currentUser.getId());
            } else {
                allRoutines = routineDAO.fetchAll();
            }
            SessionFormDialog dialog = new SessionFormDialog(session, allRoutines);
            dialog.showAndWait().ifPresent(updatedSession -> {
                try {
                    updatedSession.setId(session.getId());
                    if (sessionDAO.update(updatedSession)) {
                        // Update in session list
                        loadSessionsForRoutine(updatedSession.getRoutineId());
                    }
                } catch (Exception e) {
                    showError("Failed to update session: " + e.getMessage());
                }
            });
        } catch (Exception e) {
            showError("Failed to load routines: " + e.getMessage());
        }
    }

    private void toggleSessionDone(Session session) {
        try {
            boolean newDone = !session.getSessionDayDone();
            if (sessionDAO.updateDayDone(session.getId(), newDone)) {
                session.setSessionDayDone(newDone);
                sessionListView.refresh();
            }
        } catch (Exception ex) {
            showError("Failed to update session: " + ex.getMessage());
        }
    }

    private void deleteSession(Session session) {
        try {
            sessionExerciseDAO.deleteBySession(session.getId());
            if (sessionDAO.delete(session.getId())) {
                loadSessionsForRoutine(session.getRoutineId());
            }
        } catch (Exception ex) {
            showError("Failed to delete session: " + ex.getMessage());
        }
    }

    private void confirmDeleteSession(Session session) {
        javafx.scene.control.Dialog<javafx.scene.control.ButtonType> dialog = new javafx.scene.control.Dialog<>();
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.setTitle("Delete Session");
        dialog.getDialogPane().getStyleClass().add("dialog-pane");
        dialog.getDialogPane().getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        dialog.getDialogPane().getButtonTypes().addAll(javafx.scene.control.ButtonType.OK, javafx.scene.control.ButtonType.CANCEL);

        javafx.scene.layout.VBox content = new javafx.scene.layout.VBox();
        content.setSpacing(0);
        content.setPadding(new Insets(0));

        javafx.scene.layout.HBox customHeader = new javafx.scene.layout.HBox();
        javafx.scene.control.Label titleLabel = new javafx.scene.control.Label("Delete Session");
        titleLabel.getStyleClass().add("dialog-title");
        customHeader.getChildren().add(titleLabel);
        customHeader.setAlignment(Pos.CENTER_LEFT);
        customHeader.setSpacing(12);
        customHeader.setPadding(new Insets(16, 20, 16, 20));
        customHeader.setStyle("-fx-background-color: #161628; -fx-border-color: transparent transparent #2a2a4a transparent; -fx-background-radius: 0; -fx-border-radius: 0;");
        HBox.setHgrow(titleLabel, Priority.ALWAYS);

        javafx.scene.layout.VBox body = new javafx.scene.layout.VBox(16);
        body.setPadding(new Insets(24));
        body.setStyle("-fx-background-color: #1a1a2e;");

        javafx.scene.control.Label message = new javafx.scene.control.Label("Are you sure you want to delete the session for " + session.getDate() + "?");
        message.setStyle("-fx-font-size: 14px; -fx-text-fill: #c0c0d0; -fx-wrap-text: true;");

        javafx.scene.control.Label warning = new javafx.scene.control.Label("This action cannot be undone. All session exercises will also be removed.");
        warning.setStyle("-fx-font-size: 13px; -fx-text-fill: #e53935; -fx-font-weight: 500;");

        body.getChildren().addAll(message, warning);
        content.getChildren().addAll(customHeader, body);
        dialog.getDialogPane().setContent(content);

        dialog.getDialogPane().sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.setFill(null);
            }
        });

        javafx.scene.control.Button okButton = (javafx.scene.control.Button) dialog.getDialogPane().lookupButton(javafx.scene.control.ButtonType.OK);
        okButton.getStyleClass().addAll("btn", "btn-danger");
        okButton.setText("Delete");

        javafx.scene.control.Button cancelButton = (javafx.scene.control.Button) dialog.getDialogPane().lookupButton(javafx.scene.control.ButtonType.CANCEL);
        cancelButton.getStyleClass().addAll("btn", "btn-secondary");
        cancelButton.setText("Cancel");

        dialog.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                deleteSession(session);
            }
        });
    }

    private void openAssignExercisesDialog(Session session) {
        // Find the routine for this session
        Routine routine = routines.stream().filter(r -> r.getId().equals(session.getRoutineId())).findFirst().orElse(null);
        if (routine != null) {
            SessionExerciseAssignmentDialog dialog = new SessionExerciseAssignmentDialog(session, routine, exercises, sessionDAO, sessionExerciseDAO, routineExerciseDAO);
            dialog.showAndWait();
        }
    }

    private void loadSessionsForRoutine(long routineId) {
        try {
            sessionListView.setItems(FXCollections.observableArrayList(sessionDAO.fetchByRoutine(routineId)));
        } catch (Exception e) {
            showError("Failed to load sessions: " + e.getMessage());
        }
    }

    private void openSessionForDate(LocalDate date) {
        if (currentUser == null) {
            showError("Please select a user first");
            return;
        }

        // Find a routine that has this date's day of week AND date is within routine's since/until range
        int dayOfWeek = date.getDayOfWeek().getValue() - 1; // 0=Mon, 6=Sun
        Routine todaysRoutine = routines.stream()
            .filter(r -> r.hasDay(com.routineexe.model.DayOfWeek.fromIndex(dayOfWeek)))
            .filter(r -> r.getSince() != null && !date.isBefore(r.getSince()))
            .filter(r -> r.getUntil() == null || !date.isAfter(r.getUntil()))
            .findFirst()
            .orElse(null);

        if (todaysRoutine == null) {
            showError("No active routine for " + date.getDayOfWeek() + " on " + date);
            return;
        }

        // Check if session already exists for this date
        try {
            var existingSession = sessionDAO.fetchByRoutineAndDate(todaysRoutine.getId(), date);
            Session session;
            if (existingSession.isPresent()) {
                session = existingSession.get();
                session.setDayOfWeekIndex(dayOfWeek);
                sessionDAO.update(session);
            } else {
                // Create new session
                session = new Session();
                session.setRoutineId(todaysRoutine.getId());
                session.setDate(date);
                session.setDayOfWeekIndex(dayOfWeek);
                session.setSessionDayDone(false);
                long id = sessionDAO.insert(session);
                session.setId(id);
            }

            // Open session exercise dialog
            SessionExerciseAssignmentDialog dialog = new SessionExerciseAssignmentDialog(session, todaysRoutine, exercises, sessionDAO, sessionExerciseDAO, routineExerciseDAO);
            dialog.showAndWait();
            
            // Refresh calendar
            sessionView.setCurrentRoutine(todaysRoutine);
            loadSessionsForRoutine(todaysRoutine.getId());
            
        } catch (Exception e) {
            showError("Failed to open session: " + e.getMessage());
        }
    }

    private void showError(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.initStyle(StageStyle.UTILITY);
            alert.showAndWait();
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}