/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sams.dao;

import com.sams.model.Attendance;
import com.sams.util.DBConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author User
 */
public class ReportDAO {

    /**
     * @param studentId  filter by student (null = all students)
     * @param subjectId  filter by subject (null = all subjects)
     * @param fromDate   start of date range (null = no lower bound)
     * @param toDate     end of date range (null = no upper bound)
     */
    public List<ReportRecord> getAttendanceReport(
            Integer studentId, Integer subjectId,
            LocalDate fromDate, LocalDate toDate) {

        List<ReportRecord> records = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
                "SELECT a.attendance_id, d.session_date, d.subject_code, d.subject_name, "
              + "d.student_name, d.reg_number, d.course_name, d.status, "
              + "d.lecturer_name, d.start_time, d.venue "
              + "FROM vw_attendance_detail d "
              + "JOIN attendance a ON a.attendance_id = d.attendance_id "
              + "WHERE 1=1 "
        );

        List<Object> params = new ArrayList<>();

        if (studentId != null) {
            sql.append("AND d.reg_number = (SELECT reg_number FROM students WHERE student_id = ?) ");
            params.add(studentId);
        }
        if (subjectId != null) {
            sql.append("AND d.subject_code = (SELECT subject_code FROM subjects WHERE subject_id = ?) ");
            params.add(subjectId);
        }
        if (fromDate != null) {
            sql.append("AND d.session_date >= ? ");
            params.add(Date.valueOf(fromDate));
        }
        if (toDate != null) {
            sql.append("AND d.session_date <= ? ");
            params.add(Date.valueOf(toDate));
        }

        sql.append("ORDER BY d.session_date DESC, d.reg_number");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ReportRecord record = new ReportRecord();
                    record.sessionDate = rs.getString("session_date");
                    record.subjectCode = rs.getString("subject_code");
                    record.subjectName = rs.getString("subject_name");
                    record.studentName = rs.getString("student_name");
                    record.regNumber   = rs.getString("reg_number");
                    record.courseName  = rs.getString("course_name");
                    record.status      = rs.getString("status");
                    record.lecturerName= rs.getString("lecturer_name");
                    records.add(record);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error fetching report: " + e.getMessage());
            e.printStackTrace();
        }

        return records;
    }

   
    public static class ReportRecord {
        public String sessionDate;
        public String subjectCode;
        public String subjectName;
        public String studentName;
        public String regNumber;
        public String courseName;
        public String status;
        public String lecturerName;

        public String getSessionDate()  { return sessionDate; }
        public String getSubjectCode()  { return subjectCode; }
        public String getSubjectName()  { return subjectName; }
        public String getStudentName()  { return studentName; }
        public String getRegNumber()    { return regNumber; }
        public String getCourseName()   { return courseName; }
        public String getStatus()       { return status; }
        public String getLecturerName() { return lecturerName; }
    }
}

