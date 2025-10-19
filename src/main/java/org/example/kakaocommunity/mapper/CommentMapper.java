package org.example.kakaocommunity.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.example.kakaocommunity.dto.response.CommentResponseDto;
import org.example.kakaocommunity.entity.Comment;
import org.example.kakaocommunity.entity.Member;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentMapper {

    public static CommentResponseDto.CreateDto toCreateDto(Comment comment) {
        return CommentResponseDto.CreateDto.builder()
                .commentId(comment.getId())
                .build();
    }

    public static CommentResponseDto.CommentSummary toCommentSummary(Comment comment) {
        Member member = comment.getMember();

        return CommentResponseDto.CommentSummary.builder()
                .user(CommentResponseDto.MemberInfo.builder()
                        .id(member.getId().longValue())
                        .nickname(member.getNickname())
                        .profileImageUrl(member.getImage() != null ? member.getImage().getUrl() : null)
                        .build())
                .commentId(comment.getId())
                .content(comment.getContent())
                .build();
    }
}
