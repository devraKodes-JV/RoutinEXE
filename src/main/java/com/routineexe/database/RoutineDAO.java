package com.routineexe.database;

import com.routineexe.model.DayOfWeek;
import com.routineexe.model.Routine;
import com.routineexe.model.RoutineExercise;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class RoutineDAO {

    private final ExerciseDAO exerciseDAO;

    public RoutineDAO(ExerciseDAO exerciseDAO) {
        this.exerciseDAO = exerciseDAO;
    }

    public List<Routine> fetchAll() throws Exception {
        return fetchAll(null);
    }

    public List<Routine> fetchByUser(Long userId) throws Exception {
        List<Routine> routines = new ArrayList<>();
        String sql = "SELECT id, user_id, name, since_date, until_date FROM " + DatabaseHelper.getRoutinesTableName() + " WHERE user_id = ? ORDER BY id DESC";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Routine routine = new Routine();
                    routine.setId(rs.getLong("id"));
                    routine.setUserId(rs.getLong("user_id"));
                    routine.setName(rs.getString("name"));
                    
                    LocalDate sinceDate = rs.getObject("since_date", LocalDate.class);
                    if (sinceDate != null) {
                        routine.setSince(sinceDate);
                    }
                    
                    LocalDate untilDate = rs.getObject("until_date", LocalDate.class);
                    if (untilDate != null) {
                        routine.setUntil(untilDate);
                    }
                    
                    routines.add(routine);
                }
            }
        }
        
        // Load days and exercises for each routine
        for (Routine routine : routines) {
            loadRoutineDays(routine);
            loadRoutineExercises(routine);
        }
        
        return routines;
    }

    private List<Routine> fetchAll(Long userId) throws Exception {
        List<Routine> routines = new ArrayList<>();
        String sql;
        if (userId != null) {
            sql = "SELECT id, user_id, name, since_date, until_date FROM " + DatabaseHelper.getRoutinesTableName() + " WHERE user_id = ? ORDER BY id DESC";
        } else {
            sql = "SELECT id, user_id, name, since_date, until_date FROM " + DatabaseHelper.getRoutinesTableName() + " ORDER BY id DESC";
        }
        try (Connection conn = DatabaseHelper.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            if (userId != null) {
                pstmt.setLong(1, userId);
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Routine routine = new Routine();
                    routine.setId(rs.getLong("id"));
                    routine.setUserId(rs.getLong("user_id"));
                    routine.setName(rs.getString("name"));
                    
                    LocalDate sinceDate = rs.getObject("since_date", LocalDate.class);
                    if (sinceDate != null) {
                        routine.setSince(sinceDate);
                    }
                    
                    LocalDate untilDate = rs.getObject("until_date", LocalDate.class);
                    if (untilDate != null) {
                        routine.setUntil(untilDate);
                    }
                    
                    routines.add(routine);
                }
            }
        }
        
        // Load days and exercises for each routine
        for (Routine routine : routines) {
            loadRoutineDays(routine);
            loadRoutineExercises(routine);
        }
        
        return routines;
    }

    private void loadRoutineDays(Routine routine) throws Exception {
        String sql = "SELECT day_of_week FROM " + DatabaseHelper.getRoutineDaysTableName() + " WHERE routine_id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, routine.getId());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int dayIndex = rs.getInt("day_of_week");
                    DayOfWeek day = DayOfWeek.fromIndex(dayIndex);
                    if (day != null) {
                        routine.addDay(day);
                    }
                }
            }
        }
    }

    private void loadRoutineExercises(Routine routine) throws Exception {
        // This will be handled by RoutineExerciseDAO
    }

    public long insert(Routine routine) throws Exception {
        // Check for overlapping routines
        if (hasOverlappingRoutine(routine.getUserId(), routine.getSince(), routine.getUntil(), null)) {
            throw new SQLException("A routine already exists for this date range. Routines cannot overlap.");
        }
        
        String sql = "INSERT INTO " + DatabaseHelper.getRoutinesTableName() + " (user_id, name, since_date, until_date) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setLong(1, routine.getUserId());
            pstmt.setString(2, routine.getName());
            if (routine.getSince() != null) {
                pstmt.setDate(3, Date.valueOf(routine.getSince()));
            } else {
                pstmt.setNull(3, Types.DATE);
            }
            if (routine.getUntil() != null) {
                pstmt.setDate(4, Date.valueOf(routine.getUntil()));
            } else {
                pstmt.setNull(4, Types.DATE);
            }
            pstmt.executeUpdate();
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    long routineId = rs.getLong(1);
                    routine.setId(routineId);
                    // Insert days
                    insertRoutineDays(conn, routineId, routine.getDays());
                    return routineId;
                }
            }
        }
        throw new SQLException("Failed to insert routine");
    }

    private void insertRoutineDays(Connection conn, long routineId, Set<DayOfWeek> days) throws SQLException {
        String sql = "INSERT INTO " + DatabaseHelper.getRoutineDaysTableName() + " (routine_id, day_of_week) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (DayOfWeek day : days) {
                pstmt.setLong(1, routineId);
                pstmt.setInt(2, day.getIndex());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        }
    }

    public boolean update(Routine routine) throws Exception {
        // Check for overlapping routines (excluding this routine)
        if (hasOverlappingRoutine(routine.getUserId(), routine.getSince(), routine.getUntil(), routine.getId())) {
            throw new SQLException("A routine already exists for this date range. Routines cannot overlap.");
        }
        
        String sql = "UPDATE " + DatabaseHelper.getRoutinesTableName() + " SET name = ?, since_date = ?, until_date = ? WHERE id = ? AND user_id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, routine.getName());
            if (routine.getSince() != null) {
                pstmt.setDate(2, Date.valueOf(routine.getSince()));
            } else {
                pstmt.setNull(2, Types.DATE);
            }
            if (routine.getUntil() != null) {
                pstmt.setDate(3, Date.valueOf(routine.getUntil()));
            } else {
                pstmt.setNull(3, Types.DATE);
            }
            pstmt.setLong(4, routine.getId());
            pstmt.setLong(5, routine.getUserId());
            int updated = pstmt.executeUpdate();
            
            if (updated > 0) {
                // Update days - delete and re-insert
                deleteRoutineDays(conn, routine.getId());
                insertRoutineDays(conn, routine.getId(), routine.getDays());
                return true;
            }
            return false;
        }
    }

    private void deleteRoutineDays(Connection conn, long routineId) throws SQLException {
        String sql = "DELETE FROM " + DatabaseHelper.getRoutineDaysTableName() + " WHERE routine_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, routineId);
            pstmt.executeUpdate();
        }
    }

    public boolean delete(long id, Long userId) throws Exception {
        String sql = "DELETE FROM " + DatabaseHelper.getRoutinesTableName() + " WHERE id = ? AND user_id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.setLong(2, userId);
            return pstmt.executeUpdate() > 0;
        }
    }

    private boolean hasOverlappingRoutine(Long userId, LocalDate since, LocalDate until, Long excludeRoutineId) throws Exception {
        if (since == null) {
            return false; // No start date means no overlap check needed
        }
        
        LocalDate effectiveUntil = until != null ? until : LocalDate.MAX;
        
        String sql = "SELECT id FROM " + DatabaseHelper.getRoutinesTableName() + 
            " WHERE user_id = ? " +
            "AND since_date <= ? " +
            "AND (until_date IS NULL OR until_date >= ?)";
        
        if (excludeRoutineId != null) {
            sql += " AND id != ?";
        }
        
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, userId);
            pstmt.setDate(2, Date.valueOf(effectiveUntil));
            pstmt.setDate(3, Date.valueOf(since));
            if (excludeRoutineId != null) {
                pstmt.setLong(4, excludeRoutineId);
            }
            
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next(); // If any row exists, there's an overlap
            }
        }
    }
}