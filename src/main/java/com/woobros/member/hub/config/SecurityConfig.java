package com.woobros.member.hub.config;

import com.woobros.member.hub.config.oauth.CustomOAuth2UserService;
import com.woobros.member.hub.config.oauth.OAuth2LoginFailureHandler;
import com.woobros.member.hub.config.oauth.OAuth2LoginSuccessHandler;
import com.woobros.member.hub.model.member.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService oAuth2UserService;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        /* 시큐리티 설정
         * 아래 자세한 설명을 참조하고 추가적으로 명시하는 정보
         * userInfoEndpoint() 설정 시
         * 컨트롤러를 자동으로 만들어 준다.
         *
         * 예를 들어 설명하자면
         * user -> kakao 로그인 페이지 -> 유저정보 입력 -> 카카오 로그인 성공 -> member api call : 리다이렉트 url
         * 위와 같은 프로세스에서 카카오 리소스 서버가 유저의 정보를 전달할 api가 우리측 member 서버에 있어야 한다.
         *
         * 하지만 이를 따로 구현하지 않아도 spring이 알아서 컨트롤러를 만들어준다.
         *
         * 우리가 해야 할 건 그 요청을 처리할 OAuth2UserService를 구현하는 것이다.
         * 그리고 userService() 메소드에 해당 구현체를 메개변수로 넘겨주면 된다.
         *  */

        // h2 console를 사용하기 위한 옵션
        http
            .csrf().disable()
            .headers().frameOptions().disable();

        http
            // 세션 관리
            .sessionManagement()
            // 세션 사용하지 않음
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http
            // URL별 권한 관리를 설정하는 옵션 시작점, 해당 옵션이 선언되어야만 antMatchers를 사용 가능함
            .authorizeRequests()
            // 권한 관리 대상을 지정하는 옵션. URL, HTTP 메소드별로 관리가 가능합
            .antMatchers("/", "/templates/**", "/css/**", "/js/**",
                "/favicon.ico", "/h2/**", "/login/page")
            .permitAll()
            // antMatchers에서 지정된 URL들은 permitAll() 옵션을 통해 전체 열람 권한 설정
            // /api/v1/** 의 URL에 대해 권한 설정
            .antMatchers("/v1/auth/**")
            // "ROLE_USER" 권한을 가져야만 접근 가능하도록 설정
            .hasRole(Role.USER.name())
            // antMatchers 에서 지정되지 안읂 나머지 url은
            .anyRequest()
            // 인증된 사용자들에게만 접근 허용 처리
            .authenticated();

        http
            // oAuth2 로그인 기능에 대한 설정의 진입점
            .oauth2Login()
            .loginPage("/login/page")
            .defaultSuccessUrl("/")
            .successHandler(oAuth2LoginSuccessHandler)
            .failureHandler(oAuth2LoginFailureHandler)
            // 사용자 정보를 가져온 상태에서 추가 작업 진행
            .userInfoEndpoint().userService(oAuth2UserService);

        // 원래 스프링 시큐리티 필터 순서가 LogoutFilter 이후에 로그인 필터 동작
        // 따라서, LogoutFilter 이후에 우리가 만든 필터 동작하도록 설정
        // 순서 : LogoutFilter -> JwtAuthenticationProcessingFilter -> CustomJsonUsernamePasswordAuthenticationFilter
        // http.addFilterAfter(customJsonUsernamePasswordAuthenticationFilter(), LogoutFilter.class);
        // http.addFilterBefore(jwtAuthenticationProcessingFilter(), CustomJsonUsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}