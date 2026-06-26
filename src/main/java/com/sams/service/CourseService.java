/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sams.service;

import com.sams.dao.CourseDAO;
import com.sams.model.Course;

import java.util.List;

/**
 *
 * @author User
 */
public class CourseService {

    private final CourseDAO courseDAO;

    public CourseService() {
        this.courseDAO = new CourseDAO();
    }

    
    public List<Course> getAllCourses() {
        return courseDAO.findAll();
    }

    public Course getCourseById(int courseId) {
        return courseDAO.findById(courseId);
    }

    
    public String addCourse(Course course) {
        String validationError = validate(course);
        if (validationError != null) {
            return validationError;
        }

        if (courseDAO.existsByCourseCode(course.getCourseCode())) {
            return "A course with this code already exists.";
        }

        int newId = courseDAO.insert(course);
        if (newId == -1) {
            return "Failed to save course. Please try again.";
        }

        return "SUCCESS";
    }

    
    public String updateCourse(Course course) {
        String validationError = validate(course);
        if (validationError != null) {
            return validationError;
        }

        boolean updated = courseDAO.update(course);
        if (!updated) {
            return "Failed to update course. Please try again.";
        }

        return "SUCCESS";
    }

    
    public String deleteCourse(int courseId) {
        boolean deleted = courseDAO.softDelete(courseId);
        if (!deleted) {
            return "Failed to delete course.";
        }
        return "SUCCESS";
    }

    
    private String validate(Course course) {
        if (course.getCourseCode() == null || course.getCourseCode().isBlank()) {
            return "Course code is required.";
        }
        if (course.getCourseCode().length() > 20) {
            return "Course code must be 20 characters or fewer.";
        }
        if (course.getCourseName() == null || course.getCourseName().isBlank()) {
            return "Course name is required.";
        }
        if (course.getDurationYears() <= 0 || course.getDurationYears() > 10) {
            return "Duration must be between 1 and 10 years.";
        }
        return null; 
    }
}
