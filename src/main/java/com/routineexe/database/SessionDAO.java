package com.routineexe.database;

import com.routineexe.model.Session;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SessionDAO {

    public List<Session> fetchByUser(long userId) throws Exception {
        List<Session> sessions = new ArrayList<>();
        String sql = "SELECT s.id, s.routine_id, s.date, s.day_of_week, s.session_day_done, s.created_at "
                + "FROM " + DatabaseHelper.getSessionsTableName() + " s "
                + "JOIN " + DatabaseHelper.getRoutinesTableName() + " r ON s.routine_id = r.id "
                + "WHERE r.user_id = ? ORDER BY s.date DESC";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    sessions.add(mapRow(rs));
                }
            }
        }
        return sessions;
    }

    public List<Session> fetchByRoutine(long routineId) throws Exception {
        List<Session> sessions = new ArrayList<>();
        String sql = "SELECT id, routine_id, date, day_of_week, session_day_done, created_at FROM " + DatabaseHelper.getSessionsTableName() + " WHERE routine_id = ? ORDER BY date DESC";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, routineId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    sessions.add(mapRow(rs));
                }
            }
        }
        return sessions;
    }

    public Optional<Session> fetchByRoutineAndDate(long routineId, LocalDate date) throws Exception {
        String sql = "SELECT id, routine_id, date, day_of_week, session_day_done, created_at FROM " + DatabaseHelper.getSessionsTableName() + " WHERE routine_id = ? AND date = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, routineId);
            pstmt.setDate(2, Date.valueOf(date));
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        }
        return Optional.empty();
    }

    public Optional<Session> fetchById(long id) throws Exception {
        String sql = "SELECT id, routine_id, date, day_of_week, session_day_done, created_at FROM " + DatabaseHelper.getSessionsTableName() + " WHERE id = ?";
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

    public long insert(Session session) throws Exception {
        String sql = "INSERT INTO " + DatabaseHelper.getSessionsTableName() + " (routine_id, date, day_of_week, session_day_done) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setLong(1, session.getRoutineId());
            pstmt.setDate(2, Date.valueOf(session.getDate()));
            pstmt.setInt(3, session.getDayOfWeekIndex());
            pstmt.setInt(4, session.getSessionDayDone() ? 1 : 0);
            pstmt.executeUpdate();
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    long id = rs.getLong(1);
                    session.setId(id);
                    return id;
                }
            }
        }
        throw new SQLException("Failed to insert session");
    }

    public boolean update(Session session) throws Exception {
        String sql = "UPDATE " + DatabaseHelper.getSessionsTableName() + " SET routine_id = ?, date = ?, day_of_week = ?, session_day_done = ? WHERE id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, session.getRoutineId());
            pstmt.setDate(2, Date.valueOf(session.getDate()));
            pstmt.setInt(3, session.getDayOfWeekIndex());
            pstmt.setInt(4, session.getSessionDayDone() ? 1 : 0);
            pstmt.setLong(5, session.getId());
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean updateDayDone(long sessionId, boolean done) throws Exception {
        String sql = "UPDATE " + DatabaseHelper.getSessionsTableName() + " SET session_day_done = ? WHERE id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, done ? 1 : 0);
            pstmt.setLong(2, sessionId);
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean delete(long id) throws Exception {
        String sql = "DELETE FROM " + DatabaseHelper.getSessionsTableName() + " WHERE id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean deleteByRoutine(long routineId) throws Exception {
        String sql = "DELETE FROM " + DatabaseHelper.getSessionsTableName() + " WHERE routine_id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, routineId);
            return pstmt.executeUpdate() > 0;
        }
    }

    private Session mapRow(ResultSet rs) throws SQLException {
        Session session = new Session();
        session.setId(rs.getLong("id"));
        session.setRoutineId(rs.getLong("routine_id"));
        
        Date date = rs.getDate("date");
        if (date != null) {
            session.setDate(date.toLocalDate());
        }
        
        session.setDayOfWeekIndex(rs.getInt("day_of_week"));
        session.setSessionDayDone(rs.getInt("session_day_done") == 1);
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            session.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        return session;
    }
}