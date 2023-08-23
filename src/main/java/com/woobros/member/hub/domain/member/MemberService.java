package com.woobros.member.hub.domain.member;

import com.woobros.member.hub.model.member.MemberDto;
import org.springframework.security.core.userdetails.UserDetails;

public interface MemberService {

    MemberDto.InfoResponse getMemberInfo(UserDetails userDetails);

    MemberDto getMembershipInfo(UserDetails userDetails);

    MemberDto getPaymentInfo(UserDetails userDetails);

    void withdraw(UserDetails userDetails);
}
