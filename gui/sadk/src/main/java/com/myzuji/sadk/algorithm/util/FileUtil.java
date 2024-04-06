package com.myzuji.sadk.algorithm.util;

import com.myzuji.sadk.system.global.FileAndBufferConfig;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtil {
    public FileUtil() {
    }

    public static byte[] getBytesFromFile(String filePath) throws IOException {
        InputStream is = new FileInputStream(filePath);
        byte[] result = getBytesFromFile((InputStream) is);
        is.close();
        return result;
    }

    public static byte[] getBytesFromFile(InputStream inputStream) throws IOException {
        byte[] keyhex = null;
        int len = inputStream.available();
        if (len > FileAndBufferConfig.BIGGEST_FILE_SIZE) {
            throw new IOException("file is too big!");
        } else {
            keyhex = new byte[len];
            inputStream.read(keyhex);
            return keyhex;
        }
    }

    public static void writeBytesToFile(byte[] sourceData, OutputStream os) throws IOException {
        if (os != null) {
            os.write(sourceData);
        }

    }

    public static void writeBytesToFile(byte[] data, int offSet, int len, OutputStream os) throws IOException {
        if (os != null) {
            os.write(data, offSet, len);
        }

    }
}

