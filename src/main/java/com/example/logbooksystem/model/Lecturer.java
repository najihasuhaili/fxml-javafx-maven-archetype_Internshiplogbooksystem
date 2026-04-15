package com.example.logbooksystem.model;

public class Lecturer {

    private String lecturerId;
    private String lecturerName;
    private String lecturerEmail;
    private String password;

    public Lecturer(String lecturerId, String lecturerName, String lecturerEmail, String password) {
        this.lecturerId = lecturerId;
        this.lecturerName = lecturerName;
        this.lecturerEmail = lecturerEmail;
        this.password = password;
    }

    public String getLecturerId() {
        return lecturerId;
    }

    public String getLecturerName() {
        return lecturerName;
    }

    public String getLecturerEmail() {
        return lecturerEmail;
    }

    public String getPassword() {
        return password;
    }
}