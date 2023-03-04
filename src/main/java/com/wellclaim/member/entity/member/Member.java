package com.wellclaim.member.entity.member;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public class Member {

    @Id
    @Column
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    @NotNull
    @Email
    private String emailId;

    @NotNull
    private String password;

    @NotNull
    private String nickName;

    @NotNull
    private String name;

    @NotNull
    @Column(unique = true)
    private String phoneNumber;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updateAt;
}
