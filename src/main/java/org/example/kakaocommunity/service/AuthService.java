package org.example.kakaocommunity.service;

import lombok.RequiredArgsConstructor;
import org.example.kakaocommunity.apiPayload.status.ErrorStatus;
import org.example.kakaocommunity.controller.dto.request.AuthRequestDto;
import org.example.kakaocommunity.controller.dto.response.AuthResponseDto;
import org.example.kakaocommunity.entity.Member;
import org.example.kakaocommunity.exception.GeneralException;
import org.example.kakaocommunity.entity.RefreshToken;
import org.example.kakaocommunity.repository.MemberRepository;
import org.example.kakaocommunity.repository.RefreshTokenRepository;
import org.example.kakaocommunity.util.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    @Transactional
    public AuthResponseDto.SignupDto signup(AuthRequestDto.SignupDto signupDto) {
        // 이메일 중복 확인
        if (memberRepository.existsByEmail(signupDto.getEmail())) {
            throw new GeneralException(ErrorStatus._DUPLICATED_EMAIL);
        }

        // 닉네임 중복 확인
        if (memberRepository.existsByNickname(signupDto.getNickname())) {
            throw new GeneralException(ErrorStatus._DUPLICATED_NICKNAME);
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(signupDto.getPassword());

        // 회원 생성 및 저장
        Member member = Member.builder()
                .email(signupDto.getEmail())
                .password(encodedPassword)
                .nickname(signupDto.getNickname())
                .image(null)  // TODO: 이미지는 나중에 구현
                .build();

        Member savedMember = memberRepository.save(member);

        return AuthResponseDto.SignupDto.builder()
                .userId(savedMember.getId())
                .build();
    }

    @Transactional
    public AuthResponseDto.LoginDto login(AuthRequestDto.LoginDto loginDto) {
        // 이메일로 회원 조회
        Member member = memberRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new GeneralException(ErrorStatus._BAD_REQUEST));

        // 비밀번호 확인
        if (!passwordEncoder.matches(loginDto.getPassword(), member.getPassword())) {
            throw new GeneralException(ErrorStatus._BAD_REQUEST);
        }

        // JWT 토큰 생성
        String accessToken = jwtUtil.generateAccessToken(member.getId(), member.getEmail());
        String refreshToken = jwtUtil.generateRefreshToken(member.getId());

        // RefreshToken DB에 저장 또는 업데이트
        saveOrUpdateRefreshToken(member.getId(), refreshToken);

        return AuthResponseDto.LoginDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public void logout(Integer userId) {
        // RefreshToken 삭제
        refreshTokenRepository.deleteByUserId(userId);
    }

    @Transactional
    public String refreshAccessToken(String refreshToken) {
        // RefreshToken 검증
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new GeneralException(ErrorStatus._UNAUTHORIZED);
        }

        // DB에서 RefreshToken 조회
        RefreshToken storedToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new GeneralException(ErrorStatus._UNAUTHORIZED));

        // 만료 여부 확인
        if (storedToken.isExpired()) {
            refreshTokenRepository.delete(storedToken);
            throw new GeneralException(ErrorStatus._UNAUTHORIZED);
        }

        // 사용자 조회
        Integer userId = jwtUtil.getUserIdFromToken(refreshToken);
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._NOTFOUND));

        // 새로운 AccessToken 생성
        return jwtUtil.generateAccessToken(member.getId(), member.getEmail());
    }

    // RefreshToken 저장 또는 업데이트
    private void saveOrUpdateRefreshToken(Integer userId, String token) {
        LocalDateTime expiresAt = LocalDateTime.now().plusSeconds(refreshTokenExpiration / 1000);

        RefreshToken refreshToken = refreshTokenRepository.findByUserId(userId)
                .orElse(null);

        if (refreshToken == null) {
            // 새로 생성
            refreshToken = RefreshToken.builder()
                    .userId(userId)
                    .token(token)
                    .expiresAt(expiresAt)
                    .build();
            refreshTokenRepository.save(refreshToken);
        } else {
            // 기존 토큰 업데이트
            refreshToken.updateToken(token, expiresAt);
        }
    }
}