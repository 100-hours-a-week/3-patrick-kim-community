package org.example.kakaocommunity.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

public class PostRequestDto {

    @Getter
    public static class CreateDto {
        @NotBlank(message = "게시글 제목은 필수입니다.")
        private String title;

        @NotBlank(message = "게시글 내용은 필수입니다.")
        private String content;

        private Long postImageId;
    }

    @Getter
    public static class UpdateDto {
        private String title;
        private String content;
        private Long postImageId;
    }
}
