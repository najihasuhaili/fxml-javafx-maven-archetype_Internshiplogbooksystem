package com.example.logbooksystem.controller;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class StudentDashboardController {

    public static String loggedInStudentId;

    @FXML
    private Label lblWelcome;

    @FXML
    private Label lblAnnouncement;

    @FXML
    public void initialize() {
        System.out.println("StudentDashboard loaded. loggedInStudentId = " + loggedInStudentId);

        lblWelcome.setText("Welcome, " + loggedInStudentId);
        lblAnnouncement.setText("Select the week folder to manage your internship logbook entry.");
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            loggedInStudentId = null;

            Parent root = FXMLLoader.load(getClass().getResource("/com/example/logbooksystem/Login.fxml"));
            Stage stage = (Stage) lblWelcome.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openStudentProfile(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/logbooksystem/StudentProfile.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) lblWelcome.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Student Profile");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openWeeklyLogbook(int weekNo) {
        try {
            WeeklyLogbookController.selectedWeek = weekNo;

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/logbooksystem/WeeklyLogbook.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) lblWelcome.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Weekly Logbook - Week " + weekNo);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML private void openWeek1(ActionEvent event) { openWeeklyLogbook(1); }
    @FXML private void openWeek2(ActionEvent event) { openWeeklyLogbook(2); }
    @FXML private void openWeek3(ActionEvent event) { openWeeklyLogbook(3); }
    @FXML private void openWeek4(ActionEvent event) { openWeeklyLogbook(4); }
    @FXML private void openWeek5(ActionEvent event) { openWeeklyLogbook(5); }
    @FXML private void openWeek6(ActionEvent event) { openWeeklyLogbook(6); }
    @FXML private void openWeek7(ActionEvent event) { openWeeklyLogbook(7); }
    @FXML private void openWeek8(ActionEvent event) { openWeeklyLogbook(8); }
    @FXML private void openWeek9(ActionEvent event) { openWeeklyLogbook(9); }
    @FXML private void openWeek10(ActionEvent event) { openWeeklyLogbook(10); }
    @FXML private void openWeek11(ActionEvent event) { openWeeklyLogbook(11); }
    @FXML private void openWeek12(ActionEvent event) { openWeeklyLogbook(12); }
    @FXML private void openWeek13(ActionEvent event) { openWeeklyLogbook(13); }
    @FXML private void openWeek14(ActionEvent event) { openWeeklyLogbook(14); }
}