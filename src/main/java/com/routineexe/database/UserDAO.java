package com.routineexe.database;

import com.routineexe.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public List<User> fetchAll() throws Exception {
        List<User> users = new ArrayList<>();
        String sql = "SELECT id, username, age, height, weight FROM " + DatabaseHelper.getUsersTableName() + " ORDER BY id DESC";
        try (Connection conn = DatabaseHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setUsername(rs.getString("username"));
                
                int age = rs.getInt("age");
                if (!rs.wasNull()) user.setAge(age);
                
                double height = rs.getDouble("height");
                if (!rs.wasNull()) user.setHeight(height);
                
                double weight = rs.getDouble("weight");
                if (!rs.wasNull()) user.setWeight(weight);
                
                users.add(user);
            }
        }
        return users;
    }

    public long insert(User user) throws Exception {
        String sql = "INSERT INTO " + DatabaseHelper.getUsersTableName() + " (username, age, height, weight) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, user.getUsername());
            
            if (user.getAge().isPresent()) {
                pstmt.setInt(2, user.getAge().get());
            } else {
                pstmt.setNull(2, Types.INTEGER);
            }
            
            if (user.getHeight().isPresent()) {
                pstmt.setDouble(3, user.getHeight().get());
            } else {
                pstmt.setNull(3, Types.REAL);
            }
            
            if (user.getWeight().isPresent()) {
                pstmt.setDouble(4, user.getWeight().get());
            } else {
                pstmt.setNull(4, Types.REAL);
            }
            
            pstmt.executeUpdate();
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        }
        throw new SQLException("Failed to insert user");
    }

    public boolean delete(long id) throws Exception {
        String sql = "DELETE FROM " + DatabaseHelper.getUsersTableName() + " WHERE id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            return pstmt.executeUpdate() > 0;
        }
    }
}