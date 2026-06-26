/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sams.service;

import com.sams.dao.ReportDAO;
import com.sams.dao.ReportDAO.ReportRecord;

import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author User
 */
public class ReportService {

    private final ReportDAO reportDAO;

    public ReportService() {
        this.reportDAO = new ReportDAO();
    }

    
    public List<ReportRecord> generateReport(
            Integer studentId, Integer subjectId,
            LocalDate fromDate, LocalDate toDate) {

        
        if (fromDate != null && toDate != null && fromDate.isAfter(toDate)) {
            return List.of(); 
        }

        return reportDAO.getAttendanceReport(studentId, subjectId, fromDate, toDate);
    }
}

