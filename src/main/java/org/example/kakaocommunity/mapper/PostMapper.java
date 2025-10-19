package org.example.kakaocommunity.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.example.kakaocommunity.dto.response.PostResponseDto;
import org.example.kakaocommunity.entity.Member;
import org.example.kakaocommunity.entity.Post;

@NoArgsConstructor(access = AccessLevel.PRIVATE)

public class PostMapper {

    public static PostResponseDto.CreateDto toCreateDto(Post post) {
        return PostResponseDto.CreateDto.builder()
                .postId(post.getId())
                .build();
    }


    public static PostResponseDto.UpdateDto toUpdateDto(Post post) {
        return PostResponseDto.UpdateDto.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .updatedAt(post.getUpdatedAt())
                .postImageUrl(post.getImage() != null ? post.getImage().getUrl() : null)
                .build();
    }

    /**
     * Post Entity → DetailDto 변환
     */
    public static PostResponseDto.DetailDto toDetailDto(Post post, boolean liked) {
        Member member = post.getMember();

        return PostResponseDto.DetailDto.builder()
                .user(PostResponseDto.MemberInfo.builder()
                        .id(member.getId())
                        .nickname(member.getNickname())
                        .profileImageUrl(member.getImage() != null ? member.getImage().getUrl() : null)
                        .build())
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .postImageUrl(post.getImage() != null ? post.getImage().getUrl() : null)
                .liked(liked)
                .likes(post.getLikeCount())
                .comments(post.getCommentCount())
                .views(post.getViewCount())
                .build();
    }

    /**
     * Post Entity → PostSummary 변환
     */
    public static PostResponseDto.PostSummary toPostSummary(Post post) {
        Member member = post.getMember();

        return PostResponseDto.PostSummary.builder()
                .member(PostResponseDto.MemberInfo.builder()
                        .id(member.getId())
                        .nickname(member.getNickname())
                        .profileImageUrl(member.getImage() != null ? member.getImage().getUrl() : null)
                        .build())
                .postId(post.getId())
                .title(post.getTitle())
                .createdAt(post.getCreatedAt())
                .postImageUrl(post.getImage() != null ? post.getImage().getUrl() : null)
                .likes(post.getLikeCount())
                .comments(post.getCommentCount())
                .views(post.getViewCount())
                .build();
    }
}
