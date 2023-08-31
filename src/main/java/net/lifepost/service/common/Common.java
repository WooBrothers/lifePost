package net.lifepost.service.common;

import lombok.RequiredArgsConstructor;
import net.lifepost.service.common.exception.CommonException;
import net.lifepost.service.common.exception.ErrorEnum;
import net.lifepost.service.model.member.Member;
import net.lifepost.service.model.member.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class Common {

    private final MemberRepository memberRepository;

    public int verifyPageNo(int pageNo) {
        if (pageNo > 0) {
            pageNo--;
        } else if (pageNo < 0) {
            throw new CommonException(ErrorEnum.PAGE_NO_INVALID);
        }
        return pageNo;
    }

    /**
     * userDetail 을 이용해 유저 조회
     *
     * @param userDetails security 멤버 정보
     * @return member entity
     */
    public Member getMemberByUserDetail(UserDetails userDetails) {
        return memberRepository.findByEmail(userDetails.getUsername()).orElseThrow(() ->
            new CommonException(ErrorEnum.NOT_FOUND));
    }
}
