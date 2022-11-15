module client {
    requires javafx.controls;
    requires javafx.fxml;


    opens client to javafx.fxml;
    exports client;
    exports all_data;
    opens all_data to javafx.fxml;
}