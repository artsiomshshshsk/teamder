package com.github.artsiomshshshsk.findproject.utils;

import io.swagger.annotations.Api;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@Profile({"dev","test","prod"})
@Api(tags = "User")
@RequestMapping("/api/mock")
public class MockFileController {


    @GetMapping("/pdf")
    public ResponseEntity<byte[]> getPdf() throws IOException {
        ClassPathResource pdfFile = new ClassPathResource("static/cv.pdf");
        byte[] bytes = StreamUtils.copyToByteArray(pdfFile.getInputStream());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.builder("inline").filename("file.pdf").build());
        headers.setContentLength(bytes.length);
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }

    @GetMapping("/img")
    public ResponseEntity<byte[]> getImg() throws IOException {
        ClassPathResource pdfFile = new ClassPathResource("static/image.png");
        byte[] bytes = StreamUtils.copyToByteArray(pdfFile.getInputStream());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentDisposition(ContentDisposition.builder("inline").filename("file.png").build());
        headers.setContentLength(bytes.length);
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }

}
