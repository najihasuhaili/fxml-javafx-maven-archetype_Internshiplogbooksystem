package com.example.logbooksystem.controller;

import com.example.logbooksystem.DBConnection;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

public class StudentProfileController {

    @FXML
    private Label lblStudentId;

    @FXML
    private Label lblStudentName;

    @FXML
    private Label lblLecturerName;

    @FXML
    private Label lblStudentEmail;

    @FXML
    private Label lblSession;

    @FXML
    private Label lblTitle;

    @FXML
    private Label lblCurrentPassword;

    @FXML
    private PasswordField txtNewPassword;

    private String currentStudentId;

    @FXML
    public void initialize() {
        loadStudentProfile();
    }

    private void loadStudentProfile() {
        currentStudentId = StudentDashboardController.loggedInStudentId;

        if (currentStudentId == null || currentStudentId.isEmpty()) {
            lblTitle.setText("No student is logged in.");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {

            String sql = """
                SELECT s.student_id, s.student_name, s.student_email, s.session, s.s_password,
                       l.lecturer_name
                FROM student s
                LEFT JOIN lecturer l ON s.lecturer_id = l.lecturer_id
                WHERE s.student_id = ?
            """;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, currentStudentId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                lblStudentId.setText(rs.getString("student_id"));
                lblStudentName.setText(rs.getString("student_name"));
                lblLecturerName.setText(rs.getString("lecturer_name"));
                lblStudentEmail.setText(rs.getString("student_email"));
                lblSession.setText(rs.getString("session"));
                lblCurrentPassword.setText(rs.getString("s_password"));
                lblTitle.setText("Student Profile");
            } else {
                lblTitle.setText("Student data not found.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            lblTitle.setText("Error loading student profile.");
        }
    }

    @FXML
    private void handleChangePassword(ActionEvent event) {
        String newPassword = txtNewPassword.getText().trim();

        if (newPassword.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please enter a new password.");
            return;
        }

        if (newPassword.length() < 4) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Password must be at least 4 characters.");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {

            String sql = "UPDATE student SET s_password = ? WHERE student_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, newPassword);
            ps.setString(2, currentStudentId);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                lblCurrentPassword.setText(newPassword);
                txtNewPassword.clear();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Password updated successfully.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to update password.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", e.getMessage());
        }
    }

    @FXML
    private void goBackToDashboard(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/logbooksystem/StudentDashboard.fxml"));
            Stage stage = (Stage) lblTitle.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Student Dashboard");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}