/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sams.model;

/**
 *
 * @author User
 */
public class Course {

    private int courseId;
    private String courseCode;
    private String courseName;
    private int durationYears;
    private String description;
    private boolean active;

    public Course() {
    }

    public Course(int courseId, String courseCode, String courseName,
                  int durationYears, String description, boolean active) {
        this.courseId = courseId;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.durationYears = durationYears;
        this.description = description;
        this.active = active;
    }

    
    public Course(String courseCode, String courseName, int durationYears, String description) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.durationYears = durationYears;
        this.description = description;
        this.active = true;
    }

    

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getDurationYears() {
        return durationYears;
    }

    public void setDurationYears(int durationYears) {
        this.durationYears = durationYears;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return courseCode + " - " + courseName;
    }
}

