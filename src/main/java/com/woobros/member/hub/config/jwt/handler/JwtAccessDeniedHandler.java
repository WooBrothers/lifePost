package com.woobros.member.hub.config.jwt.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woobros.member.hub.config.CommonErrorCode;
import com.woobros.member.hub.config.jwt.ResVO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    /* 403 Fobidden Exception 처리를 위한 클래스

        공통적인 응답을 위한 ResVO 는 아래 작성하였고,
        Object to Json을 위한 CmmnVar.GSON은 공통 스태틱 클래스에 생성해 놓은 Gson 입니다. */

    private ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
        AccessDeniedException accessDeniedException) throws IOException, ServletException {
        PrintWriter writer = response.getWriter();
        ResVO res = ResVO.builder()
            .status(CommonErrorCode.FORBIDDEN.getHttpStatus())
            .message(CommonErrorCode.FORBIDDEN.getMessage())
            .code(CommonErrorCode.FORBIDDEN.getErrorCode())
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