package com.github.artsiomshshshsk.findproject.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Profile({"dev","test"})
public class MockFileUploadService implements FileUploadService {

    @Value("${app.baseUrl}")
    private String baseUrl;

    @Override
    public String uploadFile(MultipartFile file, FileType fileType) {
        if(fileType == FileType.CV){
            return baseUrl + "/api/mock/pdf";
        }
        if(fileType == FileType.PROFILE_IMAGE){
            return baseUrl + "/api/mock/img";
        }
        return null;
    }

    @Override
    public void deleteFile(String fileURL, FileType fileType) {}
}

