module com.example.calendarioextraordinaria {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.jdi;
    requires java.sql;

    opens Controladores to javafx.fxml;
    opens POJOS to javafx.base;
    opens com.example.calendarioextraordinaria to javafx.fxml;
    exports com.example.calendarioextraordinaria;
    exports Controladores;
    exports MYSQL;
    opens MYSQL to javafx.fxml;
}