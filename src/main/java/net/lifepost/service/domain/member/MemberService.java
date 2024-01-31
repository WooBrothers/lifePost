package net.lifepost.service.domain.member;

import net.lifepost.service.model.member.MemberDto;
import org.springframework.security.core.userdetails.UserDetails;

public interface MemberService {

    MemberDto.InfoResponse getMemberInfo(UserDetails userDetails);

    void withdraw(UserDetails userDetails);
}
