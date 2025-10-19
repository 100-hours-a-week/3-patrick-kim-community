package org.example.kakaocommunity.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.example.kakaocommunity.dto.response.AuthResponseDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthMapper {

    public static AuthResponseDto.RefreshDto toRefreshDto(String accessToken) {
        return AuthResponseDto.RefreshDto.builder()
                .accessToken(accessToken)
                .build();
    }
}
