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

public class LecturerProfileController {

    @FXML
    private Label lblLecturerId;

    @FXML
    private Label lblLecturerName;

    @FXML
    private Label lblLecturerEmail;

    @FXML
    private Label lblCurrentPassword;

    @FXML
    private Label lblTitle;

    @FXML
    private PasswordField txtNewPassword;

    private String currentLecturerId;

    @FXML
    public void initialize() {
        loadLecturerProfile();
    }

    private void loadLecturerProfile() {
        currentLecturerId = LecturerDashboardController.loggedInLecturerId;

        if (currentLecturerId == null || currentLecturerId.isEmpty()) {
            lblTitle.setText("No lecturer is logged in.");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {

            String sql = "SELECT * FROM lecturer WHERE lecturer_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, currentLecturerId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                lblLecturerId.setText(rs.getString("lecturer_id"));
                lblLecturerName.setText(rs.getString("lecturer_name"));
                lblLecturerEmail.setText(rs.getString("lecturer_email"));
                lblCurrentPassword.setText(rs.getString("l_password"));
                lblTitle.setText("Lecturer Profile");
            } else {
                lblTitle.setText("Lecturer data not found.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            lblTitle.setText("Error loading lecturer profile.");
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

            String sql = "UPDATE lecturer SET l_password = ? WHERE lecturer_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, newPassword);
            ps.setString(2, currentLecturerId);

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
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/logbooksystem/LecturerDashboard.fxml"));
            Stage stage = (Stage) lblTitle.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Lecturer Dashboard");
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