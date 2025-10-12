package org.example.kakaocommunity.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class MemberResponseDto {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ProfileDto {
        private Integer userId;
        private String nickname;
        private String email;
        private String profileImageUrl;
    }
}
