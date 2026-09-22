package com.routineexe.database;

import com.routineexe.model.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    public List<Category> fetchAll() throws Exception {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT id, name FROM " + DatabaseHelper.getCategoriesTableName() + " ORDER BY id DESC";
        try (Connection conn = DatabaseHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Category category = new Category();
                category.setId(rs.getLong("id"));
                category.setName(rs.getString("name"));
                categories.add(category);
            }
        }
        return categories;
    }

    public long insert(Category category) throws Exception {
        String sql = "INSERT INTO " + DatabaseHelper.getCategoriesTableName() + " (name) VALUES (?)";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, category.getName());
            pstmt.executeUpdate();
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        }
        throw new SQLException("Failed to insert category");
    }

    public boolean delete(long id) throws Exception {
        String sql = "DELETE FROM " + DatabaseHelper.getCategoriesTableName() + " WHERE id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            return pstmt.executeUpdate() > 0;
        }
    }
}