package org.example.expert.config;

import jakarta.servlet.http.HttpServletRequest;
import org.example.expert.domain.auth.dto.response.CustomUserDetails;
import org.example.expert.domain.auth.exception.AuthException;
import org.example.expert.domain.common.annotation.Auth;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.user.enums.UserRole;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Collection;
import java.util.Iterator;

public class AuthUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasAuthAnnotation = parameter.getParameterAnnotation(Auth.class) != null;
        boolean isAuthUserType = parameter.getParameterType().equals(AuthUser.class);

        // @Auth 어노테이션과 AuthUser 타입이 함께 사용되지 않은 경우 예외 발생
        if (hasAuthAnnotation != isAuthUserType) {
            throw new AuthException("@Auth와 AuthUser 타입은 함께 사용되어야 합니다.");
        }

        return hasAuthAnnotation;
    }

    @Override
    public Object resolveArgument(
            @Nullable MethodParameter parameter,
            @Nullable ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            @Nullable WebDataBinderFactory binderFactory
    ) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // JwtFilter 에서 set 한 userId, email, nickname, userRole 값을 가져옴
        Long userId = userDetails.getId();
        String email = userDetails.getUsername();
        String nickname = userDetails.getNickname();
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        System.out.println(email);
        UserRole userRole = UserRole.of(iterator.next().getAuthority());

        return new AuthUser(userId, email, nickname,  userRole);
    }
}
