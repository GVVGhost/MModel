module gvvghost.mmodel {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens gvvghost.mmodel to javafx.fxml;
    exports gvvghost.mmodel;
}