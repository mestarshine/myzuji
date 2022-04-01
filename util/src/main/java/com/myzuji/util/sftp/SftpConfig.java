package com.myzuji.util.sftp;

public class SftpConfig {

    /**
     * sftp 用户名
     */
    private final String userName;
    /**
     * sftp 地址
     */
    private final String host;
    /**
     * sftp 端口
     */
    private final int port;
    /**
     * sftp 密码
     */
    private final String passphrase;
    /**
     * sftp 证书路径
     */
    private String keyFilePath;

    /**
     * 使用证书登录 sftp
     *
     * @param userName    用户名
     * @param host        路径
     * @param port        端口
     * @param passphrase  证书密码，如证书无密码，传 null 即可
     * @param keyFilePath 证书路径
     */
    public SftpConfig(String userName, String host, int port, String passphrase, String keyFilePath) {
        this.userName = userName;
        this.host = host;
        this.port = port;
        this.passphrase = passphrase;
        this.keyFilePath = keyFilePath;
    }

    /**
     * 使用账户名密码登录
     *
     * @param userName   用户名
     * @param host       路径
     * @param port       端口
     * @param passphrase sftp密码
     */
    public SftpConfig(String userName, String host, int port, String passphrase) {
        this.userName = userName;
        this.host = host;
        this.port = port;
        this.passphrase = passphrase;
    }

    public String getUserName() {
        return userName;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getPassphrase() {
        return passphrase;
    }

    public String getKeyFilePath() {
        return keyFilePath;
    }
}
