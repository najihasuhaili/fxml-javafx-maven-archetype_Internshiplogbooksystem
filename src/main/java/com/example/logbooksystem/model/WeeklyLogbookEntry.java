package com.example.logbooksystem.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class WeeklyLogbookEntry {

    private final SimpleIntegerProperty logId;
    private final SimpleStringProperty dayName;
    private final SimpleStringProperty logDate;
    private final SimpleStringProperty activity;
    private final SimpleStringProperty remarks;
    private final SimpleStringProperty status;
    private final SimpleStringProperty lecturerComment;
    private final SimpleStringProperty lecturerSignature; // ✅ ADD

    // UPDATED CONSTRUCTOR
    public WeeklyLogbookEntry(int logId, String dayName, String logDate,
                              String activity, String remarks, String status,
                              String lecturerComment, String lecturerSignature) {

        this.logId = new SimpleIntegerProperty(logId);
        this.dayName = new SimpleStringProperty(dayName);
        this.logDate = new SimpleStringProperty(logDate);
        this.activity = new SimpleStringProperty(activity);
        this.remarks = new SimpleStringProperty(remarks);
        this.status = new SimpleStringProperty(status);
        this.lecturerComment = new SimpleStringProperty(lecturerComment);
        this.lecturerSignature = new SimpleStringProperty(lecturerSignature); // ✅ ADD
    }

    public int getLogId() { return logId.get(); }
    public SimpleIntegerProperty logIdProperty() { return logId; }

    public String getDayName() { return dayName.get(); }
    public SimpleStringProperty dayNameProperty() { return dayName; }

    public String getLogDate() { return logDate.get(); }
    public SimpleStringProperty logDateProperty() { return logDate; }

    public String getActivity() { return activity.get(); }
    public SimpleStringProperty activityProperty() { return activity; }

    public String getRemarks() { return remarks.get(); }
    public SimpleStringProperty remarksProperty() { return remarks; }

    public String getStatus() { return status.get(); }
    public SimpleStringProperty statusProperty() { return status; }

    public String getLecturerComment() { return lecturerComment.get(); }
    public SimpleStringProperty lecturerCommentProperty() { return lecturerComment; }

    // ADD GETTER FOR SIGNATURE
    public String getLecturerSignature() { return lecturerSignature.get() == null ? "-" : lecturerSignature.get(); }
    public SimpleStringProperty lecturerSignatureProperty() { return lecturerSignature; }
}