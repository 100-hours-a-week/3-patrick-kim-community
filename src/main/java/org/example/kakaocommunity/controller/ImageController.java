package org.example.kakaocommunity.controller;

import lombok.RequiredArgsConstructor;
import org.example.kakaocommunity.annotation.LoginUser;
import org.example.kakaocommunity.apiPayload.ApiResponse;
import org.example.kakaocommunity.apiPayload.status.SuccessStatus;
import org.example.kakaocommunity.controller.dto.response.ImageResponseDto;
import org.example.kakaocommunity.entity.Image;
import org.example.kakaocommunity.service.ImageService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ImageResponseDto.UploadDto>> uploadImage(
            @RequestPart("image") MultipartFile file,
            @LoginUser Integer memberId
    ) {
        Image image = imageService.uploadImage(file);

        ImageResponseDto.UploadDto response = ImageResponseDto.UploadDto.builder()
                .imageId(image.getId())
                .imageUrl(image.getUrl())
                .build();

        return ResponseEntity.status(SuccessStatus._CREATED.getCode())
                .body(ApiResponse.of(SuccessStatus._CREATED, response));
    }

    @DeleteMapping("/{imageId}")
    public ApiResponse<String> deleteImage(
            @PathVariable Long imageId,
            @LoginUser Integer memberId
    ) {
        imageService.deleteImage(imageId);
        return ApiResponse.onSuccess("이미지가 삭제되었습니다.");
    }
}
