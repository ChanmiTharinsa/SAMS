/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sams.dao;

import com.sams.model.Attendance;
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
public class AttendanceDAO {

    
    public List<Attendance> getAttendanceForSession(int sessionId) {
        List<Attendance> list = new ArrayList<>();
        String sql =
                "SELECT a.attendance_id, a.session_id, a.student_id, a.status, a.remarks, "
              + "CONCAT(s.first_name, ' ', s.last_name) AS student_name, s.reg_number "
              + "FROM attendance a "
              + "JOIN students s ON a.student_id = s.student_id "
              + "WHERE a.session_id = ? "
              + "ORDER BY s.reg_number";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, sessionId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Attendance a = new Attendance();
                    a.setAttendanceId(rs.getInt("attendance_id"));
                    a.setSessionId(rs.getInt("session_id"));
                    a.setStudentId(rs.getInt("student_id"));
                    a.setStudentName(rs.getString("student_name"));
                    a.setRegNumber(rs.getString("reg_number"));
                    a.setStatus(rs.getString("status"));
                    a.setRemarks(rs.getString("remarks"));
                    list.add(a);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error fetching attendance: " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }

    
    public List<Attendance> getStudentsForSession(int sessionId) {
        List<Attendance> list = new ArrayList<>();
        String sql =
                "SELECT s.student_id, CONCAT(s.first_name, ' ', s.last_name) AS student_name, "
              + "s.reg_number "
              + "FROM students s "
              + "JOIN courses c ON s.course_id = c.course_id "
              + "JOIN subjects sub ON sub.course_id = c.course_id "
              + "JOIN class_sessions cs ON cs.subject_id = sub.subject_id "
              + "WHERE cs.session_id = ? AND s.is_active = 1 "
              + "ORDER BY s.reg_number";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, sessionId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Attendance a = new Attendance();
                    a.setSessionId(sessionId);
                    a.setStudentId(rs.getInt("student_id"));
                    a.setStudentName(rs.getString("student_name"));
                    a.setRegNumber(rs.getString("reg_number"));
                    a.setStatus("ABSENT"); // default status
                    list.add(a);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error fetching students for session: " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }

    
    public boolean saveAttendance(Attendance attendance) {
        String sql =
                "INSERT INTO attendance (session_id, student_id, status, remarks) "
              + "VALUES (?, ?, ?, ?) "
              + "ON DUPLICATE KEY UPDATE status = VALUES(status), remarks = VALUES(remarks)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, attendance.getSessionId());
            stmt.setInt(2, attendance.getStudentId());
            stmt.setString(3, attendance.getStatus());
            stmt.setString(4, attendance.getRemarks() != null ? attendance.getRemarks() : "");

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error saving attendance: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    
    public boolean saveAllAttendance(List<Attendance> attendanceList) {
        boolean allSuccess = true;
        for (Attendance a : attendanceList) {
            if (!saveAttendance(a)) {
                allSuccess = false;
            }
        }
        return allSuccess;
    }
}

