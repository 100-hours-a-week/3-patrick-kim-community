package org.example.kakaocommunity.apiPayload.code;

public interface BaseErrorCode {
    ErrorReasonDto getReason();

    ErrorReasonDto getReasonHttpStatus();
}
