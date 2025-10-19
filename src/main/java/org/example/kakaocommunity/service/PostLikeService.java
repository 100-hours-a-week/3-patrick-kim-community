package org.example.kakaocommunity.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.kakaocommunity.entity.Member;
import org.example.kakaocommunity.entity.Post;
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
        Post post = postRepository.findById(postId).get();
        Member member = memberRepository.findById(memberId).get();

        PostLike postLike = PostLike.builder()
                .post(post)
                .member(member)
                .build();
        postLikeRepository.save(postLike);

        // 좋아요 수 증가
        post.increaseLikeCount();
    }

    @Transactional
    public void unlike(Long postId, Integer memberId) {
        postLikeRepository.findByPostIdAndMemberId(postId, memberId)
                .ifPresent(postLike -> {
                    // 좋아요 수 감소
                    Post post = postLike.getPost();
                    post.decreaseLikeCount();

                    postLikeRepository.delete(postLike);
                });
    }
}
