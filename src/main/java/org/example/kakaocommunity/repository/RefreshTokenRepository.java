package org.example.kakaocommunity.repository;

import org.example.kakaocommunity.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByUserId(Integer userId);

    Optional<RefreshToken> findByToken(String token);

    void deleteByUserId(Integer userId);
}