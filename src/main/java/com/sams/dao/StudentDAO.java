/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sams.dao;

import com.sams.model.Student;
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
public class StudentDAO {

    private static final String SELECT_BASE =
            "SELECT s.student_id, s.reg_number, s.first_name, s.last_name, s.email, "
          + "s.phone, s.course_id, c.course_name, s.enrolled_year, s.is_active "
          + "FROM students s "
          + "JOIN courses c ON s.course_id = c.course_id ";

    
    public int insert(Student student) {
        String sql = "INSERT INTO students "
                   + "(reg_number, first_name, last_name, email, phone, course_id, enrolled_year, is_active) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, student.getRegNumber());
            stmt.setString(2, student.getFirstName());
            stmt.setString(3, student.getLastName());
            stmt.setString(4, student.getEmail());
            stmt.setString(5, student.getPhone());
            stmt.setInt(6, student.getCourseId());
            stmt.setInt(7, student.getEnrolledYear());
            stmt.setBoolean(8, student.isActive());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        return keys.getInt(1);
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Error inserting student: " + e.getMessage());
            e.printStackTrace();
        }

        return -1;
    }

    
    public List<Student> findAll() {
        List<Student> students = new ArrayList<>();
        String sql = SELECT_BASE + "WHERE s.is_active = 1 ORDER BY s.reg_number";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                students.add(mapRowToStudent(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error fetching students: " + e.getMessage());
            e.printStackTrace();
        }

        return students;
    }

    public Student findById(int studentId) {
        String sql = SELECT_BASE + "WHERE s.student_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToStudent(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error fetching student by id: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    public boolean existsByRegNumber(String regNumber) {
        String sql = "SELECT 1 FROM students WHERE reg_number = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, regNumber);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.err.println("Error checking reg number: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    
    public boolean update(Student student) {
        String sql = "UPDATE students SET reg_number = ?, first_name = ?, last_name = ?, "
                   + "email = ?, phone = ?, course_id = ?, enrolled_year = ?, is_active = ? "
                   + "WHERE student_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, student.getRegNumber());
            stmt.setString(2, student.getFirstName());
            stmt.setString(3, student.getLastName());
            stmt.setString(4, student.getEmail());
            stmt.setString(5, student.getPhone());
            stmt.setInt(6, student.getCourseId());
            stmt.setInt(7, student.getEnrolledYear());
            stmt.setBoolean(8, student.isActive());
            stmt.setInt(9, student.getStudentId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error updating student: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    
    public boolean softDelete(int studentId) {
        String sql = "UPDATE students SET is_active = 0 WHERE student_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error soft-deleting student: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    private Student mapRowToStudent(ResultSet rs) throws SQLException {
        Student student = new Student();
        student.setStudentId(rs.getInt("student_id"));
        student.setRegNumber(rs.getString("reg_number"));
        student.setFirstName(rs.getString("first_name"));
        student.setLastName(rs.getString("last_name"));
        student.setEmail(rs.getString("email"));
        student.setPhone(rs.getString("phone"));
        student.setCourseId(rs.getInt("course_id"));
        student.setCourseName(rs.getString("course_name"));
        student.setEnrolledYear(rs.getInt("enrolled_year"));
        student.setActive(rs.getBoolean("is_active"));
        return student;
    }
}

