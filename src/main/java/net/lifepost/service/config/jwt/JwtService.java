package net.lifepost.service.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.lifepost.service.model.member.Member;
import net.lifepost.service.model.member.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Getter
@RequiredArgsConstructor
@Service
public class JwtService {

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.access.expiration}")
    private Long accessTokenExpirationPeriod;

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpirationPeriod;

    @Value("${jwt.access.header}")
    private String accessHeader;

    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    /**
     * JWT의 Subject와 Claim으로 email 사용 -> 클레임의 name을 "email"으로 설정 JWT의 헤더에 들어오는 값 :
     * 'Authorization(Key) = Bearer {토큰} (Value)' 형식
     */
    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String EMAIL_CLAIM = "email";
    private static final String ROLE_CLAIM = "role";

    private static final String BEARER = "Bearer ";

    private final MemberRepository memberRepository;

    /**
     * AccessToken 생성 메소드
     */
    public String createAccessToken(Member member) {
        LocalDateTime now = LocalDateTime.now().plusSeconds(accessTokenExpirationPeriod);
        Date date = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());

        return JWT.create() // JWT 토큰을 생성하는 빌더 반환
            .withSubject(ACCESS_TOKEN_SUBJECT) // JWT의 Subject 지정 -> AccessToken이므로 AccessToken
            .withExpiresAt(date) // 토큰 만료 시간 설정
            .withClaim(EMAIL_CLAIM, member.getEmail())
            .sign(Algorithm
                .HMAC512(secretKey)); // HMAC512 알고리즘 사용, application-jwt.yml에서 지정한 secret 키로 암호화
    }

    /**
     * RefreshToken 생성 RefreshToken은 Claim에 email도 넣지 않으므로 withClaim() X
     */
    public String createRefreshToken() {
        LocalDateTime now = LocalDateTime.now().plusSeconds(refreshTokenExpirationPeriod);
        Date date = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());

        return JWT.create()
            .withSubject(REFRESH_TOKEN_SUBJECT)
            .withExpiresAt(date)
            .sign(Algorithm.HMAC512(secretKey));
    }

    /**
     * AccessToken 헤더에 실어서 보내기
     */
    public void sendAccessToken(HttpServletResponse response, String accessToken) {
        response.setStatus(HttpServletResponse.SC_OK);

        setAccessTokenHeader(response, accessToken);
        log.info("재발급된 Access Token : {}", accessToken);
    }

    /**
     * AccessToken + RefreshToken 헤더에 실어서 보내기
     */
    public void sendAccessAndRefreshToken(HttpServletRequest request, HttpServletResponse response,
        String accessToken,
        String refreshToken) {
        response.setStatus(HttpServletResponse.SC_OK);

        setAccessTokenHeader(response, accessToken);
        setAccessTokenToCookie(request, response, accessToken);

        setRefreshTokenHeader(response, refreshToken);
        setRefreshTokenToCookie(request, response, refreshToken);
        log.info("Access Token, Refresh Token 헤더 설정 완료");
    }

    /**
     * 헤더에서 RefreshToken 추출 토큰 형식 : Bearer XXX에서 Bearer를 제외하고 순수 토큰만 가져오기 위해서 헤더를 가져온 후 "Bearer"를
     * 삭제(""로 replace)
     */
    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(refreshHeader))
            .filter(refreshToken -> refreshToken.startsWith(BEARER))
            .map(refreshToken -> refreshToken.replace(BEARER, ""));
    }

    /**
     * 헤더에서 AccessToken 추출 토큰 형식 : Bearer XXX에서 Bearer를 제외하고 순수 토큰만 가져오기 위해서 헤더를 가져온 후 "Bearer"를
     * 삭제(""로 replace)
     */
    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(accessHeader))
            .filter(refreshToken -> refreshToken.startsWith(BEARER))
            .map(refreshToken -> refreshToken.replace(BEARER, ""));
    }

    /**
     * AccessToken에서 Email 추출 추출 전에 JWT.require()로 검증기 생성 verify로 AceessToken 검증 후 유효하다면
     * getClaim()으로 이메일 추출 유효하지 않다면 빈 Optional 객체 반환
     */
    public Optional<String> extractEmail(String accessToken) {
        try {
            // 토큰 유효성 검사하는 데에 사용할 알고리즘이 있는 JWT verifier builder 반환
            return Optional.ofNullable(JWT.require(Algorithm.HMAC512(secretKey))
                .build() // 반환된 빌더로 JWT verifier 생성
                .verify(accessToken) // accessToken을 검증하고 유효하지 않다면 예외 발생
                .getClaim(EMAIL_CLAIM) // claim(Emial) 가져오기
                .asString());
        } catch (Exception e) {
            log.error("액세스 토큰이 유효하지 않습니다.");
            return Optional.empty();
        }
    }

    public Optional<String> extractRole(String accessToken) {
        try {
            // 토큰 유효성 검사하는 데에 사용할 알고리즘이 있는 JWT verifier builder 반환
            return Optional.ofNullable(JWT.require(Algorithm.HMAC512(secretKey))
                .build() // 반환된 빌더로 JWT verifier 생성
                .verify(accessToken) // accessToken을 검증하고 유효하지 않다면 예외 발생
                .getClaim(ROLE_CLAIM) // claim(Emial) 가져오기
                .asString());
        } catch (Exception e) {
            log.error("액세스 토큰이 유효하지 않습니다.");
            return Optional.empty();
        }
    }

    /**
     * AccessToken 헤더 설정
     */
    public void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
        response.setHeader(accessHeader, BEARER + accessToken);
    }

    /**
     * RefreshToken 헤더 설정
     */
    public void setRefreshTokenHeader(HttpServletResponse response, String refreshToken) {
        response.setHeader(refreshHeader, BEARER + refreshToken);
    }

    /**
     * 브라우저 쿠키의 accessToken 변경
     */
    private void setAccessTokenToCookie(HttpServletRequest request, HttpServletResponse response,
        String accessToken) {

        boolean isSetCookie = false;
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("accessToken")) {
                cookie.setValue(accessToken);
                cookie.setPath("/");
                cookie.setMaxAge(Math.toIntExact(accessTokenExpirationPeriod));
                response.addCookie(cookie);

                isSetCookie = true;
                break;
            }
        }

        if (!isSetCookie) {
            Cookie cookie = new Cookie("accessToken", accessToken);
            cookie.setPath("/");
            cookie.setMaxAge(Math.toIntExact(accessTokenExpirationPeriod));
            response.addCookie(cookie);
        }
    }

    /**
     * 브라우저 쿠키의 refreshToken 변경
     */
    private void setRefreshTokenToCookie(HttpServletRequest request, HttpServletResponse response,
        String refreshToken) {

        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("refreshToken")) {
                cookie.setValue(refreshToken);
                cookie.setPath("/");
                cookie.setMaxAge(Math.toIntExact(refreshTokenExpirationPeriod));
                response.addCookie(cookie);
                break;
            }
        }
    }

    /**
     * RefreshToken DB 저장(업데이트)
     */
    public void updateRefreshToken(String email, String refreshToken) {
        memberRepository.findByEmail(email)
            .ifPresentOrElse(
                member -> {
                    member.updateRefreshToken(refreshToken);
                    memberRepository.save(member);
                },
                () -> new Exception("일치하는 회원이 없습니다.")
            );
    }

    public boolean isTokenValid(String token) {
        try {
            JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token);
            return true;
        } catch (Exception e) {
            log.error("유효하지 않은 토큰입니다. {}", e.getMessage());
            return false;
        }
    }
}