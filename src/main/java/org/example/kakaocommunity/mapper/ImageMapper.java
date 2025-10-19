package org.example.kakaocommunity.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.example.kakaocommunity.dto.response.ImageResponseDto;
import org.example.kakaocommunity.entity.Image;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ImageMapper {

    public static ImageResponseDto.UploadDto toUploadDto(Image image) {
        return ImageResponseDto.UploadDto.builder()
                .imageId(image.getId())
                .imageUrl(image.getUrl())
                .build();
    }
}
