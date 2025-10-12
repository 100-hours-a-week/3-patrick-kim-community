package org.example.kakaocommunity.repository;

import org.example.kakaocommunity.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike,Long > {
    Optional<PostLike> findByPostIdAndMemberId(Long postId, Integer memberId);

}
