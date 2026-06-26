/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sams.dao;

import com.sams.model.ClassSession;
import com.sams.util.DBConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author User
 */
public class ClassSessionDAO {

    private static final String SELECT_BASE =
            "SELECT cs.session_id, cs.subject_id, cs.lecturer_id, cs.session_date, "
          + "cs.start_time, cs.end_time, cs.venue, cs.notes, "
          + "sub.subject_name, sub.subject_code, "
          + "CONCAT(l.first_name, ' ', l.last_name) AS lecturer_name "
          + "FROM class_sessions cs "
          + "JOIN subjects sub ON cs.subject_id = sub.subject_id "
          + "JOIN lecturers l ON cs.lecturer_id = l.lecturer_id ";

    public int insert(ClassSession session) {
        String sql = "INSERT INTO class_sessions "
                   + "(subject_id, lecturer_id, session_date, start_time, end_time, venue, notes) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, session.getSubjectId());
            stmt.setInt(2, session.getLecturerId());
            stmt.setDate(3, Date.valueOf(session.getSessionDate()));
            stmt.setTime(4, Time.valueOf(session.getStartTime()));
            stmt.setTime(5, Time.valueOf(session.getEndTime()));
            stmt.setString(6, session.getVenue());
            stmt.setString(7, session.getNotes());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        return keys.getInt(1);
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Error inserting class session: " + e.getMessage());
            e.printStackTrace();
        }

        return -1;
    }

    public List<ClassSession> findAll() {
        List<ClassSession> sessions = new ArrayList<>();
        String sql = SELECT_BASE + "ORDER BY cs.session_date DESC, cs.start_time";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                sessions.add(mapRowToSession(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error fetching class sessions: " + e.getMessage());
            e.printStackTrace();
        }

        return sessions;
    }

    
    public List<ClassSession> findByLecturerId(int lecturerId) {
        List<ClassSession> sessions = new ArrayList<>();
        String sql = SELECT_BASE + "WHERE cs.lecturer_id = ? ORDER BY cs.session_date DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, lecturerId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    sessions.add(mapRowToSession(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error fetching sessions by lecturer: " + e.getMessage());
            e.printStackTrace();
        }

        return sessions;
    }

    public boolean update(ClassSession session) {
        String sql = "UPDATE class_sessions SET subject_id = ?, lecturer_id = ?, "
                   + "session_date = ?, start_time = ?, end_time = ?, venue = ?, notes = ? "
                   + "WHERE session_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, session.getSubjectId());
            stmt.setInt(2, session.getLecturerId());
            stmt.setDate(3, Date.valueOf(session.getSessionDate()));
            stmt.setTime(4, Time.valueOf(session.getStartTime()));
            stmt.setTime(5, Time.valueOf(session.getEndTime()));
            stmt.setString(6, session.getVenue());
            stmt.setString(7, session.getNotes());
            stmt.setInt(8, session.getSessionId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error updating class session: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    public boolean delete(int sessionId) {
        String sql = "DELETE FROM class_sessions WHERE session_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, sessionId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting class session: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    private ClassSession mapRowToSession(ResultSet rs) throws SQLException {
        ClassSession session = new ClassSession();
        session.setSessionId(rs.getInt("session_id"));
        session.setSubjectId(rs.getInt("subject_id"));
        session.setLecturerId(rs.getInt("lecturer_id"));
        session.setSessionDate(rs.getDate("session_date").toLocalDate());
        session.setStartTime(rs.getTime("start_time").toLocalTime());
        session.setEndTime(rs.getTime("end_time").toLocalTime());
        session.setVenue(rs.getString("venue"));
        session.setNotes(rs.getString("notes"));
        session.setSubjectName(rs.getString("subject_name"));
        session.setSubjectCode(rs.getString("subject_code"));
        session.setLecturerName(rs.getString("lecturer_name"));
        return session;
    }
}

