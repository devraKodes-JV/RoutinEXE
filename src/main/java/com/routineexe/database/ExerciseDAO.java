package com.routineexe.database;

import com.routineexe.model.Category;
import com.routineexe.model.Exercise;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExerciseDAO {

    private final CategoryDAO categoryDAO;

    public ExerciseDAO(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    public List<Exercise> fetchAll() throws Exception {
        List<Exercise> exercises = new ArrayList<>();
        String sql = "SELECT e.id, e.name, e.category_id, e.description, e.time_based, c.name as category_name "
                + "FROM " + DatabaseHelper.getExercisesTableName() + " e "
                + "LEFT JOIN " + DatabaseHelper.getCategoriesTableName() + " c ON e.category_id = c.id "
                + "ORDER BY e.id DESC";
        try (Connection conn = DatabaseHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Exercise exercise = new Exercise();
                exercise.setId(rs.getLong("id"));
                exercise.setName(rs.getString("name"));
                
                Long categoryId = rs.getLong("category_id");
                if (!rs.wasNull()) {
                    Category category = new Category();
                    category.setId(categoryId);
                    category.setName(rs.getString("category_name"));
                    exercise.setCategory(category);
                }
                
                String description = rs.getString("description");
                if (description != null) {
                    exercise.setDescription(description);
                }
                
                exercise.setTimeBased(rs.getBoolean("time_based"));
                exercises.add(exercise);
            }
        }
        return exercises;
    }

    public long insert(Exercise exercise) throws Exception {
        String sql = "INSERT INTO " + DatabaseHelper.getExercisesTableName() + " (name, category_id, description, time_based) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, exercise.getName());
            if (exercise.getCategory() != null && exercise.getCategory().getId() != null) {
                pstmt.setLong(2, exercise.getCategory().getId());
            } else {
                pstmt.setNull(2, Types.BIGINT);
            }
            if (exercise.getDescription() != null) {
                pstmt.setString(3, exercise.getDescription());
            } else {
                pstmt.setNull(3, Types.VARCHAR);
            }
            pstmt.setBoolean(4, exercise.isTimeBased());
            pstmt.executeUpdate();
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        }
        throw new SQLException("Failed to insert exercise");
    }

    public boolean update(Exercise exercise) throws Exception {
        String sql = "UPDATE " + DatabaseHelper.getExercisesTableName() + " SET name = ?, category_id = ?, description = ?, time_based = ? WHERE id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, exercise.getName());
            if (exercise.getCategory() != null && exercise.getCategory().getId() != null) {
                pstmt.setLong(2, exercise.getCategory().getId());
            } else {
                pstmt.setNull(2, Types.BIGINT);
            }
            if (exercise.getDescription() != null) {
                pstmt.setString(3, exercise.getDescription());
            } else {
                pstmt.setNull(3, Types.VARCHAR);
            }
            pstmt.setBoolean(4, exercise.isTimeBased());
            pstmt.setLong(5, exercise.getId());
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean delete(long id) throws Exception {
        String sql = "DELETE FROM " + DatabaseHelper.getExercisesTableName() + " WHERE id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            return pstmt.executeUpdate() > 0;
        }
    }
}