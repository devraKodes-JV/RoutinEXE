package com.routineexe.database;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Gestiona la conexión a la base de datos SQLite y la creación del esquema.
 * La base de datos se almacena en el directorio de datos de la aplicación.
 */
public final class DatabaseHelper {

    private static final String DB_NAME = "routin-exe.db";
    private static final String TABLE_USERS = "users";
    private static final String TABLE_CATEGORIES = "categories";
    private static final String TABLE_EXERCISES = "exercises";
    private static final String TABLE_ROUTINES = "routines";
    private static final String TABLE_ROUTINE_DAYS = "routine_days";
    private static final String TABLE_ROUTINE_EXERCISES = "routine_exercises";
    private static final String TABLE_SESSIONS = "sessions";
    private static final String TABLE_SESSION_EXERCISES = "session_exercises";

    private static Connection connection;
    private static final Object lock = new Object();

    private DatabaseHelper() {
    }

    public static synchronized Connection getConnection() throws Exception {
        if (connection == null || connection.isClosed()) {
            Path dataDir = Paths.get(System.getProperty("user.dir"), "data");
            Files.createDirectories(dataDir);
            Path dbPath = dataDir.resolve(DB_NAME);
            String url = "jdbc:sqlite:" + dbPath.toAbsolutePath();

            connection = DriverManager.getConnection(url);
            initializeSchema(connection);
            seedDatabase(connection);
        }
        return connection;
    }

    private static void seedDatabase(Connection conn) throws Exception {
        boolean empty = conn.createStatement().executeQuery("SELECT COUNT(*) FROM " + TABLE_CATEGORIES).getInt(1) == 0;
        if (!empty) return;

        Path dataDir = Paths.get(System.getProperty("user.dir"), "data");
        Path seedPath = dataDir.resolve("seed.sql");
        if (!Files.exists(seedPath)) return;

        String sql = Files.readString(seedPath, StandardCharsets.UTF_8);
        conn.setAutoCommit(false);
        try (Statement stmt = conn.createStatement()) {
            for (String batch : sql.split(";")) {
                String trimmed = batch.trim();
                if (!trimmed.isEmpty()) {
                    stmt.execute(trimmed);
                }
            }
            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    private static void initializeSchema(Connection conn) throws Exception {
        String usersSql = "CREATE TABLE IF NOT EXISTS " + TABLE_USERS + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "username TEXT NOT NULL UNIQUE, "
                + "age INTEGER, "
                + "height REAL, "
                + "weight REAL"
                + ")";

        String categoriesSql = "CREATE TABLE IF NOT EXISTS " + TABLE_CATEGORIES + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name TEXT NOT NULL UNIQUE"
                + ")";

        String exercisesSql = "CREATE TABLE IF NOT EXISTS " + TABLE_EXERCISES + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name TEXT NOT NULL, "
                + "category_id INTEGER, "
                + "description TEXT, "
                + "time_based INTEGER NOT NULL DEFAULT 0, "
                + "FOREIGN KEY (category_id) REFERENCES " + TABLE_CATEGORIES + "(id) ON DELETE SET NULL"
                + ")";

        String routinesSql = "CREATE TABLE IF NOT EXISTS " + TABLE_ROUTINES + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "user_id INTEGER NOT NULL, "
                + "name TEXT NOT NULL, "
                + "since_date DATE, "
                + "until_date DATE, "
                + "FOREIGN KEY (user_id) REFERENCES " + TABLE_USERS + "(id) ON DELETE CASCADE"
                + ")";

        String routineDaysSql = "CREATE TABLE IF NOT EXISTS " + TABLE_ROUTINE_DAYS + " ("
                + "routine_id INTEGER NOT NULL, "
                + "day_of_week INTEGER NOT NULL CHECK (day_of_week >= 0 AND day_of_week <= 6), "
                + "PRIMARY KEY (routine_id, day_of_week), "
                + "FOREIGN KEY (routine_id) REFERENCES " + TABLE_ROUTINES + "(id) ON DELETE CASCADE"
                + ")";

        String routineExercisesSql = "CREATE TABLE IF NOT EXISTS " + TABLE_ROUTINE_EXERCISES + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "routine_id INTEGER NOT NULL, "
                + "exercise_id INTEGER NOT NULL, "
                + "day_of_week INTEGER NOT NULL CHECK (day_of_week >= 0 AND day_of_week <= 6), "
                + "sets INTEGER NOT NULL, "
                + "reps INTEGER NOT NULL, "
                + "FOREIGN KEY (routine_id) REFERENCES " + TABLE_ROUTINES + "(id) ON DELETE CASCADE, "
                + "FOREIGN KEY (exercise_id) REFERENCES " + TABLE_EXERCISES + "(id) ON DELETE CASCADE"
                + ")";

        String sessionsSql = "CREATE TABLE IF NOT EXISTS " + TABLE_SESSIONS + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "routine_id INTEGER NOT NULL, "
                + "date DATE NOT NULL, "
                + "day_of_week INTEGER NOT NULL CHECK (day_of_week >= 0 AND day_of_week <= 6), "
                + "session_day_done INTEGER NOT NULL DEFAULT 0, "
                + "created_at DATETIME DEFAULT CURRENT_TIMESTAMP, "
                + "FOREIGN KEY (routine_id) REFERENCES " + TABLE_ROUTINES + "(id) ON DELETE CASCADE, "
                + "UNIQUE(routine_id, date)"
                + ")";

        String sessionExercisesSql = "CREATE TABLE IF NOT EXISTS " + TABLE_SESSION_EXERCISES + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "session_id INTEGER NOT NULL, "
                + "exercise_id INTEGER NOT NULL, "
                + "sets INTEGER, "
                + "reps INTEGER, "
                + "weight REAL, "
                + "done INTEGER NOT NULL DEFAULT 0, "
                + "FOREIGN KEY (session_id) REFERENCES " + TABLE_SESSIONS + "(id) ON DELETE CASCADE, "
                + "FOREIGN KEY (exercise_id) REFERENCES " + TABLE_EXERCISES + "(id) ON DELETE CASCADE"
                + ")";

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(usersSql);
            stmt.execute(categoriesSql);
            stmt.execute(exercisesSql);
            stmt.execute(routinesSql);
            stmt.execute(routineDaysSql);
            stmt.execute(routineExercisesSql);
            stmt.execute(sessionsSql);
            stmt.execute(sessionExercisesSql);
        }
    }

    public static String getUsersTableName() {
        return TABLE_USERS;
    }

    public static String getCategoriesTableName() {
        return TABLE_CATEGORIES;
    }

    public static String getExercisesTableName() {
        return TABLE_EXERCISES;
    }

    public static String getRoutinesTableName() {
        return TABLE_ROUTINES;
    }

    public static String getRoutineDaysTableName() {
        return TABLE_ROUTINE_DAYS;
    }

    public static String getRoutineExercisesTableName() {
        return TABLE_ROUTINE_EXERCISES;
    }

    public static String getSessionsTableName() {
        return TABLE_SESSIONS;
    }

    public static String getSessionExercisesTableName() {
        return TABLE_SESSION_EXERCISES;
    }
}