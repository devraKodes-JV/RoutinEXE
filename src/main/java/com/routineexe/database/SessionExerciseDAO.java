package com.routineexe.database;

import com.routineexe.model.Exercise;
import com.routineexe.model.SessionExercise;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SessionExerciseDAO {

    private final ExerciseDAO exerciseDAO;

    public SessionExerciseDAO(ExerciseDAO exerciseDAO) {
        this.exerciseDAO = exerciseDAO;
    }

    public List<SessionExercise> fetchBySession(long sessionId) throws Exception {
        List<SessionExercise> sessionExercises = new ArrayList<>();
        String sql = "SELECT se.id, se.session_id, se.exercise_id, se.sets, se.reps, se.weight, se.done, e.name as exercise_name, e.time_based "
                + "FROM " + DatabaseHelper.getSessionExercisesTableName() + " se "
                + "LEFT JOIN " + DatabaseHelper.getExercisesTableName() + " e ON se.exercise_id = e.id "
                + "WHERE se.session_id = ? ORDER BY se.id";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, sessionId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    sessionExercises.add(mapRow(rs));
                }
            }
        }
        return sessionExercises;
    }

    public Optional<SessionExercise> fetchById(long id) throws Exception {
        String sql = "SELECT se.id, se.session_id, se.exercise_id, se.sets, se.reps, se.weight, se.done, e.name as exercise_name, e.time_based "
                + "FROM " + DatabaseHelper.getSessionExercisesTableName() + " se "
                + "LEFT JOIN " + DatabaseHelper.getExercisesTableName() + " e ON se.exercise_id = e.id "
                + "WHERE se.id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        }
        return Optional.empty();
    }

    public long insert(SessionExercise sessionExercise) throws Exception {
        String sql = "INSERT INTO " + DatabaseHelper.getSessionExercisesTableName() + " (session_id, exercise_id, sets, reps, weight, done) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setLong(1, sessionExercise.getSessionId());
            pstmt.setLong(2, sessionExercise.getExerciseId());
            if (sessionExercise.getSets() != null) {
                pstmt.setInt(3, sessionExercise.getSets());
            } else {
                pstmt.setNull(3, Types.INTEGER);
            }
            if (sessionExercise.getReps() != null) {
                pstmt.setInt(4, sessionExercise.getReps());
            } else {
                pstmt.setNull(4, Types.INTEGER);
            }
            if (sessionExercise.getWeight() != null) {
                pstmt.setDouble(5, sessionExercise.getWeight());
            } else {
                pstmt.setNull(5, Types.DOUBLE);
            }
            pstmt.setInt(6, sessionExercise.getDone() ? 1 : 0);
            pstmt.executeUpdate();
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        }
        throw new SQLException("Failed to insert session exercise");
    }

    public boolean update(SessionExercise sessionExercise) throws Exception {
        String sql = "UPDATE " + DatabaseHelper.getSessionExercisesTableName() + " SET session_id = ?, exercise_id = ?, sets = ?, reps = ?, weight = ?, done = ? WHERE id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, sessionExercise.getSessionId());
            pstmt.setLong(2, sessionExercise.getExerciseId());
            if (sessionExercise.getSets() != null) {
                pstmt.setInt(3, sessionExercise.getSets());
            } else {
                pstmt.setNull(3, Types.INTEGER);
            }
            if (sessionExercise.getReps() != null) {
                pstmt.setInt(4, sessionExercise.getReps());
            } else {
                pstmt.setNull(4, Types.INTEGER);
            }
            if (sessionExercise.getWeight() != null) {
                pstmt.setDouble(5, sessionExercise.getWeight());
            } else {
                pstmt.setNull(5, Types.DOUBLE);
            }
            pstmt.setInt(6, sessionExercise.getDone() ? 1 : 0);
            pstmt.setLong(7, sessionExercise.getId());
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean updateDone(long id, boolean done) throws Exception {
        String sql = "UPDATE " + DatabaseHelper.getSessionExercisesTableName() + " SET done = ? WHERE id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, done ? 1 : 0);
            pstmt.setLong(2, id);
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean delete(long id) throws Exception {
        String sql = "DELETE FROM " + DatabaseHelper.getSessionExercisesTableName() + " WHERE id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean deleteBySession(long sessionId) throws Exception {
        String sql = "DELETE FROM " + DatabaseHelper.getSessionExercisesTableName() + " WHERE session_id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, sessionId);
            return pstmt.executeUpdate() > 0;
        }
    }

    private SessionExercise mapRow(ResultSet rs) throws SQLException {
        SessionExercise se = new SessionExercise();
        se.setId(rs.getLong("id"));
        se.setSessionId(rs.getLong("session_id"));
        se.setExerciseId(rs.getLong("exercise_id"));
        int sets = rs.getInt("sets");
        if (!rs.wasNull()) {
            se.setSets(sets);
        }
        int reps = rs.getInt("reps");
        if (!rs.wasNull()) {
            se.setReps(reps);
        }
        
        double weight = rs.getDouble("weight");
        if (!rs.wasNull()) {
            se.setWeight(weight);
        }
        
        se.setDone(rs.getInt("done") == 1);
        return se;
    }
}