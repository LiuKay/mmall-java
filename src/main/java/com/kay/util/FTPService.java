package com.kay.util;

import static org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE;

import java.io.IOException;
import java.io.InputStream;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

/**
 * 上传到FTP服务器
 */
@Slf4j
@Data
public class FTPService {

    private final String server;
    private final int port;
    private final String username;
    private final String password;

    private FTPClient ftpClient;

    public FTPService(String server, int port, String username, String password) {
        this.server = server;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    public String getServerUrl(){
        return "ftp://" + server + ":" + port;
    }

    void open() throws IOException {
        if (ftpClient != null && ftpClient.isConnected()) {
            return;
        }

        ftpClient = new FTPClient();
        ftpClient.connect(server, port);
        int reply = ftpClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftpClient.disconnect();
            throw new IOException("Exception in connecting to FTP Server");
        }

        ftpClient.login(username, password);
        ftpClient.setControlEncoding("UTF-8");
        ftpClient.setFileType(BINARY_FILE_TYPE);
        ftpClient.enterLocalPassiveMode();
    }

    void close(){
        if (ftpClient == null || !ftpClient.isConnected()) {
            return;
        }

        try {
            ftpClient.disconnect();
        } catch (IOException e) {
            log.warn("Failed to close ftpClient", e);
        }
    }

    public boolean uploadFile(String path, InputStream inputStream) throws IOException {
        try {
            open();
            return ftpClient.storeFile(path, inputStream);
        }finally {
            if (inputStream != null) {
                inputStream.close();
            }
            close();
        }
    }

}
