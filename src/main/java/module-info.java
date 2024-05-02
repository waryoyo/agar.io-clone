module org.example.agarioclone {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.javafx;
    requires com.almasb.fxgl.all;

    opens org.example.agarioclone to javafx.fxml;
    exports org.example.agarioclone;
}