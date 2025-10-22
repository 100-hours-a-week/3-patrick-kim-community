package org.example.kakaocommunity.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.kakaocommunity.global.security.annotation.LoginUser;
import org.example.kakaocommunity.global.apiPayload.ApiResponse;
import org.example.kakaocommunity.dto.request.AuthRequestDto;
import org.example.kakaocommunity.dto.response.AuthResponseDto;
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
    public ResponseEntity<ApiResponse<String>> logout(
            @LoginUser Integer memberId
    ) {
        authService.logout(memberId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponseDto.RefreshDto>> refreshToken(
            @Valid @RequestBody AuthRequestDto.RefreshDto refreshDto
    ) {
        AuthResponseDto.RefreshDto response = authService.refreshAccessToken(refreshDto.getRefreshToken());
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }
}
