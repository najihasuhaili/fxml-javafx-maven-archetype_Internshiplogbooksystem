package com.example.logbooksystem.dao;

import com.example.logbooksystem.DBConnection;
import com.example.logbooksystem.model.Lecturer;
import com.example.logbooksystem.model.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public Lecturer loginLecturer(String email, String password) {
        String sql = "SELECT * FROM lecturer WHERE lecturer_email = ? AND l_password = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Lecturer(
                    rs.getString("lecturer_id"),
                    rs.getString("lecturer_name"),
                    rs.getString("lecturer_email"),
                    rs.getString("l_password")
                );
            }

        } catch (SQLException e) {
            System.out.println("Lecturer login failed: " + e.getMessage());
        }

        return null; 
    }

    public Student loginStudent(String email, String password) {
        String sql = "SELECT * FROM student WHERE student_email = ? AND s_password = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Student(
                    rs.getString("student_id"),
                    rs.getString("student_name"),
                    rs.getString("lecturer_id"),
                    rs.getString("student_email"),
                    rs.getString("s_password"),
                    rs.getString("session")
                );
            }

        } catch (SQLException e) {
            System.out.println("Student login failed: " + e.getMessage());
        }

        return null;
    }
}