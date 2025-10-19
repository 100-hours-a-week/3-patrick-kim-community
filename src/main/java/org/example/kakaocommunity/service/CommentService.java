package org.example.kakaocommunity.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.kakaocommunity.global.apiPayload.status.ErrorStatus;
import org.example.kakaocommunity.dto.request.CommentRequestDto;
import org.example.kakaocommunity.dto.response.CommentResponseDto;
import org.example.kakaocommunity.entity.Comment;
import org.example.kakaocommunity.entity.Member;
import org.example.kakaocommunity.entity.Post;
import org.example.kakaocommunity.global.exception.GeneralException;
import org.example.kakaocommunity.mapper.CommentMapper;
import org.example.kakaocommunity.repository.CommentRepository;
import org.example.kakaocommunity.repository.MemberRepository;
import org.example.kakaocommunity.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    public CommentResponseDto.CreateDto createComment(Integer memberId, Long postId, CommentRequestDto.CreateDto createDto) {
        Member member = memberRepository.findById(memberId).get();
        Post post = postRepository.findById(postId).get();
        Comment comment = Comment.builder()
                .post(post)
                .content(createDto.getContent())
                .member(member)
                .build();
        Comment savedComment = commentRepository.save(comment);
        return CommentMapper.toCreateDto(savedComment);
    }

    public void delete(Integer memberId, Long commentId) {
        Comment comment =  commentRepository.findById(commentId).get();

        if(!memberId.equals(comment.getMember().getId()))
            throw new GeneralException(ErrorStatus._FORBIDDEN);

        commentRepository.delete(comment);
    }

    public CommentResponseDto.ListDto getCommentList(Long postId, Long cursorId, Integer limit) {
        // limit + 1개를 조회하여 다음 페이지 존재 여부 확인
        List<Comment> comments = commentRepository.findCommentsByPostIdWithCursor(postId, cursorId, limit + 1);

        // 다음 페이지 존재 여부 확인
        boolean hasNext = comments.size() > limit;
        if (hasNext) {
            comments = comments.subList(0, limit);
        }

        // 다음 커서 ID 계산
        Long nextCursorId = comments.isEmpty() ? null : comments.get(comments.size() - 1).getId();

        // DTO 변환
        List<CommentResponseDto.CommentSummary> commentSummaries = comments.stream()
                .map(CommentMapper::toCommentSummary)
                .collect(Collectors.toList());

        return CommentResponseDto.ListDto.builder()
                .comments(commentSummaries)
                .nextCursorId(nextCursorId != null ? nextCursorId.intValue() : null)
                .hasNext(hasNext)
                .build();
    }
}
