package org.example.kakaocommunity.controller.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

public class AuthRequestDto {

    @Getter
    public static class SignupDto {
        @NotBlank(message = "이메일이 비어 있습니다.")
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        private String email;

        @NotBlank(message = "비밀번호가 비어 있습니다.")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*#?&]{8,}$",
                 message = "비밀번호 형식이 올바르지 않습니다.")
        private String password;

        @NotBlank(message = "닉네임이 비어 있습니다.")
        @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,10}$",
                 message = "닉네임 형식이 올바르지 않습니다.")
        private String nickname;

        private String profileImageUrl;
    }

    @Getter
    public static class LoginDto {
        @NotBlank(message = "이메일이 비어 있습니다.")
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        private String email;

        @NotBlank(message = "비밀번호가 비어 있습니다.")
        private String password;
    }

    @Getter
    public static class RefreshDto {
        @NotBlank(message = "RefreshToken이 필요합니다.")
        private String refreshToken;
    }
}
