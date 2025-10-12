package org.example.kakaocommunity.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.kakaocommunity.entity.PostLike;
import org.example.kakaocommunity.repository.MemberRepository;
import org.example.kakaocommunity.repository.PostLikeRepository;
import org.example.kakaocommunity.repository.PostRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostLikeService {
    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;


    @Transactional
    public void like(Long postId, Integer memberId) {
        postLikeRepository.save(
                PostLike.builder()
                        .post(postRepository.findById(postId).get())
                        .member(memberRepository.findById(memberId).get())
                        .build()
        );
    }

    @Transactional
    public void unlike(Long postId, Integer memberId) {
        postLikeRepository.findByPostIdAndMemberId(postId,memberId)
                .ifPresent(postLikeRepository::delete);
    }
}
