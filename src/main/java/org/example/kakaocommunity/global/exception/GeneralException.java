package org.example.kakaocommunity.global.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.kakaocommunity.global.apiPayload.code.BaseErrorCode;
import org.example.kakaocommunity.global.apiPayload.code.ErrorReasonDto;

@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {

    private BaseErrorCode code;

    public ErrorReasonDto getErrorReason() {
        return this.code.getReason();
    }

    public ErrorReasonDto getErrorReasonHttpStatus(){
        return this.code.getReasonHttpStatus();
    }
}
