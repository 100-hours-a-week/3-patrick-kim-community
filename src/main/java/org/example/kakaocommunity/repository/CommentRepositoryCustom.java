package org.example.kakaocommunity.repository;

import org.example.kakaocommunity.entity.Comment;

import java.util.List;

public interface CommentRepositoryCustom {

    List<Comment> findCommentsByPostIdWithCursor(Long postId, Long cursorId, int limit);
}