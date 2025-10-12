package org.example.kakaocommunity.controller;

import lombok.RequiredArgsConstructor;
import org.example.kakaocommunity.annotation.LoginUser;
import org.example.kakaocommunity.apiPayload.ApiResponse;
import org.example.kakaocommunity.apiPayload.status.SuccessStatus;
import org.example.kakaocommunity.controller.dto.request.CommentRequestDto;
import org.example.kakaocommunity.controller.dto.request.PostRequestDto;
import org.example.kakaocommunity.controller.dto.response.CommentResponseDto;
import org.example.kakaocommunity.controller.dto.response.PostResponseDto;
import org.example.kakaocommunity.entity.Comment;
import org.example.kakaocommunity.entity.Post;
import org.example.kakaocommunity.service.CommentService;
import org.example.kakaocommunity.service.PostLikeService;
import org.example.kakaocommunity.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final CommentService commentService;
    private final PostLikeService postLikeService;


    // 게시글 작성
    @PostMapping
    public ResponseEntity<ApiResponse<PostResponseDto.CreateDto>> createPost(
            @RequestBody PostRequestDto.CreateDto createDto,
            @LoginUser Integer memberId
    ) {

        Post savedPost = postService.getPost(createDto, memberId);

        PostResponseDto.CreateDto postResponse = PostResponseDto.CreateDto.builder()
                .postId(savedPost.getId()).build();

        return ResponseEntity.status(SuccessStatus._CREATED.getCode())
                .body(ApiResponse.of(SuccessStatus._CREATED, postResponse));

    }

    @PatchMapping("/{postId}")
    public ApiResponse<PostResponseDto.UpdateDto> updatePost(
            @PathVariable Long postId,
            @RequestBody PostRequestDto.UpdateDto request,
            @LoginUser Integer memberId
    ) {
        Post post = postService.updatePost(postId, request, memberId);
        return ApiResponse.onSuccess(PostResponseDto.UpdateDto
                .builder()
                .postId(post.getId())
                .updatedAt(post.getUpdatedAt())
//                .postImageUrl(post.getImage().getUrl()));
                .postImageUrl(null)
                .title(post.getTitle())
                .content(post.getContent())
                .build());
    }

    @GetMapping
    public ApiResponse<PostResponseDto.ListDto> getPostList(
            @RequestParam(required = false) Long cursorId,
            @RequestParam(defaultValue = "10") Integer limit,
            @LoginUser Integer memberId
    ) {
        PostResponseDto.ListDto response = postService.getPostList(cursorId, limit);
        return ApiResponse.onSuccess(response);
    }

    // 게시글 상세 조회
    @GetMapping("/{postId}")
    public ApiResponse<PostResponseDto.DetailDto> getPostDetail(
            @PathVariable Long postId,
            @LoginUser Integer memberId
    ) {
        PostResponseDto.DetailDto response = postService.getPostDetail(postId, memberId);
        return ApiResponse.onSuccess(response);
    }

    //  댓글 추가
    @PostMapping("/{postId}/comments")
    public ResponseEntity<ApiResponse<CommentResponseDto.CreateDto>> createComment(
            @PathVariable Long postId,
            @RequestBody CommentRequestDto.CreateDto createDto,
            @LoginUser Integer memberId
    ) {
        Comment comment = commentService.createComment(memberId, postId, createDto);

        CommentResponseDto.CreateDto response = CommentResponseDto.CreateDto.builder()
                .commentId(comment.getId())
                .build();

        return ResponseEntity.status(SuccessStatus._CREATED.getCode())
                .body(ApiResponse.of(SuccessStatus._CREATED,response));

    }

    // 댓글 목록 조회
    @GetMapping("/{postId}/comments")
    public ApiResponse<CommentResponseDto.ListDto> getCommentList(
            @PathVariable Long postId,
            @RequestParam(required = false) Long cursorId,
            @RequestParam(defaultValue = "10") Integer limit,
            @LoginUser Integer memberId
    ) {
        CommentResponseDto.ListDto response = commentService.getCommentList(postId, cursorId, limit);
        return ApiResponse.onSuccess(response);
    }


    // 좋아요 추가
    @PostMapping("/{postId}/likes")
    public ApiResponse<String> like(
            @PathVariable Long postId,
            @LoginUser Integer memberId
    ) {
        postLikeService.like(postId,memberId);
        return ApiResponse.onSuccess("좋아요 추가했습니다.");
    }

    // 좋아요 삭제
    @DeleteMapping("/{postId}/likes")
    public ApiResponse<String> unlike(
            @PathVariable Long postId,
            @LoginUser Integer memberId
    ) {
        postLikeService.unlike(postId,memberId);
        return ApiResponse.onSuccess("좋아요 삭제했습니다.");
    }
}
