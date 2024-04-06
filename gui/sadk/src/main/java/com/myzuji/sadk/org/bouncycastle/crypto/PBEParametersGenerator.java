package com.myzuji.sadk.org.bouncycastle.crypto;

import com.myzuji.sadk.org.bouncycastle.util.Strings;

public abstract class PBEParametersGenerator {
    protected byte[] password;
    protected byte[] salt;
    protected int iterationCount;

    protected PBEParametersGenerator() {
    }

    public void init(byte[] password, byte[] salt, int iterationCount) {
        this.password = password;
        this.salt = salt;
        this.iterationCount = iterationCount;
    }

    public byte[] getPassword() {
        return this.password;
    }

    public byte[] getSalt() {
        return this.salt;
    }

    public int getIterationCount() {
        return this.iterationCount;
    }

    public abstract CipherParameters generateDerivedParameters(int var1);

    public abstract CipherParameters generateDerivedParameters(int var1, int var2);

    public abstract CipherParameters generateDerivedMacParameters(int var1);

    public static byte[] PKCS5PasswordToBytes(char[] password) {
        if (password == null) {
            return new byte[0];
        } else {
            byte[] bytes = new byte[password.length];

            for (int i = 0; i != bytes.length; ++i) {
                bytes[i] = (byte) password[i];
            }

            return bytes;
        }
    }

    public static byte[] PKCS5PasswordToUTF8Bytes(char[] password) {
        return password != null ? Strings.toUTF8ByteArray(password) : new byte[0];
    }

    public static byte[] PKCS12PasswordToBytes(char[] password) {
        if (password != null && password.length > 0) {
            byte[] bytes = new byte[(password.length + 1) * 2];

            for (int i = 0; i != password.length; ++i) {
                bytes[i * 2] = (byte) (password[i] >>> 8);
                bytes[i * 2 + 1] = (byte) password[i];
            }

            return bytes;
        } else {
            return new byte[0];
        }
    }
}
