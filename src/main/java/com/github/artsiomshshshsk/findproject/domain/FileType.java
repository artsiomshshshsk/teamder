package com.github.artsiomshshshsk.findproject.domain;

public enum FileType {
    CV(".pdf","application/pdf"),
    PROFILE_IMAGE(".png","image/png");

    FileType(String extension, String contentType) {
        this.extension = extension;
        this.contentType = contentType;
    }

    final String extension;
    final String contentType;

    public String getExtension() {
        return extension;
    }

    public String getContentType() {
        return contentType;
    }
}
