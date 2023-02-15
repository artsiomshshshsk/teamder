package com.github.artsiomshshshsk.findproject.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {
    String uploadFile(MultipartFile file, String contentType);
}
