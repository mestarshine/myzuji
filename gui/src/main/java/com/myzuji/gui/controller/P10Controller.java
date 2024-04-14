package com.myzuji.gui.controller;

import com.myzuji.gui.service.BCSM2KeyUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class P10Controller {

    @FXML
    private TextField subject;

    @FXML
    private TextField password;

    @FXML
    private TextArea p10Content;

    @FXML
    private TextArea signPriKeyBase64;
    @FXML
    private TextArea signPriKeyHex;
    @FXML
    private TextArea signPubKey;
    @FXML
    private TextArea temporaryEncryptionPriKeyBase64;
    @FXML
    private TextArea temporaryEncryptionPriKeyHex;
    @FXML
    private TextArea temporaryEncryptionPubKey;

    @FXML
    void generateP10(ActionEvent event) throws Exception {
        BCSM2KeyUtil keyUtil = new BCSM2KeyUtil();
        keyUtil.generateDoublePKCS10(subject.getText(), password.getText());
        subject.setText(subject.getText());
        password.setText(password.getText());
        p10Content.setText(keyUtil.getP10Data());
        signPriKeyBase64.setText(keyUtil.getPriKeyBase64());
        signPriKeyHex.setText(keyUtil.getPriKeyHex()); // 加密
        signPubKey.setText(keyUtil.getPubKeyBase64());
        temporaryEncryptionPriKeyBase64.setText(keyUtil.getTempPriKeyBase64());
        temporaryEncryptionPriKeyHex.setText(keyUtil.getTempPriKeyHex());
        temporaryEncryptionPubKey.setText(keyUtil.getTempPubKeyBase64());
    }

}
