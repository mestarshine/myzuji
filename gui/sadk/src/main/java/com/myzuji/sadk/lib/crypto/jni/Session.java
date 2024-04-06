package com.myzuji.sadk.lib.crypto.jni;

import com.myzuji.sadk.algorithm.common.Mechanism;
import com.myzuji.sadk.algorithm.common.PKIException;

import java.io.InputStream;
import java.io.OutputStream;
import java.security.*;

public interface Session {

    byte[] sign(Mechanism var1, PrivateKey var2, byte[] var3) throws PKIException;

    byte[] sign(Mechanism var1, PrivateKey var2, InputStream var3) throws PKIException;

    boolean verify(Mechanism var1, PublicKey var2, byte[] var3, byte[] var4) throws PKIException;

    boolean verify(Mechanism var1, PublicKey var2, InputStream var3, byte[] var4) throws PKIException;

    byte[] signByHash(Mechanism var1, PrivateKey var2, byte[] var3) throws PKIException;

    boolean verifyByHash(Mechanism var1, PublicKey var2, byte[] var3, byte[] var4) throws PKIException;

    Provider getProvider();

    String getProviderName();

    byte[] encrypt(Mechanism var1, Key var2, byte[] var3) throws PKIException;

    byte[] decrypt(Mechanism var1, Key var2, byte[] var3) throws PKIException;

    void encrypt(Mechanism var1, Key var2, InputStream var3, OutputStream var4) throws PKIException;

    void decrypt(Mechanism var1, Key var2, InputStream var3, OutputStream var4) throws PKIException;

    KeyPair generateKeyPair(Mechanism var1, int var2) throws PKIException;

    Key generateKey(Mechanism var1) throws PKIException;

    Key generateKey(Mechanism var1, byte[] var2) throws PKIException;
}
