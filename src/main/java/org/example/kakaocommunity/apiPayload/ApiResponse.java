package org.example.kakaocommunity.apiPayload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;import lombok.Getter;
import org.example.kakaocommunity.apiPayload.status.SuccessStatus;


@AllArgsConstructor
@JsonPropertyOrder({"isSuccess","message","data"})
@Getter
public class ApiResponse<T> {

    @JsonProperty("isSuccess")
    private final Boolean isSuccess;
    private final String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T data;


    public static <T> ApiResponse<T> onSuccess( T data) {

        return new ApiResponse<>(true,SuccessStatus._Ok.getMessage(), data);
    }

    public static <T> ApiResponse<T> of(SuccessStatus status , T data) {

        return new ApiResponse<>(true, status.getMessage(), data);
    }
    // 실패한 경우 응답 생성
    public static <T> ApiResponse<T> onFailure( String message, T data){
        return new ApiResponse<>(false, message, data);
    }



}
