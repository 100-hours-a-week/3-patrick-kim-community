package org.example.kakaocommunity.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.kakaocommunity.annotation.LoginUser;
import org.example.kakaocommunity.apiPayload.ApiResponse;
import org.example.kakaocommunity.controller.dto.request.AuthRequestDto;
import org.example.kakaocommunity.controller.dto.response.AuthResponseDto;
import org.example.kakaocommunity.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping
    public ResponseEntity<ApiResponse<AuthResponseDto.LoginDto>> login(
            @Valid @RequestBody AuthRequestDto.LoginDto loginDto
    ) {
        AuthResponseDto.LoginDto response = authService.login(loginDto);
        return ResponseEntity.ok(
                ApiResponse.onSuccess(response)
        );
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> logout(
            @LoginUser Integer userId
    ) {
        authService.logout(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(ApiResponse.onSuccess(null));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponseDto.RefreshDto>> refreshToken(
            @Valid @RequestBody AuthRequestDto.RefreshDto refreshDto
    ) {
        String newAccessToken = authService.refreshAccessToken(refreshDto.getRefreshToken());
        AuthResponseDto.RefreshDto response = AuthResponseDto.RefreshDto.builder()
                .accessToken(newAccessToken)
                .build();
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }
}
