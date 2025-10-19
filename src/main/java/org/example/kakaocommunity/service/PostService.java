package org.example.kakaocommunity.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.kakaocommunity.global.apiPayload.status.ErrorStatus;
import org.example.kakaocommunity.dto.request.PostRequestDto;
import org.example.kakaocommunity.dto.response.PostResponseDto;
import org.example.kakaocommunity.entity.Image;
import org.example.kakaocommunity.entity.Member;
import org.example.kakaocommunity.entity.Post;
import org.example.kakaocommunity.global.exception.GeneralException;
import org.example.kakaocommunity.mapper.PostMapper;
import org.example.kakaocommunity.repository.ImageRepository;
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
    private final ImageRepository imageRepository;

    public PostResponseDto.CreateDto createPost(PostRequestDto.CreateDto createDto, Integer memberId) {
        Member member = memberRepository.findById(memberId).get();

        // 이미지 조회 (있는 경우)
        Image image = null;
        if (createDto.getPostImageId() != null) {
            image = imageRepository.findById(createDto.getPostImageId())
                    .orElseThrow(() -> new GeneralException(ErrorStatus._NOTFOUND));
        }

        Post post = Post.builder()
                .title(createDto.getTitle())
                .content(createDto.getContent())
                .image(image)
                .member(member)
                .build();

        Post savedPost = postRepository.save(post);
        return PostMapper.toCreateDto(savedPost);
    }

    public PostResponseDto.UpdateDto updatePost(Long postId, PostRequestDto.UpdateDto request, Integer memberId) {
        Member member = memberRepository.findById(memberId).get();
        Post post = postRepository.findById(postId).get();

        if(!member.getId().equals(post.getMember().getId())) throw new GeneralException(ErrorStatus._UNAUTHORIZED);

        // 제목 변경
        if (request.getTitle() != null) {
            post.changeTitle(request.getTitle());
        }

        // 내용 변경
        if (request.getContent() != null) {
            post.changeContent(request.getContent());
        }

        // 이미지 변경 (imageId가 있는 경우)
        if (request.getPostImageId() != null) {
            Image image = imageRepository.findById(request.getPostImageId())
                    .orElseThrow(() -> new GeneralException(ErrorStatus._NOTFOUND));
            post.changeImage(image);
        }

        return PostMapper.toUpdateDto(post);
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
                .map(PostMapper::toPostSummary)
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

        // 조회수 증가
        post.increaseViewCount();

        // 사용자가 좋아요를 눌렀는지 확인
        boolean liked = postLikeRepository.findByPostIdAndMemberId(postId, memberId).isPresent();

        return PostMapper.toDetailDto(post, liked);
    }
}
