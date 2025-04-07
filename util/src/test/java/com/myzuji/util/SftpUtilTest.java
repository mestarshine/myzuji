package com.myzuji.util;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.myzuji.util.sftp.SftpConfig;
import com.myzuji.util.sftp.SftpUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

public class SftpUtilTest {

    SftpUtil sftpCertUtil, sftpPwdUtil;

    @BeforeEach
    void sftpConfig() {
        SftpConfig sftpCertConfig = new SftpConfig("root", "192.168.1.xx", 22, null, "~/.ssh/xxx");
        SftpConfig sftpPwdConfig = new SftpConfig("root", "192.168.1.xx", 22, "xx");
        try {
            sftpPwdUtil = new SftpUtil(sftpPwdConfig);
            sftpCertUtil = new SftpUtil(sftpCertConfig);
        } catch (JSchException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Disabled
    public void isFolderExistTest() {
        System.out.println(sftpPwdUtil.isFolderExist("/tmp"));
    }

    @Test
    @Disabled
    void getFolderFileListTest() throws SftpException {
        List<ChannelSftp.LsEntry> list = sftpCertUtil.getFolderFileList("/tmp");
        for (ChannelSftp.LsEntry lsEntry : list) {
            System.out.println(lsEntry.getFilename());
        }
    }
}
