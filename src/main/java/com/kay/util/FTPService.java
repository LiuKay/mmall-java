package com.kay.util;

import static org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;

/**
 * 上传到FTP服务器
 */
@Slf4j
public class FTPService {

    private final String username;
    private final String password;
    private final String ip;
    private FTPClient ftpClient;

    public FTPService(String username, String password, String ip) {
        this.username = username;
        this.password = password;
        this.ip = ip;
    }

    public boolean uploadFile(List<File> fileList) throws IOException {
        log.info("开始连接ftp服务器");
        //TODO: path
        boolean result = uploadFile("img", fileList);
        log.info("开始连接ftp服务器,结束上传,上传结果:{}", result);
        return result;
    }


    public boolean uploadFile(String remotePath, List<File> fileList) throws IOException {
        boolean uploaded = true;
        FileInputStream fis = null;
        //连接FTP服务器
        boolean isConnect = connectServer(this.ip,this.username, this.password);
        if (isConnect) {
            try {
                ftpClient.changeWorkingDirectory(remotePath);
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("UTF-8");
                ftpClient.setFileType(BINARY_FILE_TYPE);
                ftpClient.enterLocalPassiveMode();
                for (File fileItem : fileList) {
                    fis = new FileInputStream(fileItem);
                    ftpClient.storeFile(fileItem.getName(), fis);
                }
                log.info("文件上传成功");

            } catch (IOException e) {
                log.error("上传文件异常", e);
                uploaded = false;
            } finally {
                if (fis != null) {
                    fis.close();
                }
                ftpClient.disconnect();
            }
        }
        return uploaded;
    }


    private boolean connectServer(String ip, String user, String pwd) {
        boolean isSuccess = false;
        ftpClient = new FTPClient();
        try {
            ftpClient.connect(ip);
            isSuccess = ftpClient.login(user, pwd);
        } catch (IOException e) {
            log.error("连接FTP服务器异常", e);
        }
        return isSuccess;
    }

}
