/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sams.model;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 *
 * @author User
 */
public class ClassSession {

    private int sessionId;
    private int subjectId;
    private int lecturerId;
    private LocalDate sessionDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String venue;
    private String notes;

    
    private String subjectName;
    private String subjectCode;
    private String lecturerName;

    public ClassSession() {
    }

    public int getSessionId() { return sessionId; }
    public void setSessionId(int sessionId) { this.sessionId = sessionId; }

    public int getSubjectId() { return subjectId; }
    public void setSubjectId(int subjectId) { this.subjectId = subjectId; }

    public int getLecturerId() { return lecturerId; }
    public void setLecturerId(int lecturerId) { this.lecturerId = lecturerId; }

    public LocalDate getSessionDate() { return sessionDate; }
    public void setSessionDate(LocalDate sessionDate) { this.sessionDate = sessionDate; }

    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }

    public String getVenue() { return venue; }
    public void setVenue(String venue) { this.venue = venue; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }

    public String getSubjectCode() { return subjectCode; }
    public void setSubjectCode(String subjectCode) { this.subjectCode = subjectCode; }

    public String getLecturerName() { return lecturerName; }
    public void setLecturerName(String lecturerName) { this.lecturerName = lecturerName; }

    
    public String getSessionDateStr() {
        return sessionDate != null ? sessionDate.toString() : "";
    }

    public String getStartTimeStr() {
        return startTime != null ? startTime.toString() : "";
    }

    public String getEndTimeStr() {
        return endTime != null ? endTime.toString() : "";
    }
}

