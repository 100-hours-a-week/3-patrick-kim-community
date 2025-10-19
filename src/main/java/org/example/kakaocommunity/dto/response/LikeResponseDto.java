package org.example.kakaocommunity.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class LikeResponseDto {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ToggleDto {
        private Integer postId;
        private Boolean liked;
    }
}
