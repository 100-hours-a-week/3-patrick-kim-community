package org.example.kakaocommunity.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.kakaocommunity.apiPayload.code.BaseErrorCode;
import org.example.kakaocommunity.apiPayload.code.ErrorReasonDto;

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
