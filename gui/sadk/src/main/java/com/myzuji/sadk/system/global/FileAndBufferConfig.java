package com.myzuji.sadk.system.global;

import com.myzuji.sadk.system.CompatibleConfig;

public class FileAndBufferConfig {
    public static final int BIG_FILE_BUFFER;
    public static final int SOURCE_FILE_SIZE;
    public static final int SIGN_FILE_SIZE;
    public static final int ENVELOPE_FILE_SIZE;
    public static final int BIGGEST_FILE_SIZE;

    public FileAndBufferConfig() {
    }

    static {
        BIG_FILE_BUFFER = CompatibleConfig.FILEANDBUFFER_BIG_FILE_BUFFER;
        SOURCE_FILE_SIZE = CompatibleConfig.FILEANDBUFFER_SOURCE_FILE_MAXSIZE;
        SIGN_FILE_SIZE = CompatibleConfig.FILEANDBUFFER_SIGNED_FILE_MAXSIZE;
        ENVELOPE_FILE_SIZE = CompatibleConfig.FILEANDBUFFER_ENVELOPE_FILE_MAXSIZE;
        BIGGEST_FILE_SIZE = CompatibleConfig.FILEANDBUFFER_BIGGEST_FILE_MAXSIZE;
    }
}

