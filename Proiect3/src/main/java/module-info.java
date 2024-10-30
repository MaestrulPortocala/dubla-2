module com.example.proiect3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires javafx.base;
    requires org.slf4j;

    opens com.example.proiect3 to javafx.fxml;
    exports com.example.proiect3;
    requires com.google.gson;
    requires java.sql;
    requires okhttp3;
    requires java.net.http;
    opens com.jamendo.model to com.google.gson;
    exports com.Panel;
    opens com.Panel to javafx.fxml;


}
