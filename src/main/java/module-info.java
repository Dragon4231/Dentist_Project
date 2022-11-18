module client {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.rmi;
    requires java.naming;


    opens client to javafx.fxml;
    exports client;
    exports all_data;
    opens all_data to javafx.fxml;

}