package org.example.kakaocommunity.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.kakaocommunity.apiPayload.status.ErrorStatus;
import org.example.kakaocommunity.controller.dto.request.PostRequestDto;
import org.example.kakaocommunity.controller.dto.response.PostResponseDto;
import org.example.kakaocommunity.entity.Member;
import org.example.kakaocommunity.entity.Post;
import org.example.kakaocommunity.exception.GeneralException;
import org.example.kakaocommunity.repository.MemberRepository;
import org.example.kakaocommunity.repository.PostLikeRepository;
import org.example.kakaocommunity.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final PostLikeRepository postLikeRepository;

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

    public PostResponseDto.ListDto getPostList(Long cursorId, Integer limit) {
        List<Post> posts = postRepository.findPostsWithCursor(cursorId, limit + 1);

        // 다음 페이지 존재 여부 확인
        boolean hasNext = posts.size() > limit;
        if (hasNext) {
            posts = posts.subList(0, limit); // limit 개수만큼만 반환
        }

        Long nextCursorId = posts.isEmpty() ? null : posts.get(posts.size() - 1).getId();

        List<PostResponseDto.PostSummary> postSummaries = posts.stream()
                .map(this::convertToPostSummary)
                .collect(Collectors.toList());

        return PostResponseDto.ListDto.builder()
                .posts(postSummaries)
                .nextCursorId(nextCursorId != null ? nextCursorId.intValue() : null)
                .hasNext(hasNext)
                .build();
    }

    public PostResponseDto.DetailDto getPostDetail(Long postId, Integer memberId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._NOTFOUND));

        Member member = post.getMember();

        // 사용자가 좋아요를 눌렀는지 확인
        boolean liked = postLikeRepository.findByPostIdAndMemberId(postId, memberId).isPresent();

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

    private PostResponseDto.PostSummary convertToPostSummary(Post post) {
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
