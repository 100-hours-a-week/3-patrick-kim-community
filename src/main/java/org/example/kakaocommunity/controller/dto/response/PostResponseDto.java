package org.example.kakaocommunity.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

public class PostResponseDto {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class CreateDto {
        private Long postId;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class UpdateDto {
        private Long postId;
        private String title;
        private String content;
        private LocalDateTime updatedAt;
        private String postImageUrl;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class DetailDto {
        private MemberInfo user;
        private Long postId;
        private String title;
        private String content;
        private LocalDateTime createdAt;
        private String postImageUrl;
        private Boolean liked;
        private Integer likes;
        private Integer comments;
        private Integer views;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ListDto {
        private List<PostSummary> posts;
        private Integer nextCursorId;
        private Boolean hasNext;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class PostSummary {
        private MemberInfo member;
        private Long postId;
        private String title;
        private LocalDateTime createdAt;
        private String postImageUrl;
        private Integer likes;
        private Integer comments;
        private Integer views;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class MemberInfo {
        private Integer id;
        private String nickname;
        private String profileImageUrl;
    }
}
