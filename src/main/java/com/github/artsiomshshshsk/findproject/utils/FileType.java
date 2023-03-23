package com.github.artsiomshshshsk.findproject.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FileType {
    CV(".pdf","application/pdf"),
    PROFILE_IMAGE_PNG(".png","image/png"),

    PROFILE_IMAGE_SVG(".svg","image/svg+xml"),

    PROFILE_IMAGE_JPG(".jpg","image/jpeg");

    final String extension;
    final String contentType;
}
