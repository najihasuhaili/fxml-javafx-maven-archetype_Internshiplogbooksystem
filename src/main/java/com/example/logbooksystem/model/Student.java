package com.example.logbooksystem.model;

public class Student {

    private String studentId;
    private String studentName;
    private String lecturerId;
    private String studentEmail;
    private String password;
    private String session;

    public Student(String studentId, String studentName, String lecturerId,
                   String studentEmail, String password, String session) {

        this.studentId = studentId;
        this.studentName = studentName;
        this.lecturerId = lecturerId;
        this.studentEmail = studentEmail;
        this.password = password;
        this.session = session;
    }

    public String getStudentId() { return studentId; }
    public String getStudentName() { return studentName; }
    public String getLecturerId() { return lecturerId; }
    public String getStudentEmail() { return studentEmail; }
    public String getPassword() { return password; }
    public String getSession() { return session; }
}