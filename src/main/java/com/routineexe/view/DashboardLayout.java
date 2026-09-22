package com.routineexe.view;

import com.routineexe.model.Category;
import com.routineexe.model.Exercise;
import com.routineexe.model.Routine;
import com.routineexe.model.User;
import com.routineexe.database.RoutineDAO;
import com.routineexe.database.SessionDAO;
import com.routineexe.database.SessionExerciseDAO;
import com.routineexe.view.categories.CategoryListView;
import com.routineexe.view.categories.CategoryToolbar;
import com.routineexe.view.categories.CategoryView;
import com.routineexe.view.exercises.ExerciseListView;
import com.routineexe.view.exercises.ExerciseToolbar;
import com.routineexe.view.exercises.ExerciseView;
import com.routineexe.view.routines.RoutineListView;
import com.routineexe.view.routines.RoutineToolbar;
import com.routineexe.view.routines.RoutineView;
import com.routineexe.view.sessions.SessionListView;
import com.routineexe.view.sessions.SessionToolbar;
import com.routineexe.view.sessions.SessionView;
import com.routineexe.util.FontAwesomeIcons;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.Collectors;

public class DashboardLayout extends VBox {

    private final User user;
    private final ObservableList<Category> categories;
    private final ObservableList<Exercise> exercises;
    private final ObservableList<Routine> routines;
    private final CategoryToolbar categoryToolbar;
    private final ExerciseToolbar exerciseToolbar;
    private final RoutineToolbar routineToolbar;
    private final CategoryListView categoryListView;
    private final ExerciseListView exerciseListView;
    private final RoutineListView routineListView;
    private final SessionListView sessionListView;
    private final SessionToolbar sessionToolbar;
    private final SessionView sessionView;
    private final SessionDAO sessionDAO;
    private final SessionExerciseDAO sessionExerciseDAO;
    private final RoutineDAO routineDAO;

    public DashboardLayout(User user, ObservableList<Category> categories, ObservableList<Exercise> exercises, ObservableList<Routine> routines,
            CategoryToolbar categoryToolbar, ExerciseToolbar exerciseToolbar, RoutineToolbar routineToolbar,
            CategoryListView categoryListView, ExerciseListView exerciseListView, RoutineListView routineListView,
            SessionListView sessionListView, SessionToolbar sessionToolbar, SessionView sessionView,
            SessionDAO sessionDAO, SessionExerciseDAO sessionExerciseDAO, RoutineDAO routineDAO,
            Runnable onBack, Runnable onCategories, Runnable onExercises, Runnable onRoutines, Runnable onSessions, Runnable onLogout) {
        this.user = user;
        this.categories = categories;
        this.exercises = exercises;
        this.routines = routines;
        this.categoryToolbar = categoryToolbar;
        this.exerciseToolbar = exerciseToolbar;
        this.routineToolbar = routineToolbar;
        this.categoryListView = categoryListView;
        this.exerciseListView = exerciseListView;
        this.routineListView = routineListView;
        this.sessionListView = sessionListView;
        this.sessionToolbar = sessionToolbar;
        this.sessionView = sessionView;
        this.sessionDAO = sessionDAO;
        this.sessionExerciseDAO = sessionExerciseDAO;
        this.routineDAO = routineDAO;
        getStyleClass().add("root");
        setSpacing(0);
        VBox.setVgrow(this, Priority.ALWAYS);

        showOverview();
    }

    public void showOverview() {
        getChildren().clear();
        
        // Overview content with scroll
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.getStyleClass().add("content-scroll-pane");
        
        VBox content = createOverviewContent(user, sessionDAO, sessionExerciseDAO);
        scrollPane.setContent(content);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        getChildren().add(scrollPane);
    }

public void showCategories() {
        getChildren().clear();

        // Categories view with header and table - full height, using real data
        CategoryView categoryView = new CategoryView(this.categories, this.categoryListView, this.categoryToolbar, true);

        VBox categoriesWrapper = new VBox();
        categoriesWrapper.setStyle("-fx-background-color: #0f0f1a;");
        VBox.setVgrow(categoriesWrapper, Priority.ALWAYS);
        categoriesWrapper.getChildren().add(categoryView);
        VBox.setVgrow(categoryView, Priority.ALWAYS);

        getChildren().add(categoriesWrapper);
    }

    public void showExercises() {
        getChildren().clear();

        // Exercises view with header and table - full height, using real data
        ExerciseView exerciseView = new ExerciseView(this.exercises, this.exerciseListView, this.exerciseToolbar, true);

        VBox exercisesWrapper = new VBox();
        exercisesWrapper.setStyle("-fx-background-color: #0f0f1a;");
        VBox.setVgrow(exercisesWrapper, Priority.ALWAYS);
        exercisesWrapper.getChildren().add(exerciseView);
        VBox.setVgrow(exerciseView, Priority.ALWAYS);

        getChildren().add(exercisesWrapper);
    }

    public void showRoutines() {
        getChildren().clear();

        try {
            if (routineDAO != null) {
                routines.setAll(routineDAO.fetchByUser(user.getId()));
            }
        } catch (Exception e) {
            // Use existing routines if refresh fails
        }

        // Routines view with header and table - full height, using real data
        RoutineView routineView = new RoutineView(this.routines, this.routineListView, this.routineToolbar, true);

        VBox routinesWrapper = new VBox();
        routinesWrapper.setStyle("-fx-background-color: #0f0f1a;");
        VBox.setVgrow(routinesWrapper, Priority.ALWAYS);
        routinesWrapper.getChildren().add(routineView);
        VBox.setVgrow(routineView, Priority.ALWAYS);

        getChildren().add(routinesWrapper);
    }

    public void showSessions() {
        getChildren().clear();

        // Set current routine to first routine for calendar display
        if (!routines.isEmpty()) {
            sessionView.setCurrentRoutine(routines.get(0));
        }

        VBox sessionsWrapper = new VBox();
        sessionsWrapper.setStyle("-fx-background-color: #0f0f1a;");
        VBox.setVgrow(sessionsWrapper, Priority.ALWAYS);
        sessionsWrapper.getChildren().add(sessionView);
        VBox.setVgrow(sessionView, Priority.ALWAYS);

        getChildren().add(sessionsWrapper);
    }

    private VBox createOverviewContent(User user, SessionDAO sessionDAO, SessionExerciseDAO sessionExerciseDAO) {
        VBox content = new VBox(24);
        content.setPadding(new Insets(24));
        content.setStyle("-fx-background-color: #0f0f1a;");

        Label welcomeTitle = new Label("Welcome back, " + user.getUsername() + "!");
        welcomeTitle.setStyle("-fx-font-size: 28px; -fx-font-weight: 600; -fx-text-fill: #e8e8f0;");

        Label subtitle = new Label("Here's an overview of your fitness journey");
        subtitle.setStyle("-fx-font-size: 14px; -fx-text-fill: #808090;");

        // Compute real stats
        int totalRoutines = routines.size();
        int activeDays = 0;
        int sessionsDone = 0;
        try {
            List<com.routineexe.model.Session> userSessions = sessionDAO.fetchByUser(user.getId());
            activeDays = (int) userSessions.stream()
                .mapToInt(s -> s.getSessionDayDone() != null && s.getSessionDayDone() ? 1 : 0)
                .sum();
            sessionsDone = (int) userSessions.stream()
                .filter(s -> s.getSessionDayDone() != null && s.getSessionDayDone())
                .count();
        } catch (Exception e) {
            // Use defaults if error
        }
        String activeDaysStr = String.valueOf(activeDays);
        String sessionsDoneStr = String.valueOf(sessionsDone);
        String achievementsStr = String.valueOf(Math.max(totalRoutines, sessionsDone));

        // Stats cards
        HBox statsRow = new HBox(16);
        statsRow.setAlignment(Pos.CENTER_LEFT);
        statsRow.getChildren().addAll(
            createStatCard(FontAwesomeIcons.LIST_CHECK, "Total Routines", String.valueOf(totalRoutines), "#1e88e5"),
            createStatCard(FontAwesomeIcons.CALENDAR_CHECK, "Active Days", activeDaysStr, "#43a047"),
            createStatCard(FontAwesomeIcons.CLOCK, "Sessions", sessionsDoneStr, "#fb8c00"),
            createStatCard(FontAwesomeIcons.TROPHY, "Achievements", achievementsStr, "#e53935")
        );

        // Recent activity from sessions
        Label activityTitle = new Label("Recent Activity");
        activityTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: 600; -fx-text-fill: #e8e8f0; -fx-padding: 16 0 0 0;");

        VBox activityList = new VBox(12);
        activityList.setPadding(new Insets(16, 0, 0, 0));

        try {
            List<com.routineexe.model.Session> recentSessions = sessionDAO.fetchByUser(user.getId()).stream()
                .limit(4)
                .collect(Collectors.toList());
            for (int i = 0; i < recentSessions.size(); i++) {
                com.routineexe.model.Session s = recentSessions.get(i);
                String dateStr = s.getDate() != null ? s.getDate().toString() : "Unknown";
                String icon = FontAwesomeIcons.DUMBBELL_ALT;
                String color = "#43a047";
                String desc = (s.getSessionDayDone() != null && s.getSessionDayDone() ? "Completed " : "Started ") + "\"" + dateStr + "\" session";
                String timeLabel;
                if (i == 0) timeLabel = "Today";
                else if (i == 1) timeLabel = "Yesterday";
                else if (i == 2) timeLabel = "2 days ago";
                else timeLabel = "3 days ago";
                activityList.getChildren().add(createActivityItem(icon, desc, timeLabel, color));
            }
        } catch (Exception e) {
            // Fallback to static items
            String[] activities = {
                "Completed workout",
                "Logged exercise",
                "Started new routine",
                "Achieved milestone"
            };
            String[] times = {"Today", "Yesterday", "2 days ago", "3 days ago"};
            String[] colors = {"#43a047", "#1e88e5", "#43a047", "#fb8c00"};
            for (int i = 0; i < 4; i++) {
                activityList.getChildren().add(createActivityItem(FontAwesomeIcons.DUMBBELL_ALT, activities[i], times[i], colors[i]));
            }
        }

        content.getChildren().addAll(welcomeTitle, subtitle, statsRow, activityTitle, activityList);
        return content;
    }

    private VBox createStatCard(String iconUnicode, String title, String value, String color) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: #1a1a2e; -fx-background-radius: 12px; -fx-border-color: #2a2a4a; -fx-border-radius: 12px; -fx-border-width: 1px;");
        card.setPrefWidth(180);
        HBox.setHgrow(card, Priority.ALWAYS);

        HBox header = new HBox();
        Label icon = new Label(iconUnicode);
        icon.setStyle("-fx-font-family: 'Font Awesome 6 Free'; -fx-font-weight: 900; -fx-font-size: 20px; -fx-text-fill: " + color + ";");
        header.getChildren().add(icon);
        header.setAlignment(Pos.CENTER_LEFT);

        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: 700; -fx-text-fill: #e8e8f0;");

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #808090; -fx-font-weight: 500;");

        card.getChildren().addAll(header, valueLabel, titleLabel);
        return card;
    }

    private HBox createActivityItem(String iconUnicode, String description, String time, String color) {
        Label icon = new Label(iconUnicode);
        icon.setStyle("-fx-font-family: 'Font Awesome 6 Free'; -fx-font-weight: 900; -fx-font-size: 16px; -fx-text-fill: " + color + "; -fx-min-width: 32px; -fx-min-height: 32px; -fx-alignment: center; -fx-background-color: " + color + "20; -fx-background-radius: 8px;");

        VBox textContent = new VBox(2);
        Label desc = new Label(description);
        desc.setStyle("-fx-font-size: 14px; -fx-text-fill: #e8e8f0; -fx-font-weight: 500;");
        Label timeLabel = new Label(time);
        timeLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #808090;");
        textContent.getChildren().addAll(desc, timeLabel);

        HBox item = new HBox(12, icon, textContent);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(12, 16, 12, 16));
        item.setStyle("-fx-background-color: #1a1a2e; -fx-background-radius: 8px; -fx-border-color: #2a2a4a; -fx-border-radius: 8px; -fx-border-width: 1px;");

        return item;
    }
}