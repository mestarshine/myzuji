package com.myzuji.gui.controller;

import com.myzuji.sadk.algorithm.common.PKIException;
import com.myzuji.sadk.algorithm.sm2.SM2Crypto;
import com.myzuji.sadk.algorithm.sm2.SM2PrivateKey;
import com.myzuji.sadk.org.bouncycastle.util.encoders.Hex;
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
    void decryptionPriKey(ActionEvent event) throws PKIException {
        String priKey = tempPriKey.getText();
        String data = encryptData.getText();

        if (data == null) {
            throw new IllegalArgumentException("null not allowed for data");
        } else {

            byte[] encoding = Base64.getDecoder().decode(priKey);
            SM2PrivateKey privateKey = SM2PrivateKey.getInstance(encoding);
            try {
                SM2Crypto engine = new SM2Crypto();
                engine.initDecrypt(privateKey.dBigInteger());
                String encryptPriKey = Hex.toHexString(engine.decrypt(Base64.getDecoder().decode(data)));
                plainData.setText(encryptPriKey);
            } catch (Exception e) {
                throw new PKIException("decrypted failure", e);
            }
        }
    }
}
