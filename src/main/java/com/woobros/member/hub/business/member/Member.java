package com.woobros.member.hub.business.member;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor
@DynamicInsert
@Entity
public class Member {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Length(min = 1, max = 30)
    private String name;

    @Column(unique = true)
    @NotNull
    @Email
    private String email;

    @NotNull
    @Column(unique = true)
    private String phone;

    @NotNull
    @ColumnDefault("false")
    private boolean marketingSms;

    @NotNull
    @ColumnDefault("false")
    private boolean marketingEmail;

    @NotNull
    @ColumnDefault("false")
    private boolean marketingKakao;

    @NotNull
    @Length(min = 6, max = 25)
    private String password;

    @NotNull
    private String nickName;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updateAt;
}
