package com.myzuji.gui.controller;

import com.myzuji.gui.KeyGeneratorApplication;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

public class MainController {

    @FXML
    private Pane rightPane;

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

}
