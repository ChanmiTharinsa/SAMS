/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sams.dao;

import com.sams.model.Course;
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
public class CourseDAO {

    
    public int insert(Course course) {
        String sql = "INSERT INTO courses (course_code, course_name, duration_years, description, is_active) "
                   + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
            
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, course.getCourseCode());
            stmt.setString(2, course.getCourseName());
            stmt.setInt(3, course.getDurationYears());
            stmt.setString(4, course.getDescription());
            stmt.setBoolean(5, course.isActive());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        return keys.getInt(1); // the new course_id
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Error inserting course: " + e.getMessage());
            e.printStackTrace();
        }

        return -1;
    }

   
    public List<Course> findAll() {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM courses WHERE is_active = 1 ORDER BY course_name";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                courses.add(mapRowToCourse(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error fetching courses: " + e.getMessage());
            e.printStackTrace();
        }

        return courses;
    }

    
    public Course findById(int courseId) {
        String sql = "SELECT * FROM courses WHERE course_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, courseId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToCourse(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error fetching course by id: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    
    public boolean existsByCourseCode(String courseCode) {
        String sql = "SELECT 1 FROM courses WHERE course_code = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, courseCode);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.err.println("Error checking course code: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    
    public boolean update(Course course) {
        String sql = "UPDATE courses SET course_code = ?, course_name = ?, "
                   + "duration_years = ?, description = ?, is_active = ? "
                   + "WHERE course_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, course.getCourseCode());
            stmt.setString(2, course.getCourseName());
            stmt.setInt(3, course.getDurationYears());
            stmt.setString(4, course.getDescription());
            stmt.setBoolean(5, course.isActive());
            stmt.setInt(6, course.getCourseId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error updating course: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    
    public boolean delete(int courseId) {
        String sql = "DELETE FROM courses WHERE course_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, courseId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting course (it may have linked subjects/students): "
                    + e.getMessage());
        }

        return false;
    }

    
    public boolean softDelete(int courseId) {
        String sql = "UPDATE courses SET is_active = 0 WHERE course_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, courseId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error soft-deleting course: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    
    private Course mapRowToCourse(ResultSet rs) throws SQLException {
        Course course = new Course();
        course.setCourseId(rs.getInt("course_id"));
        course.setCourseCode(rs.getString("course_code"));
        course.setCourseName(rs.getString("course_name"));
        course.setDurationYears(rs.getInt("duration_years"));
        course.setDescription(rs.getString("description"));
        course.setActive(rs.getBoolean("is_active"));
        return course;
    }
}