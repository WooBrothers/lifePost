package com.woobros.member.hub.config.oauth;

import com.woobros.member.hub.business.member.Member;
import com.woobros.member.hub.business.member.Role;
import com.woobros.member.hub.business.member.SocialType;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuthAttributes {

    /*
     * 리소스 서버(카카오, 구글)로 부터
     * 받는 유저정보를 담을 클래스
     * 리소스 서버별 데이터를 가공하여 일정한 규격(OAuthAttributes)으로 담는 역할
     *
     * */

    private final Map<String, Object> attributes;
    private final String nameAttributeKey;
    private final String name;
    private final String email;
    private final String picture;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes,
        String nameAttributeKey,
        String name,
        String email,
        String picture) {

        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }

    public static OAuthAttributes of(

        SocialType socialType,
        String userNameAttributeName,
        Map<String, Object> attributes) {
        /*
         * 해당 메소드는 반드시 구현이 필요하다.
         *
         * call back 요청을 한 리소스 서벼에게
         * 응답 정보를 독립적으로 처리하기 위한 코드
         */

        OAuthAttributes resultAttribute;

        if (socialType == SocialType.KAKAO) {
            resultAttribute = ofKakao("id", attributes);
        } else if (socialType == SocialType.GOOGLE) {
            resultAttribute = ofGoogle(userNameAttributeName, attributes);
        } else {
            throw new RuntimeException("registration id invalid. ");
        }

        return resultAttribute;
    }

    private static OAuthAttributes ofGoogle(
        String userNameAttributeName,
        Map<String, Object> attribute) {

        return OAuthAttributes.builder()
            .name((String) attribute.get("name"))
            .email((String) attribute.get("email"))
            .picture((String) attribute.get("picture"))
            .attributes(attribute)
            .nameAttributeKey(userNameAttributeName)
            .build();
    }

    private static OAuthAttributes ofKakao(
        String userNameAttributeName,
        Map<String, Object> attribute) {

        Map<String, Object> properties = (Map<String, Object>) attribute.get("properties");
        Map<String, Object> kakaoAccount = (Map<String, Object>) attribute.get("kakao_account");

        return OAuthAttributes.builder()
            .email((String) kakaoAccount.get("email"))
            .picture((String) properties.get("profile_image"))
            .attributes(attribute)
            .nameAttributeKey(userNameAttributeName)
            .build();
    }

    public Member toEntity() {
        return Member.builder()
            .email(email)
            .picture(picture)
            .role(Role.GUEST)
            .build();
    }
}
