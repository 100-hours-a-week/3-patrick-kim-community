package org.example.kakaocommunity.mapper;

import org.example.kakaocommunity.dto.response.MemberResponseDto;
import org.example.kakaocommunity.entity.Member;

public class MemberMapper {


    public static MemberResponseDto.ProfileDto toProfileDto(Member member) {
        return MemberResponseDto.ProfileDto.builder()
                .id(member.getId())
                .nickname(member.getNickname())
                .profileImageUrl(member.getImage() != null ? member.getImage().getUrl() : null)
                .email(member.getEmail())
                .build();
    }

    public static MemberResponseDto.UpdateDto toUpdateDto(Member member) {
        return MemberResponseDto.UpdateDto.builder()
                .id(member.getId())
                .nickname(member.getNickname())
                .profileImageUrl(member.getImage() != null ? member.getImage().getUrl() : null)
                .updatedAt(member.getUpdatedAt())
                .build();
    }
}
