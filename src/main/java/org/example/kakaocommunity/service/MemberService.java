package org.example.kakaocommunity.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.kakaocommunity.apiPayload.status.ErrorStatus;
import org.example.kakaocommunity.controller.dto.request.UserRequestDto;
import org.example.kakaocommunity.controller.dto.response.UserResponseDto;
import org.example.kakaocommunity.entity.Member;
import org.example.kakaocommunity.exception.GeneralException;
import org.example.kakaocommunity.repository.MemberRepository;
import org.example.kakaocommunity.repository.RefreshTokenRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;


    public void changePassword(Integer memberId, UserRequestDto.ChangePasswordDto request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._NOTFOUND));

        // 현재 비밀번호 확인
        if (!passwordEncoder.matches(request.getCurrentPassword(), member.getPassword())) {
            throw new GeneralException(ErrorStatus._UNAUTHORIZED);
        }

        // 새 비밀번호 암호화 및 저장
        String encodedPassword = passwordEncoder.encode(request.getNewPassword());
        member.changePassword(encodedPassword);
    }

    public UserResponseDto.UpdateDto updateProfile(Integer memberId, UserRequestDto.UpdateProfileDto request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._NOTFOUND));

        if (request.getNickname() != null && !request.getNickname().equals(member.getNickname())) {
            if (memberRepository.existsByNickname(request.getNickname())) {
                throw new GeneralException(ErrorStatus._DUPLICATED_NICKNAME);
            }
            member.changeNickname(request.getNickname());
        }

        // TODO: 이미지 변경 로직
        // if (request.getProfileImageUrl() != null) {
        //     member.changeImage(imageService.getImageByUrl(request.getProfileImageUrl()));
        // }

        return UserResponseDto.UpdateDto.builder()
                .id(member.getId())
                .nickname(member.getNickname())
                .profileImageUrl(member.getImage() != null ? member.getImage().getUrl() : null)
                .updatedAt(member.getUpdatedAt())
                .build();
    }

    public void deleteAccount(Integer memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._NOTFOUND));

        // RefreshToken 삭제
        refreshTokenRepository.deleteByUserId(memberId);

        // 회원 삭제
        memberRepository.delete(member);
    }
}
