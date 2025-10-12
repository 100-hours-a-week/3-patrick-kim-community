package org.example.kakaocommunity.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class UserResponseDto {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ProfileDto {
        private Integer id;
        private String email;
        private String nickname;
        private String profileImageUrl;
        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class UpdateDto {
        private Integer id;
        private String nickname;
        private String profileImageUrl;
        private LocalDateTime updatedAt;
    }
}