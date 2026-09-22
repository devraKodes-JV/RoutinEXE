# RoutinEXE

**RoutinEXE** — A cross-platform offline fitness routine management application.

## Description

RoutinEXE is a JavaFX desktop application that allows managing exercise routines, training sessions, exercise categories, and users. It works completely offline with a local SQLite database.

## Features

- **Users**: Create, edit, and delete users (username required, age/height/weight optional).
- **Categories**: Manage exercise categories (e.g., Cardio, Strength, Flexibility).
- **Exercises**: CRUD for exercises assigned to categories, with description and time-based flag.
- **Routines**: Create routines per user with days of the week, start/end dates, and assigned exercises (sets, reps).
- **Sessions**: Record training sessions by date, with exercise tracking (done/undo) and day completion status.
- **Dashboard**: Overview per user with statistics and navigation to all sections.
- **Calendar**: Calendar view for managing sessions by day and month.

## Architecture

```
src/main/java/com/routineexe/
├── view/           # UI (JavaFX)
│   ├── Main.java              # Main entry point
│   ├── DashboardLayout.java   # Sidebar + dashboard layout
│   ├── DashboardView.java
│   ├── users/                 # UserView, UserListView, UserFormDialog...
│   ├── categories/            # CategoryView, CategoryListView...
│   ├── exercises/             # ExerciseView, ExerciseListView...
│   ├── routines/              # RoutineView, RoutineListView...
│   └── sessions/              # SessionView, SessionListView...
├── model/          # Data models
│   ├── User.java
│   ├── Category.java
│   ├── Exercise.java
│   ├── Routine.java
│   ├── RoutineExercise.java
│   ├── Session.java
│   └── SessionExercise.java
├── database/       # Data access (DAO + helper)
│   ├── DatabaseHelper.java    # SQLite connection + schema
│   ├── UserDAO.java
│   ├── CategoryDAO.java
│   ├── ExerciseDAO.java
│   ├── RoutineDAO.java
│   ├── RoutineExerciseDAO.java
│   ├── SessionDAO.java
│   └── SessionExerciseDAO.java
└── util/           # Utilities
    └── FontAwesomeIcons.java

src/main/resources/
├── styles.css              # Styles (dark theme)
├── bootstrap-icons.css     # Bootstrap icons
└── fonts/                  # Font Awesome + Bootstrap Icons (.ttf/.woff2)

data/
└── routin-exe.db           # SQLite database (auto-created)
```

## Technologies

| Technology | Version |
|------------|---------|
| Java | 25 |
| JavaFX | 25.0.4 |
| SQLite JDBC | 3.49.1.0 |
| Build | Gradle + Maven |

## Prerequisites

- **JDK 25** or higher
- Linux (desktop app); also supports Windows and macOS (profiles in pom.xml)

## Building

### With Gradle
```bash
./gradlew build
```

### With Maven
```bash
mvn clean package
```

## Running

### AppImage (recommended for Linux)
```bash
./dist/RoutinEXE-x86_64.AppImage
```

### JAR
```bash
java -jar dist/routin-exe.jar
```

### From IDE
Run the `com.routineexe.view.Main` class.

## Database

The SQLite database is automatically created at `data/routin-exe.db` on first launch. It contains the following tables:

| Table | Description |
|-------|-------------|
| users | Users (id, username, age, height, weight) |
| categories | Exercise categories |
| exercises | Exercises (name, category, description, time_based) |
| routines | Routines (user, name, start/end date) |
| routine_days | Days of the week assigned to routines |
| routine_exercises | Exercises assigned to routines (day, sets, reps) |
| sessions | Training sessions (routine, date, day, completed) |
| session_exercises | Exercises in sessions (sets, reps, weight, done) |

### Seeding the Database

A comprehensive seed file is available at `data/seed.sql` containing 6 categories and 150 exercises (25 per category), with a mix of equipment-based and bodyweight exercises:

| Category | Exercises |
|----------|-----------|
| Cardio & Endurance | 25 |
| Upper Body Strength | 25 |
| Lower Body Strength | 25 |
| Full Body Strength | 25 |
| Core & Abs | 25 |
| Flexibility & Mobility | 25 |

To load the seed, run the SQL file against the database after the schema has been initialized:

```bash
sqlite3 data/routin-exe.db < data/seed.sql
```

The seed resets all data before inserting (DELETE + INSERT), so it is safe to re-run.

## Distribution Structure

```
dist/
├── RoutinEXE-x86_64.AppImage    # Linux executable
└── routin-exe.jar                 # Executable JAR
```

## License

Private development project.
