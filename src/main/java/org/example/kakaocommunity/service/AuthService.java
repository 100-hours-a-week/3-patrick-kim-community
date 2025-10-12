package org.example.kakaocommunity.service;

import lombok.RequiredArgsConstructor;
import org.example.kakaocommunity.apiPayload.status.ErrorStatus;
import org.example.kakaocommunity.dto.request.AuthRequestDto;
import org.example.kakaocommunity.dto.response.AuthResponseDto;
import org.example.kakaocommunity.entity.Image;
import org.example.kakaocommunity.entity.Member;
import org.example.kakaocommunity.exception.GeneralException;
import org.example.kakaocommunity.repository.ImageRepository;
import org.example.kakaocommunity.repository.MemberRepository;
import org.example.kakaocommunity.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

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

        return AuthResponseDto.LoginDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public void logout(Integer userId) {

    }
}