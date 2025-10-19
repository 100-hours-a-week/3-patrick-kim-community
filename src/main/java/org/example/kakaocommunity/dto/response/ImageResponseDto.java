package org.example.kakaocommunity.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class ImageResponseDto {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class UploadDto {
        private Long imageId;
        private String imageUrl;
    }
}
