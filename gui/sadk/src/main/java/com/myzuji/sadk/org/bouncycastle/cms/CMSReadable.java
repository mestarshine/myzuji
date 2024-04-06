package com.myzuji.sadk.org.bouncycastle.cms;

import java.io.IOException;
import java.io.InputStream;

public interface CMSReadable {
    InputStream getInputStream() throws IOException, CMSException;
}
