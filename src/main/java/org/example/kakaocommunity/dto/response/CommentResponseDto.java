package org.example.kakaocommunity.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class CommentResponseDto {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class CreateDto {
        private Long commentId;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class UpdateDto {
        private Long commentId;
        private String content;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ListDto {
        private List<CommentSummary> comments;
        private Integer nextCursorId;
        private Boolean hasNext;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class CommentSummary {
        private MemberInfo user;
        private Long commentId;
        private String content;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class MemberInfo {
        private Long id;
        private String nickname;
        private String profileImageUrl;
    }
}
