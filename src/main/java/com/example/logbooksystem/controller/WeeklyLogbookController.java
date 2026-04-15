package com.example.logbooksystem.controller;

import com.example.logbooksystem.DBConnection;
import com.example.logbooksystem.model.WeeklyLogbookEntry;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class WeeklyLogbookController implements Initializable {

    @FXML
    private Label lblWeekTitle;

    @FXML
    private ComboBox<String> cbDay;

    @FXML
    private DatePicker dpDate;

    @FXML
    private ComboBox<String> cbStatus;

    @FXML
    private TextArea txtActivity;

    @FXML
    private TextArea txtRemarks;

    @FXML
    private TableView<WeeklyLogbookEntry> tableLogbook;

    @FXML
    private TableColumn<WeeklyLogbookEntry, String> colDay;

    @FXML
    private TableColumn<WeeklyLogbookEntry, String> colDate;

    @FXML
    private TableColumn<WeeklyLogbookEntry, String> colActivity;

    @FXML
    private TableColumn<WeeklyLogbookEntry, String> colRemarks;

    @FXML
    private TableColumn<WeeklyLogbookEntry, String> colStatus;
    
    @FXML
    private TableColumn<WeeklyLogbookEntry, String> colLecturerComment;
    
    @FXML 
    private TableColumn<WeeklyLogbookEntry, String> colSignature;

    @FXML
    private Button btnBack;

    public static int selectedWeek;

    private Connection conn;
    private ObservableList<WeeklyLogbookEntry> list;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println(">>> WeeklyLogbookController initialize() called");
        System.out.println("Current loggedInStudentId = " + StudentDashboardController.loggedInStudentId);
        System.out.println("Current selectedWeek = " + selectedWeek);

        conn = DBConnection.getConnection();
        if (conn == null) {
            System.out.println("!!! Database connection FAILED (conn == null)");
            return;
        } else {
            System.out.println("Connected to database!");
        }

        lblWeekTitle.setText("Week " + selectedWeek);

        cbDay.setItems(FXCollections.observableArrayList(
                "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"
        ));

        cbStatus.setItems(FXCollections.observableArrayList("Pending"));
        cbStatus.setValue("Pending");
        cbStatus.setDisable(true);

        list = FXCollections.observableArrayList();

        colDay.setCellValueFactory(data -> data.getValue().dayNameProperty());
        colDate.setCellValueFactory(data -> data.getValue().logDateProperty());
        colActivity.setCellValueFactory(data -> data.getValue().activityProperty());
        colRemarks.setCellValueFactory(data -> data.getValue().remarksProperty());
        colStatus.setCellValueFactory(data -> data.getValue().statusProperty());
        colLecturerComment.setCellValueFactory(data -> data.getValue().lecturerCommentProperty());
        colSignature.setCellValueFactory(data -> data.getValue().lecturerSignatureProperty());
        tableLogbook.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        loadTable();

        tableLogbook.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                cbDay.setValue(newVal.getDayName());

                if (newVal.getLogDate() != null && !newVal.getLogDate().isEmpty()) {
                    dpDate.setValue(java.time.LocalDate.parse(newVal.getLogDate()));
                } else {
                    dpDate.setValue(null);
                }

                txtActivity.setText(newVal.getActivity());
                txtRemarks.setText(newVal.getRemarks());

                cbStatus.setValue(newVal.getStatus() != null ? newVal.getStatus() : "Pending");
            }
        });
    }

    private void loadTable() {
        System.out.println(">>> loadTable() called");
        list.clear();

        if (conn == null) {
            System.out.println("!!! loadTable(): conn == null");
            return;
        }

        String sql = "SELECT * FROM weekly_logbook WHERE student_id = ? AND week_no = ? ORDER BY log_date ASC";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, StudentDashboardController.loggedInStudentId);
            stmt.setInt(2, selectedWeek);

            ResultSet rs = stmt.executeQuery();

            int count = 0;
            while (rs.next()) {
                count++;

               Date date = rs.getDate("log_date");
                String dateStr = (date != null) ? date.toString() : "-";

                list.add(new WeeklyLogbookEntry(
                        rs.getInt("log_id"),
                        rs.getString("day_name"),
                        dateStr,
                        rs.getString("activity"),
                        rs.getString("remarks"),
                        rs.getString("status"),
                        rs.getString("lecturer_comment"),
                        rs.getString("lecturer_signature") // 🔥 ADD
));
            }

            System.out.println("loadTable(): rows from DB = " + count);
            tableLogbook.setItems(list);

        } catch (SQLException e) {
            System.out.println("!!! SQLException in loadTable()");
            e.printStackTrace();
        }
    }

    @FXML
    void handleAdd(ActionEvent event) {
        System.out.println(">>> handleAdd() called");
        System.out.println("Current loggedInStudentId = " + StudentDashboardController.loggedInStudentId);

        if (conn == null) {
            System.out.println("!!! conn == null in handleAdd()");
            return;
        }

        if (!validateInput()) {
            System.out.println("handleAdd(): input not complete");
            return;
        }

        String sql = "INSERT INTO weekly_logbook (student_id, week_no, day_name, log_date, activity, remarks, status) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, StudentDashboardController.loggedInStudentId);
            stmt.setInt(2, selectedWeek);
            stmt.setString(3, cbDay.getValue());
            stmt.setDate(4, Date.valueOf(dpDate.getValue()));
            stmt.setString(5, txtActivity.getText().trim());
            stmt.setString(6, txtRemarks.getText().trim());
            stmt.setString(7, "Pending");

            stmt.executeUpdate();
            System.out.println("handleAdd(): INSERT OK");

            loadTable();
            handleClear(null);

        } catch (SQLException e) {
            System.out.println("!!! SQLException in handleAdd()");
            e.printStackTrace();
        }
    }

    @FXML
    void handleUpdate(ActionEvent event) {
        System.out.println(">>> handleUpdate() called");

        WeeklyLogbookEntry selected = tableLogbook.getSelectionModel().getSelectedItem();
        if (selected == null) {
            System.out.println("handleUpdate(): no row selected");
            return;
        }

        if (!validateInput()) {
            System.out.println("handleUpdate(): input not complete");
            return;
        }

        String sql = "UPDATE weekly_logbook SET day_name=?, log_date=?, activity=?, remarks=? WHERE log_id=?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cbDay.getValue());
            stmt.setDate(2, Date.valueOf(dpDate.getValue()));
            stmt.setString(3, txtActivity.getText().trim());
            stmt.setString(4, txtRemarks.getText().trim());
            stmt.setInt(5, selected.getLogId());

            stmt.executeUpdate();
            System.out.println("handleUpdate(): updated log_id = " + selected.getLogId());

            loadTable();
            handleClear(null);

        } catch (SQLException e) {
            System.out.println("!!! SQLException in handleUpdate()");
            e.printStackTrace();
        }
    }

    @FXML
    void handleDelete(ActionEvent event) {
        System.out.println(">>> handleDelete() called");

        WeeklyLogbookEntry selected = tableLogbook.getSelectionModel().getSelectedItem();
        if (selected == null) {
            System.out.println("handleDelete(): no row selected");
            return;
        }

        String sql = "DELETE FROM weekly_logbook WHERE log_id=?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, selected.getLogId());
            stmt.executeUpdate();

            System.out.println("handleDelete(): deleted log_id = " + selected.getLogId());

            loadTable();
            handleClear(null);

        } catch (SQLException e) {
            System.out.println("!!! SQLException in handleDelete()");
            e.printStackTrace();
        }
    }

    @FXML
    void handleClear(ActionEvent event) {
        System.out.println(">>> handleClear() called");

        cbDay.setValue(null);
        dpDate.setValue(null);
        txtActivity.clear();
        txtRemarks.clear();
        cbStatus.setValue("Pending");
        tableLogbook.getSelectionModel().clearSelection();
    }

    private boolean validateInput() {
        return cbDay.getValue() != null
                && dpDate.getValue() != null
                && txtActivity.getText() != null
                && !txtActivity.getText().trim().isEmpty();
    }

    @FXML
    void handleBack(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/logbooksystem/studentDashboard.fxml"));
            Stage stage = (Stage) btnBack.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Student Dashboard");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleLogout(ActionEvent event) {
        try {
            StudentDashboardController.loggedInStudentId = null;

            Parent root = FXMLLoader.load(getClass().getResource("/com/example/logbooksystem/Login.fxml"));
            Stage stage = (Stage) btnBack.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}