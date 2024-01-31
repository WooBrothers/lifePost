package net.lifepost.service.model.member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.lifepost.service.model.card.memb_card.MemberCard;
import net.lifepost.service.model.card.memb_cust_card.MemberCustomCard;
import net.lifepost.service.model.member_letter.MemberLetter;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    private String refreshToken;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Column(columnDefinition = "int default 0")
    private Integer stampCount;

    @OneToMany(mappedBy = "member", orphanRemoval = true)
    private List<MemberLetter> memberLetters;

    @OneToMany(mappedBy = "member", orphanRemoval = true)
    private List<MemberCard> memberCards;

    @OneToMany(mappedBy = "member", orphanRemoval = true)
    private List<MemberCustomCard> memberCustomCardList;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updateAt;

    public Member updateRefreshToken(String reIssueRefreshToken) {
        this.setRefreshToken(reIssueRefreshToken);
        return this;
    }

    public Member rewardStamp() {
        this.setStampCount(this.getStampCount() + 1);
        return this;
    }
}
