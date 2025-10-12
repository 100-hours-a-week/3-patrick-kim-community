package org.example.kakaocommunity.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.kakaocommunity.apiPayload.status.ErrorStatus;
import org.example.kakaocommunity.dto.request.PostRequestDto;
import org.example.kakaocommunity.entity.Member;
import org.example.kakaocommunity.entity.Post;
import org.example.kakaocommunity.exception.GeneralException;
import org.example.kakaocommunity.repository.MemberRepository;
import org.example.kakaocommunity.repository.PostRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public Post getPost(PostRequestDto.CreateDto createDto, Integer memberId) {
        Member member = memberRepository.findById(memberId).get();

        Post post = Post.builder()
                .title(createDto.getTitle())
                .content(createDto.getContent())
                .image(null) //TODO : 이미지 나중에 처리
                .member(member)
                .build();

        return postRepository.save(post);
    }

    public Post updatePost(Long postId, PostRequestDto.UpdateDto request, Integer memberId) {
        Member member = memberRepository.findById(memberId).get();
        Post post = postRepository.findById(postId).get();

        if(!member.getId().equals(post.getMember().getId())) throw new GeneralException(ErrorStatus._UNAUTHORIZED);

        // 이미지가 있으면? 이미지 url에 맞게 변경하기

        post.changeContent(request.getContent());
        post.changeTitle(request.getTitle());

        return post;
    }
}
