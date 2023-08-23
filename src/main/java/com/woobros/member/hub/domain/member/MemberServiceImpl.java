package com.woobros.member.hub.domain.member;

import com.woobros.member.hub.common.Common;
import com.woobros.member.hub.model.card.memb_card.MemberCard;
import com.woobros.member.hub.model.card.memb_card.MemberCardRepository;
import com.woobros.member.hub.model.card.memb_cust_card.MemberCustomCard;
import com.woobros.member.hub.model.card.memb_cust_card.MemberCustomCardRepository;
import com.woobros.member.hub.model.member.Member;
import com.woobros.member.hub.model.member.MemberDto;
import com.woobros.member.hub.model.member.MemberMapper;
import com.woobros.member.hub.model.member.MemberRepository;
import com.woobros.member.hub.model.member_letter.MemberLetter;
import com.woobros.member.hub.model.member_letter.MemberLetterRepository;
import com.woobros.member.hub.model.stamp.Stamp;
import com.woobros.member.hub.model.stamp.StampRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberMapper memberMapper;
    private final Common common;
    private final StampRepository stampRepository;
    private final MemberCardRepository memberCardRepository;
    private final MemberCustomCardRepository memberCustomCardRepository;
    private final MemberLetterRepository memberLetterRepository;
    private final MemberRepository memberRepository;


    @Override
    public MemberDto.InfoResponse getMemberInfo(UserDetails userDetails) {
        Member member = common.getMemberByUserDetail(userDetails);

        return memberMapper
            .toInfoResponse(member)
            .setSignInDate(member.getCreatedAt());
    }

    @Override
    public MemberDto getMembershipInfo(UserDetails userDetails) {
        return null;
    }

    @Override
    public MemberDto getPaymentInfo(UserDetails userDetails) {
        return null;
    }

    @Override
    public void withdraw(UserDetails userDetails) {
        Member member = common.getMemberByUserDetail(userDetails);

        List<Stamp> stamps = stampRepository.findByMemberId(member.getId());
        stampRepository.deleteAll(stamps);

        List<MemberCard> memberCards = memberCardRepository.findByMemberId(member.getId());
        memberCardRepository.deleteAll(memberCards);

        List<MemberCustomCard> memberCustomCards = memberCustomCardRepository
            .findByMemberId(member.getId());
        memberCustomCardRepository.deleteAll(memberCustomCards);

        List<MemberLetter> memberLetters = memberLetterRepository.findByMemberId(member.getId());
        memberLetterRepository.deleteAll(memberLetters);

        memberRepository.delete(member);

        // 탈퇴 계정 정보 다른 DB 이관
        // 결제 기록은 보관 필요
        // 추후에 고려
    }
}
