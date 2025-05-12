module com.example.bankapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.example.bankapp.model to javafx.base;
    opens com.example.bankapp.controller to javafx.fxml;

    exports com.example.bankapp;
    exports com.example.bankapp.controller;
}
