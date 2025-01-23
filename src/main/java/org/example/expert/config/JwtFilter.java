package org.example.expert.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.domain.auth.dto.response.CustomUserDetails;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        if (requestURI.startsWith("/auth")) {
            chain.doFilter(request, response);
        } else {

            String bearerJwt = request.getHeader("Authorization");

            if (bearerJwt == null || !bearerJwt.startsWith("Bearer ")) {
                // 토큰이 없는 경우 400을 반환합니다.
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "JWT 토큰이 필요합니다.");
                chain.doFilter(request, response);
            } else {

                String jwt = jwtUtil.substringToken(bearerJwt);

                try {
                    // JWT 유효성 검사와 claims 추출
                    Claims claims = jwtUtil.extractClaims(jwt);
                    if (claims == null) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "잘못된 JWT 토큰입니다.");
                        chain.doFilter(request, response);
                    }
                    Long userId = Long.parseLong(claims.getSubject());
                    String email = (String) claims.get("email");
                    String nickname = (String) claims.get("nickname");
                    UserRole userRole = UserRole.of(claims.get("userRole", String.class));


                    User user = User.fromAuthUser(new AuthUser(userId, email, nickname, userRole));
                    CustomUserDetails userDetails = new CustomUserDetails(user);
                    System.out.println(user.getEmail());
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(authToken);

                    chain.doFilter(request, response);
                } catch (SecurityException | MalformedJwtException e) {
                    log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.", e);
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않는 JWT 서명입니다.");
                } catch (ExpiredJwtException e) {
                    log.error("Expired JWT token, 만료된 JWT token 입니다.", e);
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "만료된 JWT 토큰입니다.");
                } catch (UnsupportedJwtException e) {
                    log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.", e);
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "지원되지 않는 JWT 토큰입니다.");
                } catch (Exception e) {
                    log.error("Internal server error", e);
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
            }
        }
    }
}
