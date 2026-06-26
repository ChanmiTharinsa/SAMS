/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sams.dao;

import com.sams.model.Lecturer;
import com.sams.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author User
 */
public class LecturerDAO {

    private static final String SELECT_BASE =
            "SELECT l.lecturer_id, l.user_id, l.employee_code, l.first_name, l.last_name, "
          + "l.email, l.phone, l.department, l.is_active, u.username "
          + "FROM lecturers l "
          + "JOIN users u ON l.user_id = u.user_id ";

    /**     
     * @param lecturer the lecturer profile data to insert
     * @param username desired login username
     * @param password desired login password (plain text - see AuthService note on hashing for production use)
     */
    public int insert(Lecturer lecturer, String username, String password) {
        String insertUserSql =
                "INSERT INTO users (username, password, role, full_name, email) "
              + "VALUES (?, ?, 'LECTURER', ?, ?)";

        String insertLecturerSql =
                "INSERT INTO lecturers (user_id, employee_code, first_name, last_name, email, phone, department, is_active) "
              + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // start transaction

            int newUserId;

            
            try (PreparedStatement userStmt = conn.prepareStatement(
                    insertUserSql, Statement.RETURN_GENERATED_KEYS)) {

                userStmt.setString(1, username);
                userStmt.setString(2, password);
                userStmt.setString(3, lecturer.getFullName());
                userStmt.setString(4, lecturer.getEmail());
                userStmt.executeUpdate();

                try (ResultSet keys = userStmt.getGeneratedKeys()) {
                    if (!keys.next()) {
                        conn.rollback();
                        return -1;
                    }
                    newUserId = keys.getInt(1);
                }
            }

            
            int newLecturerId;
            try (PreparedStatement lecturerStmt = conn.prepareStatement(
                    insertLecturerSql, Statement.RETURN_GENERATED_KEYS)) {

                lecturerStmt.setInt(1, newUserId);
                lecturerStmt.setString(2, lecturer.getEmployeeCode());
                lecturerStmt.setString(3, lecturer.getFirstName());
                lecturerStmt.setString(4, lecturer.getLastName());
                lecturerStmt.setString(5, lecturer.getEmail());
                lecturerStmt.setString(6, lecturer.getPhone());
                lecturerStmt.setString(7, lecturer.getDepartment());
                lecturerStmt.setBoolean(8, true);
                lecturerStmt.executeUpdate();

                try (ResultSet keys = lecturerStmt.getGeneratedKeys()) {
                    if (!keys.next()) {
                        conn.rollback();
                        return -1;
                    }
                    newLecturerId = keys.getInt(1);
                }
            }

            conn.commit();
            return newLecturerId;

        } catch (SQLException e) {
            System.err.println("Error inserting lecturer (rolling back): " + e.getMessage());
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            return -1;

        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true); 
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Lecturer> findAll() {
        List<Lecturer> lecturers = new ArrayList<>();
        String sql = SELECT_BASE + "WHERE l.is_active = 1 ORDER BY l.first_name";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lecturers.add(mapRowToLecturer(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error fetching lecturers: " + e.getMessage());
            e.printStackTrace();
        }

        return lecturers;
    }

    public Lecturer findById(int lecturerId) {
        String sql = SELECT_BASE + "WHERE l.lecturer_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, lecturerId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToLecturer(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error fetching lecturer by id: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    public boolean existsByEmployeeCode(String employeeCode) {
        String sql = "SELECT 1 FROM lecturers WHERE employee_code = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, employeeCode);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.err.println("Error checking employee code: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    
    public boolean update(Lecturer lecturer) {
        String sql = "UPDATE lecturers SET employee_code = ?, first_name = ?, last_name = ?, "
                   + "email = ?, phone = ?, department = ?, is_active = ? "
                   + "WHERE lecturer_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, lecturer.getEmployeeCode());
            stmt.setString(2, lecturer.getFirstName());
            stmt.setString(3, lecturer.getLastName());
            stmt.setString(4, lecturer.getEmail());
            stmt.setString(5, lecturer.getPhone());
            stmt.setString(6, lecturer.getDepartment());
            stmt.setBoolean(7, lecturer.isActive());
            stmt.setInt(8, lecturer.getLecturerId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error updating lecturer: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    
    public boolean softDelete(int lecturerId, int userId) {
        String deactivateLecturerSql = "UPDATE lecturers SET is_active = 0 WHERE lecturer_id = ?";
        String deactivateUserSql = "UPDATE users SET is_active = 0 WHERE user_id = ?";

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement stmt1 = conn.prepareStatement(deactivateLecturerSql)) {
                stmt1.setInt(1, lecturerId);
                stmt1.executeUpdate();
            }

            try (PreparedStatement stmt2 = conn.prepareStatement(deactivateUserSql)) {
                stmt2.setInt(1, userId);
                stmt2.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            System.err.println("Error deactivating lecturer: " + e.getMessage());
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            return false;

        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private Lecturer mapRowToLecturer(ResultSet rs) throws SQLException {
        Lecturer lecturer = new Lecturer();
        lecturer.setLecturerId(rs.getInt("lecturer_id"));
        lecturer.setUserId(rs.getInt("user_id"));
        lecturer.setEmployeeCode(rs.getString("employee_code"));
        lecturer.setFirstName(rs.getString("first_name"));
        lecturer.setLastName(rs.getString("last_name"));
        lecturer.setEmail(rs.getString("email"));
        lecturer.setPhone(rs.getString("phone"));
        lecturer.setDepartment(rs.getString("department"));
        lecturer.setActive(rs.getBoolean("is_active"));
        lecturer.setUsername(rs.getString("username"));
        return lecturer;
    }
}
