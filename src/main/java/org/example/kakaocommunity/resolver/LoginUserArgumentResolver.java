package org.example.kakaocommunity.resolver;

import org.example.kakaocommunity.annotation.LoginUser;
import org.example.kakaocommunity.apiPayload.status.ErrorStatus;
import org.example.kakaocommunity.exception.GeneralException;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginUser.class)
                && parameter.getParameterType().equals(Integer.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                   ModelAndViewContainer mavContainer,
                                   NativeWebRequest webRequest,
                                   WebDataBinderFactory binderFactory) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new GeneralException(ErrorStatus._UNAUTHORIZED);

        }

        // JWT 필터에서 설정한 userId를 반환
        return Integer.parseInt(authentication.getName());
    }
}
