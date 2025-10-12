package org.example.kakaocommunity.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class ImageResponseDto {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class UploadDto {
        private String s3Url;
    }
}
