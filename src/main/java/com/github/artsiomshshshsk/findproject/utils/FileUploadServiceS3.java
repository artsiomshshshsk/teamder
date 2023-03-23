package com.github.artsiomshshshsk.findproject.utils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.github.artsiomshshshsk.findproject.config.S3ConfigProperties;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
@Service
@Profile("prod")
public class FileUploadServiceS3 implements FileUploadService {

    private final AmazonS3 s3Client;
    private final S3ConfigProperties s3ConfigProperties;
    private static final String TEMP_DIR = System.getProperty("java.io.tmpdir");


    public String uploadFile(MultipartFile file) {

        String filename = getFilename(file);
        Path tempFile = getTempFile(file, filename);
        uploadTempFile(file, filename, tempFile);
        deleteTemporaryFile(tempFile);

        return s3ConfigProperties.endpoint() + s3ConfigProperties.bucketName() + "/" + filename;
    }

    private static Path getTempFile(MultipartFile file, String filename) {
        Path tempFile;
        try {
            tempFile = Files.createTempFile(Paths.get(TEMP_DIR), "temp-", getExtension(filename));
            Files.write(tempFile, file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return tempFile;
    }

    private static void deleteTemporaryFile(Path tempFile) {
        try {
            Files.delete(tempFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void uploadTempFile(MultipartFile file, String filename, Path tempFile) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        PutObjectRequest putObjectRequest = new PutObjectRequest(s3ConfigProperties.bucketName(), filename, tempFile.toFile());
        putObjectRequest.setMetadata(metadata);
        s3Client.putObject(putObjectRequest);
    }


    private static String getExtension(String filename) {
        return filename.substring(filename.lastIndexOf("."));
    }

    private String getFilename(MultipartFile file) {

        String extension = getExtension(Objects.requireNonNull(file.getOriginalFilename()));
        String filename = UUID.randomUUID() + extension;

        while (s3Client.doesObjectExist(s3ConfigProperties.bucketName(), filename)) {
            filename = UUID.randomUUID() + extension;
        }

        return filename;
    }

    public void deleteFile(String resumeURL) {
        String filename = resumeURL.substring(resumeURL.lastIndexOf("/") + 1);
        s3Client.deleteObject(s3ConfigProperties.bucketName(), filename);
    }

}
