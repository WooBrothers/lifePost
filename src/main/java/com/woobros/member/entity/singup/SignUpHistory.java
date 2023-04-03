package com.woobros.member.entity.singup;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public class SignUpHistory {

    @Id
    @Column
    @GeneratedValue
    private Long id;

    @Column
    private String emailId;

    @Column
    private String password;

    @Column
    private String nickName;

    @Column
    private String name;

    @Column
    private String phoneNumber;

    @Column
    @CreationTimestamp
    private LocalDateTime created;

    @Column
    @UpdateTimestamp
    private LocalDateTime update;
}
