package com.example.logbooksystem.controller;

import com.example.logbooksystem.DBConnection;
import com.example.logbooksystem.model.StudentRecord;
import com.example.logbooksystem.LecturerSession;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class LecturerDashboardController implements Initializable {

    public static String loggedInLecturerId;

    @FXML
    private Label lblWelcomeLecturer;

    @FXML
    private Label lblMessage;

    @FXML
    private TableView<StudentRecord> tableStudents;

    @FXML
    private TableColumn<StudentRecord, String> colStudentId;

    @FXML
    private TableColumn<StudentRecord, String> colStudentName;

    @FXML
    private TableColumn<StudentRecord, String> colStudentEmail;

    private Connection conn;
    private ObservableList<StudentRecord> list;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println(">>> LecturerDashboardController initialize() called");
        System.out.println("Current loggedInLecturerId = " + loggedInLecturerId);

        conn = DBConnection.getConnection();
        if (conn == null) {
            System.out.println("!!! Database connection FAILED");
            return;
        } else {
            System.out.println("Connected to database!");
        }

        lblWelcomeLecturer.setText("Welcome, Lecturer " + loggedInLecturerId);

        colStudentId.setCellValueFactory(data -> data.getValue().studentIdProperty());
        colStudentName.setCellValueFactory(data -> data.getValue().studentNameProperty());
        colStudentEmail.setCellValueFactory(data -> data.getValue().studentEmailProperty());

        tableStudents.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        list = FXCollections.observableArrayList();
        loadStudents();
    }

    private void loadStudents() {
        System.out.println(">>> loadStudents() called");
        list.clear();

        String sql = "SELECT student_id, student_name, student_email, session, lecturer_id FROM student WHERE lecturer_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, loggedInLecturerId);

            ResultSet rs = stmt.executeQuery();

            int count = 0;
            while (rs.next()) {
                count++;
                list.add(new StudentRecord(
                        rs.getString("student_id"),
                        rs.getString("student_name"),
                        rs.getString("student_email"),
                        rs.getString("session"),
                        rs.getString("lecturer_id")
                ));
            }

            System.out.println("loadStudents(): rows = " + count);
            tableStudents.setItems(list);

            if (count == 0) {
                lblMessage.setText("No students found under this lecturer.");
            } else {
                lblMessage.setText("");
            }

        } catch (SQLException e) {
            System.out.println("!!! SQLException in loadStudents()");
            e.printStackTrace();
            lblMessage.setText("Failed to load student list.");
        }
    }

    @FXML
    private void handleRefresh(ActionEvent event) {
        loadStudents();
    }

    @FXML
    private void handleViewLogbook(ActionEvent event) {

        StudentRecord selected = tableStudents.getSelectionModel().getSelectedItem();

        if (selected == null) {
            lblMessage.setText("Please select a student first.");
            return;
        }

        LecturerSession.selectedStudentId = selected.getStudentId();
        LecturerSession.selectedStudentName = selected.getStudentName();

        try {
            Parent root = FXMLLoader.load(
                getClass().getResource("/com/example/logbooksystem/LecturerLogbookReview.fxml")
            );

            Stage stage = (Stage) lblWelcomeLecturer.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Student Logbook Review");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            loggedInLecturerId = null;

            Parent root = FXMLLoader.load(getClass().getResource("/com/example/logbooksystem/Login.fxml"));
            Stage stage = (Stage) lblWelcomeLecturer.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // =========================
    // NEW MENU FUNCTIONS
    // =========================

    @FXML
    private void openLecturerProfile(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/logbooksystem/LecturerProfile.fxml"));
            Stage stage = (Stage) lblWelcomeLecturer.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Lecturer Profile");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goHomeDashboard(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/logbooksystem/LecturerDashboard.fxml"));
            Stage stage = (Stage) lblWelcomeLecturer.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Lecturer Dashboard");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}