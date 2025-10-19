package org.example.kakaocommunity.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

public class CommentRequestDto {

    @Getter
    public static class CreateDto {
        @NotBlank(message = "댓글은 필수입니다.")
        private String content;

        private Integer postId;
    }

    @Getter
    public static class UpdateDto {
        @NotBlank(message = "댓글이 비어있습니다.")
        private String content;
    }
}
