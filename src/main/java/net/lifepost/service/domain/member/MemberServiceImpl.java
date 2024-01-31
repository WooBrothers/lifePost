package net.lifepost.service.domain.member;

import java.util.List;
import lombok.RequiredArgsConstructor;
import net.lifepost.service.common.Common;
import net.lifepost.service.model.card.memb_card.MemberCard;
import net.lifepost.service.model.card.memb_card.MemberCardRepository;
import net.lifepost.service.model.card.memb_cust_card.MemberCustomCard;
import net.lifepost.service.model.card.memb_cust_card.MemberCustomCardRepository;
import net.lifepost.service.model.member.Member;
import net.lifepost.service.model.member.MemberDto;
import net.lifepost.service.model.member.MemberMapper;
import net.lifepost.service.model.member.MemberRepository;
import net.lifepost.service.model.member_letter.MemberLetter;
import net.lifepost.service.model.member_letter.MemberLetterRepository;
import net.lifepost.service.model.stamp.Stamp;
import net.lifepost.service.model.stamp.StampRepository;
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
