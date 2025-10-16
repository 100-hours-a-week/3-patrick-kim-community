package org.example.kakaocommunity.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

public class MemberRequestDto {

    @Getter
    public static class ChangePasswordDto {
        @NotBlank(message = "현재 비밀번호가 비어 있습니다.")
        private String currentPassword;

        @NotBlank(message = "새 비밀번호가 비어 있습니다.")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*#?&]{8,}$",
                message = "비밀번호 형식이 올바르지 않습니다.")
        private String newPassword;
    }

    @Getter
    public static class UpdateProfileDto {
        @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,10}$",
                message = "닉네임 형식이 올바르지 않습니다.")
        private String nickname;

        private Long profileImageId;
    }
}
