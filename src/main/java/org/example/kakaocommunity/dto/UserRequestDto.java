package org.example.kakaocommunity.dto;

import lombok.*;

public class UserRequestDto
{

    @Builder
    @Getter
    @AllArgsConstructor
    @RequiredArgsConstructor(access = AccessLevel.PROTECTED)
    public static class signup {
        private String email;
        private String password;
        private String nickname;
        private String imageUrl;

    }
}
