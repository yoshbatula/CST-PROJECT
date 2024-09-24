module org.example.cstproject {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens org.example.cstproject to javafx.fxml;
    exports org.example.cstproject;
}