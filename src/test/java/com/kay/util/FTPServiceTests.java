package com.kay.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.net.ftp.FTPClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockftpserver.fake.FakeFtpServer;
import org.mockftpserver.fake.UserAccount;
import org.mockftpserver.fake.filesystem.DirectoryEntry;
import org.mockftpserver.fake.filesystem.FileEntry;
import org.mockftpserver.fake.filesystem.UnixFakeFileSystem;
import org.springframework.core.io.ClassPathResource;

public class FTPServiceTests {

    private FakeFtpServer fakeFtpServer;

    private FTPService ftpService;

    private FTPClient ftpClient;

    @BeforeEach
    public void setUp() throws Exception {
        fakeFtpServer = new FakeFtpServer();
        fakeFtpServer.addUserAccount(new UserAccount("test", "test", "/data"));

        UnixFakeFileSystem fileSystem = new UnixFakeFileSystem();
        fileSystem.add(new DirectoryEntry("/data"));
        fileSystem.add(new FileEntry("/data/foobar.txt", "abcdef 1234567890"));
        fakeFtpServer.setFileSystem(fileSystem);
        fakeFtpServer.setServerControlPort(0);

        fakeFtpServer.start();

        ftpService = new FTPService("localhost", fakeFtpServer.getServerControlPort(), "test", "test");
        ftpService.open();

        ftpClient = ftpService.getFtpClient();
    }

    @AfterEach
    public void tearDown() throws Exception {
        ftpService.close();
        fakeFtpServer.stop();
    }

    @Test
    public void testListFiles() throws IOException {
        String[] listNames = ftpClient.listNames("");
        assertThat(listNames).contains("foobar.txt");
    }

    @Test
    void testUpload() throws IOException {
        ClassPathResource resource = new ClassPathResource("test.json");
        InputStream inputStream = resource.getInputStream();

        ftpService.uploadFile("/upload.json", inputStream);

        assertThat(fakeFtpServer.getFileSystem().exists("/upload.json")).isTrue();
    }
}