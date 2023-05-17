package com.woobros.member.hub.domain.letter;

import com.woobros.member.hub.model.letter.Letter;
import com.woobros.member.hub.model.letter.LetterDto;
import com.woobros.member.hub.model.letter.LetterMapper;
import com.woobros.member.hub.model.letter.LetterMapperImpl;
import com.woobros.member.hub.model.letter.LetterTageEnum;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

class LetterServiceImplTest {

    private final LetterMapper letterMapper = new LetterMapperImpl();

    @Test
    void testPageEntityToPageDto() {

        // test data
        List<Letter> letters = Stream.of(
            Letter.builder().writer("tester").title("test1").tag(LetterTageEnum.TEST)
                .contents("test1").build(),
            Letter.builder().writer("tester").title("test2").tag(LetterTageEnum.TEST)
                .contents("test2").build(),
            Letter.builder().writer("tester").title("test3").tag(LetterTageEnum.TEST)
                .contents("test3").build(),
            Letter.builder().writer("tester").title("test4").tag(LetterTageEnum.TEST)
                .contents("test4").build()
        ).collect(Collectors.toList());

        // given from test data
        Page<Letter> given = new PageImpl<>(letters);

        // Page<Entity> to Page<Dto> convert logic result
        Page<LetterDto.Response> actual = new PageImpl<>(given.stream()
            .map(letterMapper::toResponseDto).collect(Collectors.toList()));

        // List<Entity> to Page<Dto> convert logic result
        Page<LetterDto.Response> expected = new PageImpl<>(
            letters.stream().map(letterMapper::toResponseDto)
                .collect(Collectors.toList()));

        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }
}