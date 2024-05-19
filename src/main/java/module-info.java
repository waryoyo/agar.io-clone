module org.example.agarioclone {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.datatransfer;
    requires java.desktop;

    requires org.kordamp.ikonli.javafx;
    requires com.almasb.fxgl.all;

    opens org.example.agarioclone to javafx.fxml;
    exports org.example.agarioclone;
    exports org.example.agarioclone.components;
    opens org.example.agarioclone.components to javafx.fxml;
    exports org.example.agarioclone.factories;
    opens org.example.agarioclone.factories to javafx.fxml;
}