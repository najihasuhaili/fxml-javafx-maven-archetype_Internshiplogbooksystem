package com.example.logbooksystem.model;

public class Admin {

    private String adminId;
    private String adminName;
    private String adminEmail;
    private String password;

    public Admin(String adminId, String adminName, String adminEmail, String password) {
        this.adminId = adminId;
        this.adminName = adminName;
        this.adminEmail = adminEmail;
        this.password = password;
    }

    public String getAdminId() { return adminId; }
    public String getAdminName() { return adminName; }
    public String getAdminEmail() { return adminEmail; }
    public String getPassword() { return password; }
}