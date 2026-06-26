/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sams.dao;

import com.sams.model.Subject;
import com.sams.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author User
 */
public class SubjectDAO {

    
    public List<Subject> findAll() {
        List<Subject> subjects = new ArrayList<>();
        String sql = "SELECT s.subject_id, s.course_id, s.subject_code, s.subject_name, "
                   + "s.credits, s.semester, s.is_active, c.course_name "
                   + "FROM subjects s "
                   + "JOIN courses c ON s.course_id = c.course_id "
                   + "WHERE s.is_active = 1 "
                   + "ORDER BY c.course_name, s.semester, s.subject_name";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                subjects.add(mapRowToSubject(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error fetching subjects: " + e.getMessage());
            e.printStackTrace();
        }

        return subjects;
    }

    private Subject mapRowToSubject(ResultSet rs) throws SQLException {
        Subject subject = new Subject();
        subject.setSubjectId(rs.getInt("subject_id"));
        subject.setCourseId(rs.getInt("course_id"));
        subject.setSubjectCode(rs.getString("subject_code"));
        subject.setSubjectName(rs.getString("subject_name"));
        subject.setCredits(rs.getInt("credits"));
        subject.setSemester(rs.getInt("semester"));
        subject.setActive(rs.getBoolean("is_active"));
        subject.setCourseName(rs.getString("course_name"));
        return subject;
    }
}
