package org.example.kakaocommunity.repository;

import org.example.kakaocommunity.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post,Long> {

}
