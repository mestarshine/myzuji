package com.myzuji.gui.controller;

import com.myzuji.gui.service.P10RequestUtils;
import com.myzuji.sadk.P10Request;
import com.myzuji.sadk.algorithm.common.PKIException;
import com.myzuji.sadk.algorithm.sm2.SM2PrivateKey;
import com.myzuji.sadk.algorithm.sm2.SM2PublicKey;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class P10Controller {

    @FXML
    private TextField subject;

    @FXML
    private TextField password;

    @FXML
    private TextArea p10Content;

    @FXML
    private TextArea base64SignPriKey;
    @FXML
    private TextArea hexSignPriKey;
    @FXML
    private TextArea signPubKey;
    @FXML
    private TextArea base64TemporaryEncryptionPriKey;
    @FXML
    private TextArea hexTemporaryEncryptionPriKey;
    @FXML
    private TextArea temporaryEncryptionPubKey;

    @FXML
    void generateP10(ActionEvent event) throws PKIException {
        P10Request cfcap10Request = P10RequestUtils.buildSM2CSR(subject.getText(), password.getText());

        SM2PrivateKey sm2PrivateKey = (SM2PrivateKey) cfcap10Request.getPrivateKey();
        SM2PublicKey sm2PublicKey = (SM2PublicKey) cfcap10Request.getPublicKey();

        // 临时密钥，用户需要安全保存（加密密钥）
        SM2PrivateKey tempSm2PrivateKey = (SM2PrivateKey) cfcap10Request.getTemporaryPrivateKey();
        SM2PublicKey tempSm2PublicKey = (SM2PublicKey) cfcap10Request.getTemporaryPublicKey();

        String base6410CSR = new String(cfcap10Request.getBase64P10Data(), StandardCharsets.UTF_8);
        String sm2PriKey = Base64.getEncoder().encodeToString(sm2PrivateKey.getEncoded());
        String sm2HexPriKey = sm2PrivateKey.getD().toString(16);
        String sm2PubKey = Base64.getEncoder().encodeToString(sm2PublicKey.getEncoded());

        String tempSm2PriKey = Base64.getEncoder().encodeToString(tempSm2PrivateKey.getEncoded());
        String tempSm2HexPriKey = tempSm2PrivateKey.getD().toString(16);
        String tempSm2PubKey = Base64.getEncoder().encodeToString(tempSm2PublicKey.getEncoded());

        subject.setText(subject.getText());
        password.setText(password.getText());
        p10Content.setText(base6410CSR);
        base64SignPriKey.setText(sm2PriKey);
        hexSignPriKey.setText(sm2HexPriKey); // 加密
        signPubKey.setText(sm2PubKey);
        base64TemporaryEncryptionPriKey.setText(tempSm2PriKey);
        hexTemporaryEncryptionPriKey.setText(tempSm2HexPriKey);
        temporaryEncryptionPubKey.setText(tempSm2PubKey);
    }

}
