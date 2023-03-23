package com.github.artsiomshshshsk.findproject.utils;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {
    String uploadFile(MultipartFile file);
    void deleteFile(String fileURL);
}
