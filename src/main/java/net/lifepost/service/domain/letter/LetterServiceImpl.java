package net.lifepost.service.domain.letter;

import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.lifepost.service.common.Common;
import net.lifepost.service.common.exception.CommonException;
import net.lifepost.service.common.exception.ErrorEnum;
import net.lifepost.service.domain.card.FocusTypeEnum;
import net.lifepost.service.domain.letter.LetterDto.PageResponse;
import net.lifepost.service.domain.letter.LetterDto.PostFocusRequest;
import net.lifepost.service.model.letter.Letter;
import net.lifepost.service.model.letter.LetterMapper;
import net.lifepost.service.model.letter.LetterRepository;
import net.lifepost.service.model.member.Member;
import net.lifepost.service.model.member_letter.MemberLetter;
import net.lifepost.service.model.member_letter.MemberLetterRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class LetterServiceImpl implements LetterService {

    /* beans */
    private final Common common;
    private final LetterRepository letterRepository;
    private final MemberLetterRepository memberLetterRepository;
    private final LetterMapper letterMapper;

    @Override
    public Page<PageResponse> getMemberLetterList(Long letterId, int size,
        List<FocusTypeEnum> focusTypeList, UserDetails userDetails) {

        Pageable pageable = PageRequest.of(0, size);

        Member member = common.getMemberByUserDetail(userDetails);

        Page<MemberLetter> memberLetters;
        if (letterId == 0) {
            memberLetters = memberLetterRepository
                .findByMemberIdAndFocusInOrderByLetterIdDesc(member.getId(), focusTypeList,
                    pageable);
        } else {
            memberLetters = memberLetterRepository
                .findByMemberIdAndFocusInAndLetterIdLessThanOrderByLetterIdDesc(
                    member.getId(), focusTypeList, letterId, pageable
                );
        }

        List<PageResponse> pageResponses = new ArrayList<>();
        memberLetters.forEach(memberLetter -> {
            PageResponse pageResponse = letterMapper.toPageResponseDto(memberLetter.getLetter());
            pageResponse
                .setFocusType(memberLetter.getFocus())
                .setLimitedContent(memberLetter.getLetter().getContents())
                .setMemberLetterId(memberLetter.getId());

            pageResponses.add(pageResponse);
        });

        return new PageImpl<>(pageResponses, pageable, memberLetters.getTotalElements());
    }

    @Override
    public Page<PageResponse> getIndexLetterList(LocalDate postDate, int size) {
        Pageable pageable = PageRequest.of(0, size);

        Page<Letter> letters;
        LocalDate tomorrow = LocalDate.now().plusDays(1);

        if (postDate.isBefore(tomorrow) || postDate.equals(tomorrow)) {
            letters = letterRepository
                .findByPostDateBeforeOrderByPostDateDesc(postDate, pageable);
        } else {
            throw new CommonException(ErrorEnum.POST_DATE_INVALID);
        }

        List<PageResponse> pageResponses = getPageResponse(letters);

        return new PageImpl<>(pageResponses, pageable, letters.getTotalElements());
    }

    @Override
    public void postFocusLetter(PostFocusRequest focusCardRequest, UserDetails userDetails) {
        Member member = common.getMemberByUserDetail(userDetails);

        MemberLetter memberLetter = memberLetterRepository.findByMemberIdAndLetterId(member.getId(),
            focusCardRequest.getLetterId())
            .filter(
                ml -> ml.getId().equals(focusCardRequest.getMemberLetterId()))
            .orElseThrow(() -> new CommonException(ErrorEnum.NOT_FOUND));

        memberLetter.focusLetter(focusCardRequest.getFocusType());
    }

    /**
     * [open] 비로그인 편지 읽기 시 제한된 컨텐츠 전달
     *
     * @param letterId - 읽으려는 편지의 아이디
     */
    @Override
    public LetterDto.ReadResponse getOpenLetterContents(Long letterId) {

        Letter letter = letterRepository.findById(letterId)
            .orElseThrow(() -> new CommonException(ErrorEnum.LETTER_REQUEST_INVALID));

        return letterMapper.toResponseDto(letter);
    }

    private List<PageResponse> getPageResponse(Page<Letter> letters) {
        List<PageResponse> pageResponses = new ArrayList<>();

        letters.forEach(letter -> {
                PageResponse pageResponse = letterMapper.toPageResponseDto(letter)
                    .setLimitedContent(letter.getContents());
                pageResponses.add(pageResponse);
            }
        );

        return pageResponses;
    }
}
