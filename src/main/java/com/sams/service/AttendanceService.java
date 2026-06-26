/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sams.service;

import com.sams.dao.AttendanceDAO;
import com.sams.model.Attendance;

import java.util.List;

/**
 *
 * @author User
 */
public class AttendanceService {

    private final AttendanceDAO attendanceDAO;

    public AttendanceService() {
        this.attendanceDAO = new AttendanceDAO();
    }

    
    public List<Attendance> getAttendanceForSession(int sessionId) {
        List<Attendance> existing = attendanceDAO.getAttendanceForSession(sessionId);

        if (!existing.isEmpty()) {
            return existing; 
        }

        
        return attendanceDAO.getStudentsForSession(sessionId);
    }

    public String saveAttendance(List<Attendance> attendanceList) {
        if (attendanceList == null || attendanceList.isEmpty()) {
            return "No attendance records to save.";
        }

        boolean success = attendanceDAO.saveAllAttendance(attendanceList);
        return success ? "SUCCESS" : "Some records failed to save. Please try again.";
    }
}