package com.myzuji.gui.controller;

import com.myzuji.gui.KeyGeneratorApplication;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController {

    @FXML
    private Pane rightPane;

    @FXML
    private ResourceBundle resourceBundle;

    @FXML
    private URL location;

    @FXML
    void generateP10View() {
        rightPane.getChildren().clear();
        rightPane.getChildren().addAll(KeyGeneratorApplication.loadView("/view/p10.fxml"));
        KeyGeneratorApplication.getStage().setTitle("生成国密P10");
    }

    @FXML
    void decryptionKeyView() {
        rightPane.getChildren().clear();
        rightPane.getChildren().addAll(KeyGeneratorApplication.loadView("/view/decryptionprikey.fxml"));
        KeyGeneratorApplication.getStage().setTitle("数字信封解密");
    }

    @FXML
    void initialize() {
        generateP10View();
    }
}
