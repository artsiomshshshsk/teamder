package com.github.artsiomshshshsk.findproject.service;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Primary
public class FileUploadServiceMock implements FileUploadService{
    @Override
    public String uploadFile(MultipartFile file, String contentType) {
        return "https://nsu.ru/xmlui/bitstream/handle/nsu/8870/Frederick_Brooks.pdf";
    }
}
