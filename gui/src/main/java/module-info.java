module com.myzuji.gui {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires org.bouncycastle.provider;
    requires org.bouncycastle.pkix;

    opens com.myzuji.gui.controller to javafx.fxml;
    exports com.myzuji.gui;
}
