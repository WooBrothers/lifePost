package com.woobros.member.hub.config.jwt.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woobros.member.hub.config.CommonErrorCode;
import com.woobros.member.hub.config.jwt.ResVO;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    /* Unauthorized Exception 처리를 위한 클래스 */

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException authException) throws IOException, ServletException {

        PrintWriter writer = response.getWriter();

        ResVO res = ResVO.builder()
            .status(CommonErrorCode.UNAUTHORIZED.getHttpStatus())
            .code(CommonErrorCode.UNAUTHORIZED.getErrorCode())
            .message(CommonErrorCode.UNAUTHORIZED.getMessage())
            .build();

        try {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            writer.write(objectMapper.writeValueAsString(res));
        } catch (NullPointerException e) {
            log.error("응답 메시지 작성 에러", e);
        } finally {
            if (writer != null) {
                writer.flush();
                writer.close();
            }
        }

        response.getWriter().write(objectMapper.writeValueAsString(res));
    }
}