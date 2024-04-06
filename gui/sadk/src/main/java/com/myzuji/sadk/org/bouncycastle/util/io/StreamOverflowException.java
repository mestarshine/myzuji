package com.myzuji.sadk.org.bouncycastle.util.io;

import java.io.IOException;

public class StreamOverflowException extends IOException {

    private static final long serialVersionUID = 4843793234895401315L;

    public StreamOverflowException(String msg) {
        super(msg);
    }
}
