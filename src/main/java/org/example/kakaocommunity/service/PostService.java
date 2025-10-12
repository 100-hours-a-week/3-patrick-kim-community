package org.example.kakaocommunity.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.kakaocommunity.dto.request.PostRequestDto;
import org.example.kakaocommunity.entity.Member;
import org.example.kakaocommunity.entity.Post;
import org.example.kakaocommunity.repository.MemberRepository;
import org.example.kakaocommunity.repository.PostRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    @Transactional
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
}
