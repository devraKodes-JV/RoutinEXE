package com.routineexe.database;

import com.routineexe.model.DayOfWeek;
import com.routineexe.model.Exercise;
import com.routineexe.model.Routine;
import com.routineexe.model.RoutineExercise;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoutineExerciseDAO {

    private final ExerciseDAO exerciseDAO;

    public RoutineExerciseDAO(ExerciseDAO exerciseDAO) {
        this.exerciseDAO = exerciseDAO;
    }

    public List<RoutineExercise> fetchByRoutine(long routineId) throws Exception {
        List<RoutineExercise> routineExercises = new ArrayList<>();
        String sql = "SELECT re.id, re.routine_id, re.exercise_id, re.day_of_week, re.sets, re.reps, re.order_index, "
                + "e.name as exercise_name, e.time_based as exercise_time_based "
                + "FROM " + DatabaseHelper.getRoutineExercisesTableName() + " re "
                + "LEFT JOIN " + DatabaseHelper.getExercisesTableName() + " e ON re.exercise_id = e.id "
                + "WHERE re.routine_id = ? ORDER BY re.day_of_week, re.order_index, re.id";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, routineId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    RoutineExercise re = new RoutineExercise();
                    re.setId(rs.getLong("id"));
                    
                    Routine routine = new Routine();
                    routine.setId(rs.getLong("routine_id"));
                    re.setRoutine(routine);
                    
                    Exercise exercise = new Exercise();
                    exercise.setId(rs.getLong("exercise_id"));
                    exercise.setName(rs.getString("exercise_name"));
                    exercise.setTimeBased(rs.getInt("exercise_time_based") == 1);
                    re.setExercise(exercise);
                    
                    int dayIndex = rs.getInt("day_of_week");
                    re.setDayOfWeek(DayOfWeek.fromIndex(dayIndex));
                    
                    re.setSets(rs.getInt("sets"));
                    re.setReps(rs.getInt("reps"));
                    re.setOrderIndex(rs.getInt("order_index"));
                    
                    routineExercises.add(re);
                }
            }
        }
        return routineExercises;
    }

    /**
     * Devuelve el próximo índice de orden disponible dentro de un día de la rutina.
     */
    public int nextOrderIndex(long routineId, DayOfWeek dayOfWeek) throws Exception {
        String sql = "SELECT COALESCE(MAX(order_index), 0) + 1 FROM " + DatabaseHelper.getRoutineExercisesTableName()
                + " WHERE routine_id = ? AND day_of_week = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, routineId);
            pstmt.setInt(2, dayOfWeek.getIndex());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 1;
    }

    public long insert(RoutineExercise routineExercise) throws Exception {
        int orderIndex = routineExercise.getOrderIndex() != null
                ? routineExercise.getOrderIndex()
                : nextOrderIndex(routineExercise.getRoutine().getId(), routineExercise.getDayOfWeek());
        routineExercise.setOrderIndex(orderIndex);

        String sql = "INSERT INTO " + DatabaseHelper.getRoutineExercisesTableName()
                + " (routine_id, exercise_id, day_of_week, sets, reps, order_index) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setLong(1, routineExercise.getRoutine().getId());
            pstmt.setLong(2, routineExercise.getExercise().getId());
            pstmt.setInt(3, routineExercise.getDayOfWeek().getIndex());
            pstmt.setInt(4, routineExercise.getSets());
            pstmt.setInt(5, routineExercise.getReps());
            pstmt.setInt(6, orderIndex);
            pstmt.executeUpdate();
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        }
        throw new SQLException("Failed to insert routine exercise");
    }

    public boolean update(RoutineExercise routineExercise) throws Exception {
        if (routineExercise.getOrderIndex() == null) {
            routineExercise.setOrderIndex(nextOrderIndex(routineExercise.getRoutine().getId(), routineExercise.getDayOfWeek()));
        }
        String sql = "UPDATE " + DatabaseHelper.getRoutineExercisesTableName()
                + " SET routine_id = ?, exercise_id = ?, day_of_week = ?, sets = ?, reps = ?, order_index = ? WHERE id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, routineExercise.getRoutine().getId());
            pstmt.setLong(2, routineExercise.getExercise().getId());
            pstmt.setInt(3, routineExercise.getDayOfWeek().getIndex());
            pstmt.setInt(4, routineExercise.getSets());
            pstmt.setInt(5, routineExercise.getReps());
            pstmt.setInt(6, routineExercise.getOrderIndex());
            pstmt.setLong(7, routineExercise.getId());
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean updateOrder(long id, int orderIndex) throws Exception {
        String sql = "UPDATE " + DatabaseHelper.getRoutineExercisesTableName() + " SET order_index = ? WHERE id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, orderIndex);
            pstmt.setLong(2, id);
            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Persiste un ordenamiento completo (ids en el orden desired) para un día de la rutina.
     */
    public boolean saveOrder(long routineId, DayOfWeek dayOfWeek, List<Long> orderedIds) throws Exception {
        String sql = "UPDATE " + DatabaseHelper.getRoutineExercisesTableName()
                + " SET order_index = ? WHERE id = ? AND routine_id = ? AND day_of_week = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            try {
                int index = 1;
                for (Long id : orderedIds) {
                    pstmt.setInt(1, index++);
                    pstmt.setLong(2, id);
                    pstmt.setLong(3, routineId);
                    pstmt.setInt(4, dayOfWeek.getIndex());
                    pstmt.addBatch();
                }
                pstmt.executeBatch();
                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
        return true;
    }

    public boolean delete(long id) throws Exception {
        String sql = "DELETE FROM " + DatabaseHelper.getRoutineExercisesTableName() + " WHERE id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean deleteByRoutine(long routineId) throws Exception {
        String sql = "DELETE FROM " + DatabaseHelper.getRoutineExercisesTableName() + " WHERE routine_id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, routineId);
            return pstmt.executeUpdate() > 0;
        }
    }
}