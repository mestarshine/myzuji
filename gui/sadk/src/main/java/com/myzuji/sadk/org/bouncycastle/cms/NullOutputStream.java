package com.myzuji.sadk.org.bouncycastle.cms;

import java.io.IOException;
import java.io.OutputStream;

public class NullOutputStream extends OutputStream {
    NullOutputStream() {
    }

    public void write(byte[] buf) throws IOException {
    }

    public void write(byte[] buf, int off, int len) throws IOException {
    }

    public void write(int b) throws IOException {
    }
}
