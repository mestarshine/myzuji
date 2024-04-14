package com.myzuji.gui.controller;

import com.myzuji.gui.service.BCSM2Util;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.util.Base64;

public class DecryptionPriKey {

    @FXML
    private TextArea encryptData;

    @FXML
    private TextArea plainData;

    @FXML
    private TextArea tempPriKey;

    @FXML
    void decryptionPriKey(ActionEvent event) {
        String priKey = tempPriKey.getText();
        String data = encryptData.getText();

        if (data == null) {
            throw new IllegalArgumentException("null not allowed for data");
        } else {
            try {

                plainData.setText(BCSM2Util.decryptBase64(Base64.getDecoder().decode(data), priKey));
            } catch (Exception e) {
                throw new RuntimeException("decrypted failure", e);
            }
        }
    }
}
