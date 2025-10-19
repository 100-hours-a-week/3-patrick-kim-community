package org.example.kakaocommunity.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.kakaocommunity.global.apiPayload.status.ErrorStatus;
import org.example.kakaocommunity.global.config.S3Config;
import org.example.kakaocommunity.dto.response.ImageResponseDto;
import org.example.kakaocommunity.entity.Image;
import org.example.kakaocommunity.entity.enums.ImageStatus;
import org.example.kakaocommunity.global.exception.GeneralException;
import org.example.kakaocommunity.mapper.ImageMapper;
import org.example.kakaocommunity.repository.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ImageService {
    private final AmazonS3 amazonS3;
    private final S3Config s3Config;
    private final ImageRepository imageRepository;

    public ImageResponseDto.UploadDto uploadImage(MultipartFile file) {
        validateImageFile(file);

        String fileName = createFileName(file.getOriginalFilename());
        String s3Key = "images/" + fileName;

        try {
            uploadToS3(file, s3Key);
        } catch (IOException e) {
            log.error("이미지 업로드 실패: {}", e.getMessage());
            throw new GeneralException(ErrorStatus._INTERNAL_SERVER_ERROR);
        }

        String imageUrl = amazonS3.getUrl(s3Config.getBucket(), s3Key).toString();

        Image image = Image.builder()
                .s3Key(s3Key)
                .url(imageUrl)
                .status(ImageStatus.UNUSED)
                .build();

        Image savedImage = imageRepository.save(image);
        return ImageMapper.toUploadDto(savedImage);
    }

    public void deleteImage(Long imageId) {
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._NOTFOUND));

        // S3에서 파일 삭제
        try {
            amazonS3.deleteObject(new DeleteObjectRequest(s3Config.getBucket(), image.getS3Key()));
        } catch (Exception e) {
            log.error("S3 이미지 삭제 실패: {}", e.getMessage());
            throw new GeneralException(ErrorStatus._INTERNAL_SERVER_ERROR);
        }

        // DB에서 삭제
        imageRepository.delete(image);
    }

    private void validateImageFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new GeneralException(ErrorStatus._BAD_REQUEST);
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new GeneralException(ErrorStatus._BAD_REQUEST);
        }

        // 파일 크기 제한 (10MB)
        long maxSize = 10 * 1024 * 1024; // 10MB
        if (file.getSize() > maxSize) {
            throw new GeneralException(ErrorStatus._BAD_REQUEST);
        }
    }

    private String createFileName(String originalFileName) {
        String ext = extractExt(originalFileName);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    private String extractExt(String originalFileName) {
        int pos = originalFileName.lastIndexOf(".");
        return originalFileName.substring(pos + 1);
    }

    private void uploadToS3(MultipartFile file, String s3Key) throws IOException {
        InputStream inputStream = file.getInputStream();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        PutObjectRequest putObjectRequest = new PutObjectRequest(
                s3Config.getBucket(),
                s3Key,
                inputStream,
                metadata
        );

        amazonS3.putObject(putObjectRequest);
    }
}