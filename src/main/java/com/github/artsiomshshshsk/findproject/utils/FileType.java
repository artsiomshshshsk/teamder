package com.github.artsiomshshshsk.findproject.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FileType {
    CV(".pdf","application/pdf"),
    PROFILE_IMAGE(".png","image/png");

    final String extension;
    final String contentType;
}
