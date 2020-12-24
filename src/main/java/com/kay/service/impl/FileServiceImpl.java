package com.kay.service.impl;

import com.kay.config.AppConfigProperties;
import com.kay.service.FileService;
import com.kay.util.FTPService;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by kay on 2018/3/21.
 */
@Service
@Slf4j
public class FileServiceImpl implements FileService {

    private final FTPService ftpService;

    private final AppConfigProperties properties;

    @Autowired
    public FileServiceImpl(FTPService ftpService, AppConfigProperties properties) {
        this.ftpService = ftpService;
        this.properties = properties;
    }

    @Override
    public String getFileServerUrl() {
        return ftpService.getServerUrl();
    }

    @Override
    public String uploadImg(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String fileName = FilenameUtils.getName(originalFilename);
        String extension = FilenameUtils.getExtension(originalFilename);
        String path = properties.getImgPath() + fileName + "_" + UUID.randomUUID().toString() + "." + extension;

        log.info("Start upload file:{},path:{}", fileName, path);
        InputStream inputStream;
        try {
            inputStream = file.getInputStream();
            boolean result = ftpService.uploadFile(path, inputStream);
            log.info("End upload file:{},result={}", path, result);
            return path;
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to upload file", e);
        }
    }
}
