package com.myzuji.sadk.system;

import java.io.*;

public class FileHelper {
    private static final int BUFFSIZE = 65536;

    private FileHelper() {
    }

    public static final void deleteFile(String fileName) {
        if (fileName != null) {
            try {
                File file = new File(fileName);
                if (file.exists()) {
                    file.delete();
                }
            } catch (Exception var2) {
            }
        }

    }

    public static final void write(String filePath, byte[] data) throws IOException {
        if (filePath == null) {
            throw new IllegalArgumentException("Illegal Argument: filePath");
        } else if (data == null) {
            throw new IllegalArgumentException("Illegal Argument: data");
        } else {
            FileOutputStream fos = null;

            try {
                File file = new File(filePath);
                if (!file.exists()) {
                    file.createNewFile();
                }

                fos = new FileOutputStream(file);
                fos.write(data, 0, data.length);
                fos.flush();
            } catch (IOException var11) {
                IOException e = var11;
                throw e;
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (Exception var10) {
                    }
                }

            }

        }
    }

    public static final byte[] read(String filePath) throws IOException {
        if (filePath == null) {
            throw new IllegalArgumentException("Illegal Argument: filePath");
        } else {
            FileInputStream crls = null;

            try {
                crls = new FileInputStream(filePath);
                byte[] out = new byte[crls.available()];
                byte[] buffer = new byte[65536];

                int rLength;
                for (int offset = 0; (rLength = crls.read(buffer, 0, buffer.length)) != -1; offset += rLength) {
                    System.arraycopy(buffer, 0, out, offset, rLength);
                }

                byte[] var6 = out;
                return var6;
            } catch (IOException var15) {
                IOException e = var15;
                throw e;
            } finally {
                if (crls != null) {
                    try {
                        crls.close();
                    } catch (Exception var14) {
                    }
                }

            }
        }
    }

    public static final byte[] read(InputStream in) throws IOException {
        if (in == null) {
            throw new IllegalArgumentException("Illegal Argument: in");
        } else {
            try {
                byte[] out = new byte[in.available()];
                byte[] buffer = new byte[65536];

                int rLength;
                for (int offset = 0; (rLength = in.read(buffer, 0, buffer.length)) != -1; offset += rLength) {
                    System.arraycopy(buffer, 0, out, offset, rLength);
                }

                return out;
            } catch (IOException var5) {
                IOException e = var5;
                throw e;
            }
        }
    }
}
