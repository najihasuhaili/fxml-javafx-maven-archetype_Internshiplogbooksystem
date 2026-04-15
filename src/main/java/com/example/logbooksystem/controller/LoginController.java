package com.example.logbooksystem.controller;

import com.example.logbooksystem.DBConnection;
import java.io.IOException;
import java.sql.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class LoginController {
    @FXML private TextField txtId;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblMessage;
    @FXML private Button btnLogin;
    @FXML
    private void handleLogin(ActionEvent event) {
        String id = txtId.getText().trim();
        String pass = txtPassword.getText().trim();

        if (id.isEmpty() || pass.isEmpty()) {
            lblMessage.setText("Please enter ID and password.");
            return;
        }
        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) {
                lblMessage.setText("Database connection failed.");
                return;
            }

            // STUDENT 
            PreparedStatement psStudent = conn.prepareStatement(
                "SELECT * FROM student WHERE student_id=? AND s_password=?"
            );
            psStudent.setString(1, id);
            psStudent.setString(2, pass);
            ResultSet rsStudent = psStudent.executeQuery();
            if (rsStudent.next()) {
                StudentDashboardController.loggedInStudentId = rsStudent.getString("student_id");
                openPage("/com/example/logbooksystem/StudentDashboard.fxml");
                return;
            }

            //LECTURER 
            PreparedStatement psLecturer = conn.prepareStatement(
                "SELECT * FROM lecturer WHERE lecturer_id=? AND l_password=?"
            );
            psLecturer.setString(1, id);
            psLecturer.setString(2, pass);
            ResultSet rsLecturer = psLecturer.executeQuery();
            if (rsLecturer.next()) {
                LecturerDashboardController.loggedInLecturerId = rsLecturer.getString("lecturer_id");
                openPage("/com/example/logbooksystem/LecturerDashboard.fxml");
                return;
            }

            //  ADMIN 
            PreparedStatement psAdmin = conn.prepareStatement(
                "SELECT * FROM admin WHERE admin_id=? AND a_password=?"
            );
            psAdmin.setString(1, id);
            psAdmin.setString(2, pass);
            ResultSet rsAdmin = psAdmin.executeQuery();
            if (rsAdmin.next()) {
                AdminDashboardController.loggedInAdminId = rsAdmin.getString("admin_id");
                openPage("/com/example/logbooksystem/AdminDashboard.fxml");
                return;
            }
            lblMessage.setText("Invalid ID or password.");
        } catch (Exception e) {
            e.printStackTrace();
            lblMessage.setText("Error: " + e.getMessage());
        }
    }
    private void openPage(String path) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(path));
        Stage stage = (Stage) btnLogin.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}