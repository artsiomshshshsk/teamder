package com.github.artsiomshshshsk.findproject.utils;


import com.github.artsiomshshshsk.findproject.exception.IllegalFileFormat;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.Set;

@Service
@AllArgsConstructor
public class UploadValidationService {

    private final FileUploadService fileUploadService;

    private static final String PDF_FILE_TYPE = "application/pdf";

    private final static Set<String> allowedImageTypes = Set.of("image/png", "image/jpeg", "image/jpg", "image/svg+xml");

    public String uploadCv(MultipartFile file) {
        if (Objects.requireNonNull(file.getContentType()).equals(PDF_FILE_TYPE)) {
            return fileUploadService.uploadFile(file);
        } else {
            throw new IllegalFileFormat("File type is not supported:" + file.getContentType() + ". Choose the following: " + PDF_FILE_TYPE);
        }
    }

    public String uploadProfileImage(MultipartFile file) {
        if(!allowedImageTypes.contains(file.getContentType())){
            throw new IllegalFileFormat("File type is not supported:" + file.getContentType() + ". Choose one of the following: " + allowedImageTypes);
        }
        return fileUploadService.uploadFile(file);
    }

    public void deleteFile(String fileURL) {
        fileUploadService.deleteFile(fileURL);
    }

}
