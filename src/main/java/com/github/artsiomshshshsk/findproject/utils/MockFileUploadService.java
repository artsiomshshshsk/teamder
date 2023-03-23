package com.github.artsiomshshshsk.findproject.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Service
@Slf4j
@Profile({"dev","test"})
public class MockFileUploadService implements FileUploadService {

    @Value("${app.baseUrl}")
    private String baseUrl;

    private final String PDF_FILE_TYPE = "application/pdf";

    @Override
    public String uploadFile(MultipartFile file) {
        if(Objects.requireNonNull(file.getContentType()).equals(PDF_FILE_TYPE)) {
            return baseUrl + "/api/mock/pdf";
        }
        else {
            return baseUrl + "/api/mock/img";
        }
    }

    @Override
    public void deleteFile(String fileURL) {}
}

