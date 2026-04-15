package com.example.logbooksystem.controller;

import com.example.logbooksystem.DBConnection;
import com.example.logbooksystem.model.StudentRecord;
import com.example.logbooksystem.model.LecturerRecord;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class AdminDashboardController implements Initializable {

    static String loggedInAdminId;

    @FXML private Label lblTitle;

    @FXML private Pane paneHome, paneStudent, paneLecturer;

    // HOME
    @FXML private TableView<StudentRecord> tableHome;
    @FXML private TableColumn<StudentRecord, String> colHid, colHname, colHemail, colHsession, colHlecturer;
    @FXML private TextField txtSearchHome;

    // STUDENT
    @FXML private TableView<StudentRecord> tableStudent;
    @FXML private TableColumn<StudentRecord, String> colSid, colSname, colSemail, colSsession, colSlecturer;
    @FXML private TextField txtSid, txtSname, txtSemail, txtSsession, txtSearchStudent;
    @FXML private ComboBox<String> cbStudentLecturer;

    // LECTURER
    @FXML private TableView<LecturerRecord> tableLecturer;
    @FXML private TableColumn<LecturerRecord, String> colLid, colLname, colLemail;
    @FXML private TextField txtLid, txtLname, txtLemail, txtSearchLecturer;

    private Connection conn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        conn = DBConnection.getConnection();

        // HOME TABLE
        colHid.setCellValueFactory(d -> d.getValue().studentIdProperty());
        colHname.setCellValueFactory(d -> d.getValue().studentNameProperty());
        colHemail.setCellValueFactory(d -> d.getValue().studentEmailProperty());
        colHsession.setCellValueFactory(d -> d.getValue().sessionProperty());
        colHlecturer.setCellValueFactory(d -> d.getValue().lecturerIdProperty());

        // STUDENT TABLE
        colSid.setCellValueFactory(d -> d.getValue().studentIdProperty());
        colSname.setCellValueFactory(d -> d.getValue().studentNameProperty());
        colSemail.setCellValueFactory(d -> d.getValue().studentEmailProperty());
        colSsession.setCellValueFactory(d -> d.getValue().sessionProperty());
        colSlecturer.setCellValueFactory(d -> d.getValue().lecturerIdProperty());

        // LECTURER TABLE
        colLid.setCellValueFactory(d -> d.getValue().lecturerIdProperty());
        colLname.setCellValueFactory(d -> d.getValue().lecturerNameProperty());
        colLemail.setCellValueFactory(d -> d.getValue().lecturerEmailProperty());

        tableStudent.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                txtSid.setText(newVal.getStudentId());
                txtSname.setText(newVal.getStudentName());
                txtSemail.setText(newVal.getStudentEmail());
                txtSsession.setText(newVal.getSession());
                cbStudentLecturer.setValue(newVal.getLecturerId());
            }
        });

        tableLecturer.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                txtLid.setText(newVal.getLecturerId());
                txtLname.setText(newVal.getLecturerName());
                txtLemail.setText(newVal.getLecturerEmail());
            }
        });

        loadLecturerCombo();
        loadStudentTable();
        loadStudentManageTable();
        loadLecturerTable();

        showHome();
    }

    // ================= MENU SWITCH =================
    @FXML
    void showHome() {
        paneHome.setVisible(true);
        paneStudent.setVisible(false);
        paneLecturer.setVisible(false);
        lblTitle.setText("Admin Dashboard");
        loadStudentTable();
    }

    @FXML
    void showStudent() {
        paneHome.setVisible(false);
        paneStudent.setVisible(true);
        paneLecturer.setVisible(false);
        lblTitle.setText("Student Management");
        loadStudentManageTable();
        loadLecturerCombo();
    }

    @FXML
    void showLecturer() {
        paneHome.setVisible(false);
        paneStudent.setVisible(false);
        paneLecturer.setVisible(true);
        lblTitle.setText("Lecturer Management");
        loadLecturerTable();
    }

    // ================= LOAD HOME =================
    @FXML
    void loadStudentTable() {
        ObservableList<StudentRecord> list = FXCollections.observableArrayList();

        try {
            String sql = "SELECT s.student_id, s.student_name, s.student_email, s.session, l.lecturer_name " +
                         "FROM student s LEFT JOIN lecturer l ON s.lecturer_id = l.lecturer_id";
            ResultSet rs = conn.createStatement().executeQuery(sql);

            while (rs.next()) {
                list.add(new StudentRecord(
                        rs.getString("student_id"),
                        rs.getString("student_name"),
                        rs.getString("student_email"),
                        rs.getString("session"),
                        rs.getString("lecturer_name")
                ));
            }

            tableHome.setItems(list);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= LOAD STUDENT =================
    private void loadStudentManageTable() {
        ObservableList<StudentRecord> list = FXCollections.observableArrayList();

        try {
            String sql = "SELECT s.student_id, s.student_name, s.student_email, s.session, l.lecturer_name " +
                         "FROM student s LEFT JOIN lecturer l ON s.lecturer_id = l.lecturer_id";
            ResultSet rs = conn.createStatement().executeQuery(sql);

            while (rs.next()) {
                list.add(new StudentRecord(
                        rs.getString("student_id"),
                        rs.getString("student_name"),
                        rs.getString("student_email"),
                        rs.getString("session"),
                        rs.getString("lecturer_name")
                ));
            }

            tableStudent.setItems(list);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadLecturerCombo() {
        ObservableList<String> list = FXCollections.observableArrayList();

        try {
            ResultSet rs = conn.createStatement().executeQuery("SELECT lecturer_name FROM lecturer");

            while (rs.next()) {
                list.add(rs.getString("lecturer_name"));
            }

            cbStudentLecturer.setItems(list);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getLecturerId(String lecturerName) {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT lecturer_id FROM lecturer WHERE lecturer_name=?");
            ps.setString(1, lecturerName);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) return rs.getString("lecturer_id");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // ================= STUDENT CRUD =================
    @FXML
    void addStudent() {
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO student (student_id, student_name, lecturer_id, student_email, s_password, session) VALUES (?, ?, ?, ?, ?, ?)"
            );
            ps.setString(1, txtSid.getText());
            ps.setString(2, txtSname.getText());
            ps.setString(3, getLecturerId(cbStudentLecturer.getValue()));
            ps.setString(4, txtSemail.getText());
            ps.setString(5, "123");
            ps.setString(6, txtSsession.getText());

            ps.executeUpdate();

            loadStudentManageTable();
            loadStudentTable();
            resetStudent();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void updateStudent() {
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE student SET student_name=?, lecturer_id=?, student_email=?, session=? WHERE student_id=?"
            );
            ps.setString(1, txtSname.getText());
            ps.setString(2, getLecturerId(cbStudentLecturer.getValue()));
            ps.setString(3, txtSemail.getText());
            ps.setString(4, txtSsession.getText());
            ps.setString(5, txtSid.getText());

            ps.executeUpdate();

            loadStudentManageTable();
            loadStudentTable();
            resetStudent();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void deleteStudent() {
        try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM student WHERE student_id=?");
            ps.setString(1, txtSid.getText());
            ps.executeUpdate();

            loadStudentManageTable();
            loadStudentTable();
            resetStudent();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void resetStudent() {
        txtSid.clear();
        txtSname.clear();
        txtSemail.clear();
        txtSsession.clear();
        cbStudentLecturer.setValue(null);
    }

    @FXML
    void searchStudent() {
        String keyword = "%" + txtSearchStudent.getText() + "%";
        ObservableList<StudentRecord> list = FXCollections.observableArrayList();

        try {
            String sql = "SELECT s.student_id, s.student_name, s.student_email, s.session, l.lecturer_name " +
                         "FROM student s LEFT JOIN lecturer l ON s.lecturer_id = l.lecturer_id " +
                         "WHERE s.student_id LIKE ? OR s.student_name LIKE ? OR s.student_email LIKE ? OR s.session LIKE ? OR l.lecturer_name LIKE ?";
            PreparedStatement ps = conn.prepareStatement(sql);

            for (int i = 1; i <= 5; i++) ps.setString(i, keyword);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new StudentRecord(
                        rs.getString("student_id"),
                        rs.getString("student_name"),
                        rs.getString("student_email"),
                        rs.getString("session"),
                        rs.getString("lecturer_name")
                ));
            }

            tableStudent.setItems(list);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= HOME SEARCH =================
    @FXML
    void searchHome() {
        String keyword = "%" + txtSearchHome.getText() + "%";
        ObservableList<StudentRecord> list = FXCollections.observableArrayList();

        try {
            String sql = "SELECT s.student_id, s.student_name, s.student_email, s.session, l.lecturer_name " +
                         "FROM student s LEFT JOIN lecturer l ON s.lecturer_id = l.lecturer_id " +
                         "WHERE s.student_id LIKE ? OR s.student_name LIKE ? OR s.student_email LIKE ? OR s.session LIKE ? OR l.lecturer_name LIKE ?";
            PreparedStatement ps = conn.prepareStatement(sql);

            for (int i = 1; i <= 5; i++) ps.setString(i, keyword);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new StudentRecord(
                        rs.getString("student_id"),
                        rs.getString("student_name"),
                        rs.getString("student_email"),
                        rs.getString("session"),
                        rs.getString("lecturer_name")
                ));
            }

            tableHome.setItems(list);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= LECTURER LOAD =================
    private void loadLecturerTable() {
        ObservableList<LecturerRecord> list = FXCollections.observableArrayList();

        try {
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM lecturer");

            while (rs.next()) {
                list.add(new LecturerRecord(
                        rs.getString("lecturer_id"),
                        rs.getString("lecturer_name"),
                        rs.getString("lecturer_email")
                ));
            }

            tableLecturer.setItems(list);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= LECTURER CRUD =================
    @FXML
    void addLecturer() {
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO lecturer (lecturer_id, lecturer_name, lecturer_email, l_password) VALUES (?, ?, ?, ?)"
            );
            ps.setString(1, txtLid.getText());
            ps.setString(2, txtLname.getText());
            ps.setString(3, txtLemail.getText());
            ps.setString(4, "123");

            ps.executeUpdate();

            loadLecturerTable();
            loadLecturerCombo();
            loadStudentManageTable();
            loadStudentTable();
            resetLecturer();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void updateLecturer() {
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE lecturer SET lecturer_name=?, lecturer_email=? WHERE lecturer_id=?"
            );
            ps.setString(1, txtLname.getText());
            ps.setString(2, txtLemail.getText());
            ps.setString(3, txtLid.getText());

            ps.executeUpdate();

            loadLecturerTable();
            loadLecturerCombo();
            loadStudentManageTable();
            loadStudentTable();
            resetLecturer();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void deleteLecturer() {
        try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM lecturer WHERE lecturer_id=?");
            ps.setString(1, txtLid.getText());
            ps.executeUpdate();

            loadLecturerTable();
            loadLecturerCombo();
            loadStudentManageTable();
            loadStudentTable();
            resetLecturer();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void resetLecturer() {
        txtLid.clear();
        txtLname.clear();
        txtLemail.clear();
    }

    @FXML
    void searchLecturer() {
        String keyword = "%" + txtSearchLecturer.getText() + "%";
        ObservableList<LecturerRecord> list = FXCollections.observableArrayList();

        try {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM lecturer WHERE lecturer_id LIKE ? OR lecturer_name LIKE ? OR lecturer_email LIKE ?"
            );
            ps.setString(1, keyword);
            ps.setString(2, keyword);
            ps.setString(3, keyword);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new LecturerRecord(
                        rs.getString("lecturer_id"),
                        rs.getString("lecturer_name"),
                        rs.getString("lecturer_email")
                ));
            }

            tableLecturer.setItems(list);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= LOGOUT =================
    @FXML
    void handleLogout() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/logbooksystem/Login.fxml"));
            Stage stage = (Stage) tableHome.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}