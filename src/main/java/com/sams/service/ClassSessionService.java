/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sams.service;

import com.sams.dao.ClassSessionDAO;
import com.sams.dao.SubjectDAO;
import com.sams.model.ClassSession;
import com.sams.model.Subject;

import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author User
 */
public class ClassSessionService {

    private final ClassSessionDAO sessionDAO;
    private final SubjectDAO subjectDAO;

    public ClassSessionService() {
        this.sessionDAO = new ClassSessionDAO();
        this.subjectDAO = new SubjectDAO();
    }

    public List<ClassSession> getAllSessions() {
        return sessionDAO.findAll();
    }

    public List<ClassSession> getSessionsByLecturer(int lecturerId) {
        return sessionDAO.findByLecturerId(lecturerId);
    }

    public List<Subject> getAllSubjects() {
        return subjectDAO.findAll();
    }

    public String addSession(ClassSession session) {
        String error = validate(session);
        if (error != null) return error;

        int newId = sessionDAO.insert(session);
        return newId == -1 ? "Failed to save session. Please try again." : "SUCCESS";
    }

    public String updateSession(ClassSession session) {
        String error = validate(session);
        if (error != null) return error;

        return sessionDAO.update(session) ? "SUCCESS" : "Failed to update session.";
    }

    public String deleteSession(int sessionId) {
        return sessionDAO.delete(sessionId) ? "SUCCESS" : "Failed to delete session.";
    }

    private String validate(ClassSession session) {
        if (session.getSubjectId() <= 0) {
            return "Please select a subject.";
        }
        if (session.getLecturerId() <= 0) {
            return "Please select a lecturer.";
        }
        if (session.getSessionDate() == null) {
            return "Please select a session date.";
        }
        if (session.getSessionDate().isBefore(LocalDate.of(2000, 1, 1))) {
            return "Session date looks invalid.";
        }
        if (session.getStartTime() == null) {
            return "Start time is required (format HH:MM).";
        }
        if (session.getEndTime() == null) {
            return "End time is required (format HH:MM).";
        }
        if (!session.getEndTime().isAfter(session.getStartTime())) {
            return "End time must be after start time.";
        }
        if (session.getVenue() == null || session.getVenue().isBlank()) {
            return "Venue is required.";
        }
        return null;
    }
}
