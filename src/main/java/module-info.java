module com.example.logbooksystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql; 
    requires java.base;

    // Untuk FXML loader boleh create controller via reflection
    opens com.example.logbooksystem.controller to javafx.fxml;
    opens com.example.logbooksystem to javafx.fxml; 

    // Package yang nak kita "public" ke luar module (optional untuk controller)
    exports com.example.logbooksystem;
    exports com.example.logbooksystem.controller;
}