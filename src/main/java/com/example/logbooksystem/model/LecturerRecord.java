package com.example.logbooksystem.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class LecturerRecord {

    private final StringProperty lecturerId;
    private final StringProperty lecturerName;
    private final StringProperty lecturerEmail;

    public LecturerRecord(String lecturerId, String lecturerName, String lecturerEmail) {
        this.lecturerId = new SimpleStringProperty(lecturerId);
        this.lecturerName = new SimpleStringProperty(lecturerName);
        this.lecturerEmail = new SimpleStringProperty(lecturerEmail);
    }

    public String getLecturerId() {
        return lecturerId.get();
    }

    public StringProperty lecturerIdProperty() {
        return lecturerId;
    }

    public String getLecturerName() {
        return lecturerName.get();
    }

    public StringProperty lecturerNameProperty() {
        return lecturerName;
    }

    public String getLecturerEmail() {
        return lecturerEmail.get();
    }

    public StringProperty lecturerEmailProperty() {
        return lecturerEmail;
    }
}