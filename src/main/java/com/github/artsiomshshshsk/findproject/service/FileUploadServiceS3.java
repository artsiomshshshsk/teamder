package com.github.artsiomshshshsk.findproject.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.github.artsiomshshshsk.findproject.config.S3ConfigProperties;
import com.github.artsiomshshshsk.findproject.exception.InvalidFileFormatException;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@AllArgsConstructor
@Service
@Primary
public class FileUploadServiceS3 implements FileUploadService{

    private final AmazonS3 s3Client;
    private final S3ConfigProperties s3ConfigProperties;
    private static final String TEMP_DIR = System.getProperty("java.io.tmpdir");


    public String uploadFile(MultipartFile file, String contentType) {
        String filename = UUID.randomUUID().toString() + ".pdf";
        while (s3Client.doesObjectExist(s3ConfigProperties.bucketName(), filename)) {
            filename = UUID.randomUUID().toString() + ".pdf";
        }

        if (file.isEmpty() || !file.getContentType().equals(contentType)) {
            throw new InvalidFileFormatException("Only pdf files are acceptable");
        }

        Path tempFile;
        try {
            tempFile = Files.createTempFile(Paths.get(TEMP_DIR), "temp-", ".pdf");
            Files.write(tempFile, file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Upload the file to S3
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("application/pdf");
        PutObjectRequest putObjectRequest = new PutObjectRequest(s3ConfigProperties.bucketName(), filename, tempFile.toFile());
        putObjectRequest.setMetadata(metadata);
        s3Client.putObject(putObjectRequest);

        // Delete the temporary file
        try {
            Files.delete(tempFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return s3ConfigProperties.endpoint() + s3ConfigProperties.bucketName() + "/" + filename;
    }
}