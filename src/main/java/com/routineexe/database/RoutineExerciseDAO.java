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
        String sql = "SELECT re.id, re.routine_id, re.exercise_id, re.day_of_week, re.sets, re.reps, e.name as exercise_name "
                + "FROM " + DatabaseHelper.getRoutineExercisesTableName() + " re "
                + "LEFT JOIN " + DatabaseHelper.getExercisesTableName() + " e ON re.exercise_id = e.id "
                + "WHERE re.routine_id = ? ORDER BY re.day_of_week, re.id";
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
                    re.setExercise(exercise);
                    
                    int dayIndex = rs.getInt("day_of_week");
                    re.setDayOfWeek(DayOfWeek.fromIndex(dayIndex));
                    
                    re.setSets(rs.getInt("sets"));
                    re.setReps(rs.getInt("reps"));
                    
                    routineExercises.add(re);
                }
            }
        }
        return routineExercises;
    }

    public long insert(RoutineExercise routineExercise) throws Exception {
        String sql = "INSERT INTO " + DatabaseHelper.getRoutineExercisesTableName() + " (routine_id, exercise_id, day_of_week, sets, reps) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setLong(1, routineExercise.getRoutine().getId());
            pstmt.setLong(2, routineExercise.getExercise().getId());
            pstmt.setInt(3, routineExercise.getDayOfWeek().getIndex());
            pstmt.setInt(4, routineExercise.getSets());
            pstmt.setInt(5, routineExercise.getReps());
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
        String sql = "UPDATE " + DatabaseHelper.getRoutineExercisesTableName() + " SET routine_id = ?, exercise_id = ?, day_of_week = ?, sets = ?, reps = ? WHERE id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, routineExercise.getRoutine().getId());
            pstmt.setLong(2, routineExercise.getExercise().getId());
            pstmt.setInt(3, routineExercise.getDayOfWeek().getIndex());
            pstmt.setInt(4, routineExercise.getSets());
            pstmt.setInt(5, routineExercise.getReps());
            pstmt.setLong(6, routineExercise.getId());
            return pstmt.executeUpdate() > 0;
        }
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