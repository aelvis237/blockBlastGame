module com.example.blackblastgame {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.blackblastgame to javafx.fxml;
    exports com.example.blackblastgame;
}