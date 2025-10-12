package org.example.kakaocommunity.apiPayload.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.kakaocommunity.apiPayload.code.BaseErrorCode;
import org.example.kakaocommunity.apiPayload.code.ErrorReasonDto;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,  "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN,  "금지된 요청입니다."),

    _DUPLICATED_EMAIL(HttpStatus.CONFLICT, "중복된 이메일이 존재합니다."),
    _DUPLICATED_NICKNAME(HttpStatus.CONFLICT, "중복된 닉네임이 존재합니다."),
    _USER_NOTFOUND(HttpStatus.NOT_FOUND,"사용자가 존재하지 않습니다." );



    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public ErrorReasonDto getReason() {
        return ErrorReasonDto.builder()
                .message(message)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDto getReasonHttpStatus() {
        return ErrorReasonDto.builder()
                .message(message)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}
