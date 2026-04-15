package com.example.logbooksystem;

import com.example.logbooksystem.model.Lecturer;
import com.example.logbooksystem.model.Student;

public class Session {

    private static Lecturer currentLecturer;
    private static Student currentStudent;

    public static void setCurrentLecturer(Lecturer lecturer) {
        currentLecturer = lecturer;
        currentStudent = null;
    }

    public static Lecturer getCurrentLecturer() {
        return currentLecturer;
    }

    public static void setCurrentStudent(Student student) {
        currentStudent = student;
        currentLecturer = null;
    }

    public static Student getCurrentStudent() {
        return currentStudent;
    }

    public static void clear() {
        currentLecturer = null;
        currentStudent = null;
    }
}