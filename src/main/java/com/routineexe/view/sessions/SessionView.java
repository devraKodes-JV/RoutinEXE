package com.routineexe.view.sessions;

import com.routineexe.database.SessionDAO;
import com.routineexe.database.SessionExerciseDAO;
import com.routineexe.model.Routine;
import com.routineexe.model.Session;
import com.routineexe.util.FontAwesomeIcons;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

public class SessionView extends VBox {

    private final VBox calendarContent;
    private final Button startSessionBtn;
    private LocalDate currentMonth;
    private Routine currentRoutine;
    private Consumer<LocalDate> onDayClick;
    private SessionDAO sessionDAO;
    private SessionExerciseDAO sessionExerciseDAO;

    public SessionView(Consumer<LocalDate> onDayClick, SessionDAO sessionDAO, SessionExerciseDAO sessionExerciseDAO) {
        this.onDayClick = onDayClick;
        this.sessionDAO = sessionDAO;
        this.sessionExerciseDAO = sessionExerciseDAO;
        this.currentMonth = YearMonth.now().atDay(1);
        
        setSpacing(0);
        VBox.setVgrow(this, Priority.ALWAYS);
        setStyle("-fx-background-color: #0f0f1a;");

        // Header with title and start session button
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setSpacing(16);
        header.setPadding(new Insets(24, 24, 16, 24));
        header.setStyle("-fx-background-color: #1a1a2e; -fx-border-color: transparent transparent #2a2a4a transparent;");

        Label title = new Label("Sessions");
        title.getStyleClass().add("dashboard-title");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        startSessionBtn = new Button();
        startSessionBtn.getStyleClass().addAll("btn", "btn-primary");
        startSessionBtn.setGraphic(createIcon(FontAwesomeIcons.PLAY));
        startSessionBtn.setText(" Start Session");
        startSessionBtn.setMinWidth(180);

        header.getChildren().addAll(title, spacer, startSessionBtn);

        // Calendar area
        calendarContent = new VBox();
        calendarContent.setPadding(new Insets(24));
        calendarContent.setSpacing(16);
        VBox.setVgrow(calendarContent, Priority.ALWAYS);

        getChildren().addAll(header, calendarContent);
        
        renderCalendar();
    }

    public void setCurrentRoutine(Routine routine) {
        this.currentRoutine = routine;
        this.currentMonth = YearMonth.now().atDay(1);
        renderCalendar();
    }

    public void setOnStartSession(Runnable onStartSession) {
        startSessionBtn.setOnAction(e -> {
            if (onStartSession != null) {
                onStartSession.run();
            }
        });
    }

    private void renderCalendar() {
        calendarContent.getChildren().clear();

        // Month navigation
        HBox monthNav = new HBox();
        monthNav.setAlignment(Pos.CENTER);
        monthNav.setSpacing(16);

        Button prevBtn = createNavButton(FontAwesomeIcons.CHEVRON_LEFT, () -> {
            currentMonth = currentMonth.minusMonths(1);
            renderCalendar();
        });

        Label monthLabel = new Label(currentMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()) + " " + currentMonth.getYear());
        monthLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: 600; -fx-text-fill: #e8e8f0;");

        Button nextBtn = createNavButton(FontAwesomeIcons.CHEVRON_RIGHT, () -> {
            currentMonth = currentMonth.plusMonths(1);
            renderCalendar();
        });

        monthNav.getChildren().addAll(prevBtn, monthLabel, nextBtn);

        // Calendar grid
        GridPane calendarGrid = new GridPane();
        calendarGrid.setHgap(2);
        calendarGrid.setVgap(2);
        calendarGrid.setAlignment(Pos.CENTER);
        HBox.setHgrow(calendarGrid, Priority.ALWAYS);

        // Day headers
        String[] dayHeaders = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (int i = 0; i < 7; i++) {
            Label dayLabel = new Label(dayHeaders[i]);
            dayLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: 600; -fx-text-fill: #808090; -fx-padding: 8px; -fx-alignment: center;");
            dayLabel.setMinWidth(50);
            dayLabel.setMaxWidth(Double.MAX_VALUE);
            calendarGrid.add(dayLabel, i, 0);
        }

        // Calculate first day of month and number of days
        YearMonth yearMonth = YearMonth.from(currentMonth);
        LocalDate firstOfMonth = yearMonth.atDay(1);
        int dayOfWeekOfFirst = firstOfMonth.getDayOfWeek().getValue() % 7; // 0=Sun, 6=Sat
        int daysInMonth = yearMonth.lengthOfMonth();

        int row = 1;
        int col = 0;

        // Previous month days (grayed out)
        LocalDate prevMonthLastDay = firstOfMonth.minusDays(1);
        for (int i = dayOfWeekOfFirst - 1; i >= 0; i--) {
            LocalDate date = prevMonthLastDay.minusDays(i);
            calendarGrid.add(createDayCell(date, false, false, false, false), col, row);
            col++;
        }

        // Current month days
        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = yearMonth.atDay(day);
            boolean isToday = date.equals(LocalDate.now());
            boolean isSelected = false; // No selection concept now
            boolean hasSession = hasSessionForDate(date);
            boolean sessionDone = isSessionDone(date);
            
            calendarGrid.add(createDayCell(date, isToday, isSelected, hasSession, sessionDone), col, row);
            
            col++;
            if (col > 6) {
                col = 0;
                row++;
            }
        }

        // Next month days to fill the grid (max 6 rows total)
        LocalDate nextMonthFirst = yearMonth.plusMonths(1).atDay(1);
        int nextMonthDay = 1;
        while (row < 6) {
            LocalDate date = nextMonthFirst.plusDays(nextMonthDay - 1);
            calendarGrid.add(createDayCell(date, false, false, false, false), col, row);
            col++;
            nextMonthDay++;
            if (col > 6) {
                col = 0;
                row++;
            }
        }

        calendarContent.getChildren().addAll(monthNav, calendarGrid);

        // Legend
        HBox legend = new HBox(16);
        legend.setAlignment(Pos.CENTER);
        legend.setPadding(new Insets(16, 0, 0, 0));
        legend.getChildren().addAll(
            createLegendItem("#1e88e5", "Today"),
            createLegendItem("#4caf50", "Session Done ✓"),
            createLegendItem("#ff9800", "Session Pending"),
            createLegendItem("#3a3a5a", "Other Month")
        );
        calendarContent.getChildren().add(legend);
    }

    private boolean hasSessionForDate(LocalDate date) {
        if (currentRoutine == null || sessionDAO == null) return false;
        try {
            return sessionDAO.fetchByRoutineAndDate(currentRoutine.getId(), date).isPresent();
        } catch (Exception ex) {
            return false;
        }
    }

    private boolean isSessionDone(LocalDate date) {
        if (currentRoutine == null || sessionDAO == null) return false;
        try {
            var sessionOpt = sessionDAO.fetchByRoutineAndDate(currentRoutine.getId(), date);
            return sessionOpt.isPresent() && sessionOpt.get().getSessionDayDone();
        } catch (Exception ex) {
            return false;
        }
    }

    private VBox createDayCell(LocalDate date, boolean isToday, boolean isSelected, boolean hasSession, boolean sessionDone) {
        VBox cell = new VBox(4);
        cell.setAlignment(Pos.TOP_CENTER);
        cell.setPadding(new Insets(8, 4, 8, 4));
        cell.setMinSize(50, 70);
        cell.setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(cell, Priority.ALWAYS);

        // Determine style
        String bgColor;
        String borderColor;
        String textColor = "#e8e8f0";
        
        if (isToday) {
            bgColor = "#1a1a2e";
            borderColor = "#1e88e5";
        } else if (date.getMonth() != currentMonth.getMonth()) {
            bgColor = "#141426";
            borderColor = "#2a2a4a";
            textColor = "#606078";
        } else {
            bgColor = "#1a1a2e";
            borderColor = "#2a2a4a";
        }

        if (hasSession) {
            if (sessionDone) {
                borderColor = "#4caf50";
                bgColor = "#142d14";
            } else {
                borderColor = "#ff9800";
                bgColor = "#2d2414";
            }
        }

        cell.setStyle("-fx-background-color: " + bgColor + "; -fx-border-color: " + borderColor + "; -fx-border-width: " + (isToday ? "2" : "1") + "; -fx-border-radius: 8; -fx-background-radius: 8;");

        Label dayNum = new Label(String.valueOf(date.getDayOfMonth()));
        dayNum.setStyle("-fx-font-size: 16px; -fx-font-weight: " + (isToday ? "700" : "500") + "; -fx-text-fill: " + textColor + ";");

        // Session indicators
        if (hasSession) {
            if (sessionDone) {
                Label indicator = new Label("✓");
                indicator.setStyle("-fx-font-size: 14px; -fx-text-fill: #4caf50; -fx-font-weight: 700;");
                cell.getChildren().addAll(dayNum, indicator);
            } else {
                Label indicator = new Label("●");
                indicator.setStyle("-fx-font-size: 10px; -fx-text-fill: #ff9800;");
                cell.getChildren().addAll(dayNum, indicator);
            }
        } else {
            cell.getChildren().add(dayNum);
        }

        // Click handler - allow clicking on any date in current month that has an active routine
        if (date.getMonth() == currentMonth.getMonth() && currentRoutine != null && onDayClick != null) {
            // Check if routine is active for this date
            boolean routineActive = currentRoutine.getSince() != null && !date.isBefore(currentRoutine.getSince())
                && (currentRoutine.getUntil() == null || !date.isAfter(currentRoutine.getUntil()));
            boolean routineHasDay = currentRoutine.hasDay(com.routineexe.model.DayOfWeek.fromIndex(date.getDayOfWeek().getValue() - 1));
            
            if (routineActive && routineHasDay) {
                cell.setStyle(cell.getStyle() + " -fx-cursor: hand;");
                cell.setOnMouseClicked(e -> {
                    onDayClick.accept(date);
                });
            }
        }

        return cell;
    }

    private Button createNavButton(String iconUnicode, Runnable action) {
        Button btn = new Button();
        Label icon = new Label(iconUnicode);
        icon.setStyle("-fx-font-family: 'Font Awesome 6 Free'; -fx-font-weight: 900; -fx-font-size: 16px; -fx-text-fill: #808090;");
        btn.setGraphic(icon);
        btn.setStyle("-fx-background-color: #2a2a4a; -fx-background-radius: 6; -fx-padding: 8 12; -fx-border-color: transparent;");
        btn.setOnAction(e -> action.run());
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #3a3a5a; -fx-background-radius: 6; -fx-padding: 8 12;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: #2a2a4a; -fx-background-radius: 6; -fx-padding: 8 12;"));
        return btn;
    }

    private HBox createLegendItem(String color, String text) {
        Label dot = new Label("●");
        dot.setStyle("-fx-font-size: 12px; -fx-text-fill: " + color + ";");
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 12px; -fx-text-fill: #c0c0d0;");
        HBox box = new HBox(6, dot, label);
        box.setAlignment(Pos.CENTER_LEFT);
        return box;
    }

    private Label createIcon(String iconUnicode) {
        Label icon = new Label(iconUnicode);
        icon.setStyle("-fx-font-family: 'Font Awesome 6 Free'; -fx-font-weight: 900; -fx-font-size: 14px; -fx-text-fill: white;");
        return icon;
    }
}