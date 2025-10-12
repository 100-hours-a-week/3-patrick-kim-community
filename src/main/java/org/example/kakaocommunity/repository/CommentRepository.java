package org.example.kakaocommunity.repository;

import org.example.kakaocommunity.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment,Long> {
}
