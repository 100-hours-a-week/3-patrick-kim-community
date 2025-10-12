package org.example.kakaocommunity.repository;

import org.example.kakaocommunity.entity.Post;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> findPostsWithCursor(Long cursorId, int limit);
}