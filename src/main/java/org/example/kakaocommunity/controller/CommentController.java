package org.example.kakaocommunity.controller;

import lombok.RequiredArgsConstructor;
import org.example.kakaocommunity.annotation.LoginUser;
import org.example.kakaocommunity.apiPayload.ApiResponse;
import org.example.kakaocommunity.service.CommentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    // 댓글 삭제
    @DeleteMapping("comments/{commentId}")
    public ApiResponse<String> deleteComment(
            @PathVariable Long commentId,
            @LoginUser Integer memberId
    ) {
        commentService.delete(memberId,commentId);
        return ApiResponse.onSuccess("성공적으로 삭제했습니다.");

    }

}
