package org.example.kakaocommunity.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.kakaocommunity.annotation.LoginUser;
import org.example.kakaocommunity.apiPayload.ApiResponse;
import org.example.kakaocommunity.apiPayload.status.SuccessStatus;
import org.example.kakaocommunity.controller.dto.request.AuthRequestDto;
import org.example.kakaocommunity.controller.dto.request.UserRequestDto;
import org.example.kakaocommunity.controller.dto.response.AuthResponseDto;
import org.example.kakaocommunity.controller.dto.response.UserResponseDto;
import org.example.kakaocommunity.service.AuthService;
import org.example.kakaocommunity.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class MemberController {

    private final AuthService authService;
    private final MemberService userService;

    // 회원가입
    @PostMapping
    public ResponseEntity<ApiResponse<AuthResponseDto.SignupDto>> signup(
            @Valid @RequestBody AuthRequestDto.SignupDto signupDto
    ) {
        AuthResponseDto.SignupDto response = authService.signup(signupDto);
        return ResponseEntity.status(SuccessStatus._CREATED.getCode())
                .body(ApiResponse.of(SuccessStatus._CREATED, response));
    }

    // 비밀번호 변경
    @PatchMapping("/me/password")
    public ApiResponse<String> changePassword(
            @LoginUser Integer memberId,
            @RequestBody UserRequestDto.ChangePasswordDto request
    ) {
        userService.changePassword(memberId, request);
        return ApiResponse.onSuccess("비밀번호가 변경되었습니다.");
    }

    // 프로필 수정
    @PatchMapping("/me")
    public ApiResponse<UserResponseDto.UpdateDto> updateProfile(
            @LoginUser Integer memberId,
            @RequestBody UserRequestDto.UpdateProfileDto request
    ) {
        UserResponseDto.UpdateDto response = userService.updateProfile(memberId, request);
        return ApiResponse.onSuccess(response);
    }

    // 회원 탈퇴
    @DeleteMapping("/me")
    public ApiResponse<String> deleteAccount(
            @LoginUser Integer memberId
    ) {
        userService.deleteAccount(memberId);
        return ApiResponse.onSuccess("회원 탈퇴가 완료되었습니다.");
    }
}
