package com.kay.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by kay on 2018/3/21.
 */
public interface FileService {
    String upload(MultipartFile file, String path);
}
