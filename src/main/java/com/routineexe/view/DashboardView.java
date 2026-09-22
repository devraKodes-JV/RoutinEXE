package com.routineexe.view;

import com.routineexe.model.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class DashboardView extends VBox {

    public DashboardView(User user, Runnable onBack, Runnable onCategories) {
        getStyleClass().add("root");
        setSpacing(0);

        // Top bar with back button and user info
        HBox topBar = createTopBar(user, onBack);

        // Main content area with sidebar
        HBox contentArea = new HBox();
        contentArea.setSpacing(0);
        HBox.setHgrow(contentArea, Priority.ALWAYS);
        VBox.setVgrow(contentArea, Priority.ALWAYS);

        VBox sidebar = createSidebar(user, onCategories);
        VBox mainContent = createMainContent(user);

        contentArea.getChildren().addAll(sidebar, mainContent);
        HBox.setHgrow(mainContent, Priority.ALWAYS);

        getChildren().addAll(topBar, contentArea);
    }

    private HBox createTopBar(User user, Runnable onBack) {
        Button backBtn = new Button();
        Label backIcon = new Label("\uf071");
        backIcon.getStyleClass().addAll("bi");
        backIcon.setStyle("-fx-font-size: 14px;");
        backBtn.setGraphic(backIcon);
        backBtn.getStyleClass().addAll("btn", "btn-primary", "btn-icon");
        backBtn.setOnAction(e -> onBack.run());
        backBtn.setTooltip(new javafx.scene.control.Tooltip("Back to Users"));

        Label userName = new Label("Dashboard - " + user.getUsername());
        userName.getStyleClass().add("dashboard-title");

        HBox topBar = new HBox(16, backBtn, userName);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(16, 24, 16, 24));
        topBar.setStyle("-fx-background-color: #1a1a2e; -fx-border-color: transparent transparent #2a2a4a transparent;");

        return topBar;
    }

    private VBox createSidebar(User user, Runnable onCategories) {
        VBox sidebar = new VBox();
        sidebar.setStyle("-fx-background-color: #161628; -fx-border-color: transparent #2a2a4a transparent transparent; -fx-min-width: 240px; -fx-max-width: 240px;");
        sidebar.setPadding(new Insets(24, 0, 24, 0));
        sidebar.setSpacing(8);

        Label sidebarTitle = new Label("MENU");
        sidebarTitle.getStyleClass().add("sidebar-title");
        sidebarTitle.setPadding(new Insets(0, 24, 16, 24));

        VBox navItems = new VBox(4);
        navItems.setPadding(new Insets(0, 16, 0, 16));

        String[] navLabels = {"Overview", "Categories"};
        String[] navIcons = {"\uf430", "\uf3af"};

        for (int i = 0; i < navLabels.length; i++) {
            String icon = navIcons[i];
            String label = navLabels[i];
            Runnable action = (label.equals("Categories")) ? onCategories : null;
            Button navBtn = createNavButton(icon, label, i == 0, action);
            navItems.getChildren().add(navBtn);
        }

        Separator separator = new Separator();
        separator.setStyle("-fx-background-color: #2a2a4a;");
        separator.setPadding(new Insets(16, 24, 16, 24));

        Label userInfoTitle = new Label("USER INFO");
        userInfoTitle.getStyleClass().add("sidebar-title");
        userInfoTitle.setPadding(new Insets(0, 24, 16, 24));

        VBox userInfo = new VBox(8);
        userInfo.setPadding(new Insets(0, 24, 0, 24));

        userInfo.getChildren().addAll(
            createInfoRow("\uf4da", "Username", user.getUsername()),
            createInfoRow("\uf1ee", "Age", user.getAge().map(String::valueOf).orElse("Not set")),
            createInfoRow("\uf47c", "Height", user.getHeight().map(h -> String.format("%.1f cm", h)).orElse("Not set")),
            createInfoRow("\uf47c", "Weight", user.getWeight().map(w -> String.format("%.1f kg", w)).orElse("Not set"))
        );

        sidebar.getChildren().addAll(sidebarTitle, navItems, separator, userInfoTitle, userInfo);
        return sidebar;
    }

    private Button createNavButton(String iconUnicode, String label, boolean active, Runnable action) {
        Label icon = new Label(iconUnicode);
        icon.getStyleClass().add("bi");
        icon.setStyle("-fx-font-size: 16px; -fx-text-fill: " + (active ? "#1e88e5" : "#808090") + "; -fx-min-width: 24px;");

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
            btn.setOnAction(e -> action.run());
        }
        btn.setOnMouseEntered(e -> {
            if (!active) btn.setStyle("-fx-background-color: #1e3a5f; -fx-background-radius: 6px;");
        });
        btn.setOnMouseExited(e -> {
            if (!active) btn.setStyle("-fx-background-color: transparent; -fx-background-radius: 6px;");
        });

        return btn;
    }

    private HBox createInfoRow(String iconUnicode, String label, String value) {
        Label icon = new Label(iconUnicode);
        icon.getStyleClass().add("bi");
        icon.setStyle("-fx-font-size: 14px; -fx-text-fill: #808090; -fx-min-width: 24px;");

        Label lbl = new Label(label);
        lbl.setStyle("-fx-font-size: 13px; -fx-text-fill: #808090;");

        Label val = new Label(value);
        val.setStyle("-fx-font-size: 13px; -fx-text-fill: #e8e8f0; -fx-font-weight: 500;");

        HBox row = new HBox(12, icon, lbl, val);
        row.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(lbl, Priority.ALWAYS);
        return row;
    }

    private VBox createMainContent(User user) {
        VBox mainContent = new VBox(24);
        mainContent.setPadding(new Insets(24));
        mainContent.setStyle("-fx-background-color: #0f0f1a;");

        Label welcomeTitle = new Label("Welcome back, " + user.getUsername() + "!");
        welcomeTitle.setStyle("-fx-font-size: 28px; -fx-font-weight: 600; -fx-text-fill: #e8e8f0;");

        Label subtitle = new Label("Here's an overview of your fitness journey");
        subtitle.setStyle("-fx-font-size: 14px; -fx-text-fill: #808090;");

        // Stats cards
        HBox statsRow = new HBox(16);
        statsRow.setAlignment(Pos.CENTER_LEFT);
        statsRow.getChildren().addAll(
            createStatCard("\uf3e7", "Total Routines", "12", "#1e88e5"),
            createStatCard("\uf408", "Active Days", "8", "#43a047"),
            createStatCard("\uf27b", "Hours This Week", "5.5", "#fb8c00"),
            createStatCard("\uf585", "Achievements", "3", "#e53935")
        );

        // Recent activity placeholder
        Label activityTitle = new Label("Recent Activity");
        activityTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: 600; -fx-text-fill: #e8e8f0; -fx-padding: 16 0 0 0;");

        VBox activityList = new VBox(12);
        activityList.setPadding(new Insets(16, 0, 0, 0));

        String[] activities = {
            "Completed \"Upper Body Strength\" routine",
            "Logged weight: 75.5 kg",
            "Completed \"Cardio Blast\" routine",
            "Achieved \"Week Warrior\" badge"
        };
        String[] activityTimes = {"2 hours ago", "Yesterday", "2 days ago", "3 days ago"};
        String[] activityIcons = {"\uf26e", "\uf47c", "\uf26e", "\uf585"};
        String[] activityColors = {"#43a047", "#1e88e5", "#43a047", "#fb8c00"};

        for (int i = 0; i < activities.length; i++) {
            activityList.getChildren().add(createActivityItem(activityIcons[i], activities[i], activityTimes[i], activityColors[i]));
        }

        mainContent.getChildren().addAll(welcomeTitle, subtitle, statsRow, activityTitle, activityList);
        return mainContent;
    }

    private VBox createStatCard(String iconUnicode, String title, String value, String color) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: #1a1a2e; -fx-background-radius: 12px; -fx-border-color: #2a2a4a; -fx-border-radius: 12px; -fx-border-width: 1px;");
        card.setPrefWidth(180);
        HBox.setHgrow(card, Priority.ALWAYS);

        HBox header = new HBox();
        Label icon = new Label(iconUnicode);
        icon.getStyleClass().add("bi");
        icon.setStyle("-fx-font-size: 20px; -fx-text-fill: " + color + ";");
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
        icon.getStyleClass().add("bi");
        icon.setStyle("-fx-font-size: 16px; -fx-text-fill: " + color + "; -fx-min-width: 32px; -fx-min-height: 32px; -fx-alignment: center; -fx-background-color: " + color + "20; -fx-background-radius: 8px;");

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