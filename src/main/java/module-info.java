module ru.globux.testfx {
    requires javafx.controls;
    requires javafx.fxml;

    opens ru.globux.testfx to javafx.fxml;
    exports ru.globux.testfx;
}