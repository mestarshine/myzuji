package com.myzuji.util.sftp;

import com.jcraft.jsch.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * sftp 工具类
 */
public class SftpUtil implements AutoCloseable {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Session session;
    private final ChannelSftp channel;

    /**
     * 使用 证书登录 SFTP
     *
     * @param sftpConfig sftp 相关配置
     * @throws JSchException
     */
    public SftpUtil(SftpConfig sftpConfig) throws JSchException {
        JSch jsch = new JSch();
        session = jsch.getSession(sftpConfig.getUserName(), sftpConfig.getHost(), sftpConfig.getPort());
        if (sftpConfig.getKeyFilePath() != null) {
            if (sftpConfig.getPassphrase() != null) {
                jsch.addIdentity(sftpConfig.getKeyFilePath(), sftpConfig.getPassphrase());
            } else {
                jsch.addIdentity(sftpConfig.getKeyFilePath());
            }
            session.setConfig("kex", "diffie-hellman-group1-sha1");
            logger.debug("连接sftp，私钥文件路径：{}", sftpConfig.getKeyFilePath());
        } else {
            session.setPassword(sftpConfig.getPassphrase());
        }
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();
        channel = (ChannelSftp) session.openChannel("sftp");
        channel.connect();
        logger.info("连接到SFTP成功.Host：{} ", sftpConfig.getHost());
    }

    @Override
    public void close() throws Exception {
        if (channel != null) {
            channel.disconnect();
        }
        if (session != null) {
            session.disconnect();
        }
    }

    /**
     * 获取路径下所有文件名称
     *
     * @param path
     * @return
     * @throws SftpException
     */
    public List<ChannelSftp.LsEntry> getFolderFileList(String path) throws SftpException {
        List<ChannelSftp.LsEntry> list = new ArrayList<>();
        if (channel != null) {
            Vector vv = channel.ls(path);
            if (vv == null && vv.size() == 0) {
                return list;
            } else {
                Object[] aa = vv.toArray();
                for (int i = 0; i < aa.length; i++) {
                    ChannelSftp.LsEntry temp = (ChannelSftp.LsEntry) aa[i];
                    list.add(temp);

                }
            }
        }
        return list;
    }

    /**
     * 下载文件
     *
     * @param remotePathFile 远程文件
     * @throws SftpException SftpException
     * @throws IOException   IOException
     */
    public InputStream downloadFile(String remotePathFile) throws SftpException, IOException {
        if (channel == null) {
            throw new IOException("sftp server not login");
        }
        return channel.get(remotePathFile);
    }

    /**
     * 上传文件
     *
     * @param remotePathFile
     * @throws SftpException
     * @throws IOException
     */
    public void uploadFile(InputStream data, String remotePathFile) throws SftpException, IOException {
        if (channel == null) {
            throw new IOException("sftp server not login");
        }

        String path = remotePathFile.substring(0, remotePathFile.lastIndexOf("/"));
        if (!isFolderExist(path)) {
            mkdir(path);
        }
        channel.put(data, remotePathFile);
    }

    /**
     * 指定目录是否存在
     *
     * @param directory
     * @return
     */
    public boolean isFolderExist(String directory) {
        try {
            SftpATTRS sftpATTRS = channel.lstat(directory);
            return sftpATTRS.isDir();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 创建指定文件夹
     *
     * @param dirName dirName
     */
    public void mkdir(String dirName) throws SftpException {
        String[] dirs = dirName.split("/");

        String now = channel.pwd();
        channel.cd("/");
        //最后一个为文件 不创建目录
        for (int i = 0; i < dirs.length; i++) {
            if (StringUtils.isNotBlank(dirs[i])) {
                boolean dirExists = openFolder(dirs[i]);
                if (!dirExists) {
                    channel.mkdir(dirs[i]);
                    channel.cd(dirs[i]);
                }
            }
        }
        channel.cd(now);
    }

    /**
     * 打开指定目录
     *
     * @param directory directory
     * @return 是否打开目录
     */
    public boolean openFolder(String directory) {
        try {
            channel.cd(directory);
            return true;
        } catch (SftpException e) {
            return false;
        }
    }
}
