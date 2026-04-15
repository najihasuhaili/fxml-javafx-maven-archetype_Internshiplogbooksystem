package com.example.logbooksystem.controller;

import com.example.logbooksystem.DBConnection;
import com.example.logbooksystem.LecturerSession;
import com.example.logbooksystem.model.WeeklyLogbookEntry;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class LecturerLogbookReviewController implements Initializable {

    @FXML private Label lblStudent;

    @FXML private TableView<WeeklyLogbookEntry> tableLogbook;
    @FXML private TableColumn<WeeklyLogbookEntry, String> colDay;
    @FXML private TableColumn<WeeklyLogbookEntry, String> colDate;
    @FXML private TableColumn<WeeklyLogbookEntry, String> colActivity;
    @FXML private TableColumn<WeeklyLogbookEntry, String> colRemarks;
    @FXML private TableColumn<WeeklyLogbookEntry, String> colStatus;
    @FXML private TableColumn<WeeklyLogbookEntry, String> colComment;
    @FXML private TableColumn<WeeklyLogbookEntry, String> colSignature;

    @FXML private TextArea txtComment;
    @FXML private Label lblMessage;

    // CHECKBOX
    @FXML private CheckBox cbNeedImprovement;
    @FXML private CheckBox cbGood;
    @FXML private CheckBox cbExcellent;

    // RADIOBUTTON
    @FXML private RadioButton rbReviewed;
    @FXML private RadioButton rbPending;
    @FXML private RadioButton rbRejected;
    @FXML private ToggleGroup statusGroup;

    private int currentWeek = 1;
    private Connection conn;
    private ObservableList<WeeklyLogbookEntry> list;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        conn = DBConnection.getConnection();

        lblStudent.setText(
                "Student: "
                        + LecturerSession.selectedStudentName
                        + " ("
                        + LecturerSession.selectedStudentId
                        + ")"
        );

        colDay.setCellValueFactory(data -> data.getValue().dayNameProperty());
        colDate.setCellValueFactory(data -> data.getValue().logDateProperty());
        colActivity.setCellValueFactory(data -> data.getValue().activityProperty());
        colRemarks.setCellValueFactory(data -> data.getValue().remarksProperty());
        colStatus.setCellValueFactory(data -> data.getValue().statusProperty());
        colComment.setCellValueFactory(data -> data.getValue().lecturerCommentProperty());
        colSignature.setCellValueFactory(data -> data.getValue().lecturerSignatureProperty());

        list = FXCollections.observableArrayList();
        loadTable();

        // Bila klik row, auto fill comment & status lama
        tableLogbook.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                loadSelectedLogbookData(newVal);
            }
        });
    }

    private void loadTable() {
        list.clear();

        String sql = "SELECT * FROM weekly_logbook WHERE student_id=? AND week_no=?";

        try {
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, LecturerSession.selectedStudentId);
            pst.setInt(2, currentWeek);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                list.add(new WeeklyLogbookEntry(
                        rs.getInt("log_id"),
                        rs.getString("day_name"),
                        rs.getDate("log_date").toString(),
                        rs.getString("activity"),
                        rs.getString("remarks"),
                        rs.getString("status"),
                        rs.getString("lecturer_comment"),
                        rs.getString("lecturer_signature")
                ));
            }

            tableLogbook.setItems(list);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadSelectedLogbookData(WeeklyLogbookEntry selected) {
        String comment = selected.getLecturerComment();

        if (comment == null) comment = "";

        txtComment.setText(removeTags(comment));

        // reset checkbox dulu
        cbNeedImprovement.setSelected(false);
        cbGood.setSelected(false);
        cbExcellent.setSelected(false);

        // detect tag lama
        if (comment.contains("[Need Improvement]")) {
            cbNeedImprovement.setSelected(true);
        }
        if (comment.contains("[Good]")) {
            cbGood.setSelected(true);
        }
        if (comment.contains("[Excellent]")) {
            cbExcellent.setSelected(true);
        }

        // load radio status lama
        String status = selected.getStatus();
        if (status != null) {
            switch (status) {
                case "Reviewed":
                    rbReviewed.setSelected(true);
                    break;
                case "Pending":
                    rbPending.setSelected(true);
                    break;
                case "Rejected":
                    rbRejected.setSelected(true);
                    break;
            }
        }
    }

    private String removeTags(String comment) {
        return comment
                .replace("[Need Improvement]", "")
                .replace("[Good]", "")
                .replace("[Excellent]", "")
                .trim();
    }

@FXML
private void handleReview(ActionEvent event) {

    WeeklyLogbookEntry selected = tableLogbook.getSelectionModel().getSelectedItem();

    if (selected == null) {
        lblMessage.setText("Please select a row first.");
        return;
    }

    String finalComment = buildFinalComment();
    String finalStatus = getSelectedStatus();

    if (finalStatus == null) {
        lblMessage.setText("Please select review status.");
        return;
    }

    if (finalComment.isBlank()) {
        lblMessage.setText("Please enter comment or choose evaluation.");
        return;
    }

    String sql = "UPDATE weekly_logbook SET status=?, lecturer_comment=?, reviewed_by=?, lecturer_signature=? WHERE log_id=?";

    try {

        String signature = "✔ Signed by Lecturer: "
                + LecturerDashboardController.loggedInLecturerId
                + "\nDate: "
                + java.time.LocalDateTime.now();

        PreparedStatement pst = conn.prepareStatement(sql);

        pst.setString(1, finalStatus);
        pst.setString(2, finalComment);
        pst.setString(3, LecturerDashboardController.loggedInLecturerId);
        pst.setString(4, signature);
        pst.setInt(5, selected.getLogId());

        pst.executeUpdate();

        lblMessage.setStyle("-fx-text-fill: green;");
        lblMessage.setText("Review submitted successfully.");

        loadTable();
        clearForm();

    } catch (Exception e) {
        e.printStackTrace();
        lblMessage.setStyle("-fx-text-fill: red;");
        lblMessage.setText("Failed to submit review.");
    }
}

    private String buildFinalComment() {
        StringBuilder tags = new StringBuilder();

        if (cbNeedImprovement.isSelected()) {
            tags.append("[Need Improvement] ");
        }
        if (cbGood.isSelected()) {
            tags.append("[Good] ");
        }
        if (cbExcellent.isSelected()) {
            tags.append("[Excellent] ");
        }

        String commentText = txtComment.getText().trim();

        return (tags.toString() + commentText).trim();
    }

    private String getSelectedStatus() {
        if (rbReviewed.isSelected()) return "Reviewed";
        if (rbPending.isSelected()) return "Pending";
        if (rbRejected.isSelected()) return "Rejected";
        return null;
    }

    private void clearForm() {
        txtComment.clear();
        cbNeedImprovement.setSelected(false);
        cbGood.setSelected(false);
        cbExcellent.setSelected(false);
        statusGroup.selectToggle(null);
    }

    private void changeWeek(int week) {
        currentWeek = week;
        loadTable();
        clearForm();
        lblMessage.setText("");
    }

    @FXML private void week1(ActionEvent e) { changeWeek(1); }
    @FXML private void week2(ActionEvent e) { changeWeek(2); }
    @FXML private void week3(ActionEvent e) { changeWeek(3); }
    @FXML private void week4(ActionEvent e) { changeWeek(4); }
    @FXML private void week5(ActionEvent e) { changeWeek(5); }
    @FXML private void week6(ActionEvent e) { changeWeek(6); }
    @FXML private void week7(ActionEvent e) { changeWeek(7); }
    @FXML private void week8(ActionEvent e) { changeWeek(8); }
    @FXML private void week9(ActionEvent e) { changeWeek(9); }
    @FXML private void week10(ActionEvent e) { changeWeek(10); }
    @FXML private void week11(ActionEvent e) { changeWeek(11); }
    @FXML private void week12(ActionEvent e) { changeWeek(12); }
    @FXML private void week13(ActionEvent e) { changeWeek(13); }
    @FXML private void week14(ActionEvent e) { changeWeek(14); }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/com/example/logbooksystem/LecturerDashboard.fxml")
            );

            Stage stage = (Stage) lblStudent.getScene().getWindow();
            stage.setScene(new Scene(root));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}