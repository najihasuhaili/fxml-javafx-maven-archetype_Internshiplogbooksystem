package com.example.logbooksystem.model;

import javafx.beans.property.SimpleStringProperty;

public class StudentRecord {

    private final SimpleStringProperty studentId;
    private final SimpleStringProperty studentName;
    private final SimpleStringProperty studentEmail;
    private final SimpleStringProperty session;
    private final SimpleStringProperty lecturerId;

    public StudentRecord(String studentId, String studentName, String studentEmail,
                         String session, String lecturerId) {

        this.studentId = new SimpleStringProperty(studentId);
        this.studentName = new SimpleStringProperty(studentName);
        this.studentEmail = new SimpleStringProperty(studentEmail);
        this.session = new SimpleStringProperty(session);
        this.lecturerId = new SimpleStringProperty(lecturerId);
    }

    public String getStudentId() {
        return studentId.get();
    }

    public SimpleStringProperty studentIdProperty() {
        return studentId;
    }

    public String getStudentName() {
        return studentName.get();
    }

    public SimpleStringProperty studentNameProperty() {
        return studentName;
    }

    public String getStudentEmail() {
        return studentEmail.get();
    }

    public SimpleStringProperty studentEmailProperty() {
        return studentEmail;
    }

    public String getSession() {
        return session.get();
    }

    public SimpleStringProperty sessionProperty() {
        return session;
    }

    public String getLecturerId() {
        return lecturerId.get();
    }

    public SimpleStringProperty lecturerIdProperty() {
        return lecturerId;
    }
}