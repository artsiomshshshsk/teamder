package com.github.artsiomshshshsk.findproject.service;

import com.github.artsiomshshshsk.findproject.domain.FileType;
import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {
    String uploadFile(MultipartFile file, FileType fileType);
    void deleteFile(String fileURL, FileType fileType);
}
