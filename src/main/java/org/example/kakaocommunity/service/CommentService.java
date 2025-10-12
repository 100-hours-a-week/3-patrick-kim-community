package org.example.kakaocommunity.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.kakaocommunity.dto.request.CommentRequestDto;
import org.example.kakaocommunity.entity.Comment;
import org.example.kakaocommunity.entity.Member;
import org.example.kakaocommunity.entity.Post;
import org.example.kakaocommunity.repository.CommentRepository;
import org.example.kakaocommunity.repository.MemberRepository;
import org.example.kakaocommunity.repository.PostRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    public Comment createComment(Integer memberId,Long postId, CommentRequestDto.CreateDto createDto) {
        Member member = memberRepository.findById(memberId).get();
        Post post  = postRepository.findById(postId).get();
        Comment comment = Comment.builder()
                .post(post)
                .content(createDto.getContent())
                .member(member)
                .build();
        return commentRepository.save(comment);
    }
}
