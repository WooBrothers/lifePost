package com.woobros.member.hub.model.member;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicInsert
@Entity
public class Member {

    @Id
    @GeneratedValue
    private Long id;

    @Length(min = 1, max = 30)
    private String name;

    @Column(unique = true, nullable = false)
    @Email
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private String nickName;

    private String picture;

    private String accessToken;

    private String refreshToken;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updateAt;

    public String getRoleKey() {
        return this.role.getKey();
    }

    public Member update(String name) {
        this.name = name;

        return this;
    }

    public Member updateRefreshToken(String reIssueRefreshToken) {
        this.refreshToken = reIssueRefreshToken;
        return this;
    }

    public Member updateTokens(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        return this;
    }
}
