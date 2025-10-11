package org.example.kakaocommunity.apiPayload.status;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessStatus {

    _Ok(200,"요청이 성공적으로 처리되었습니다."),
    _CREATED(201 , "리소스가 성공적으로 생성되었습니다.");

    private final int code;
    private final String message;
}
