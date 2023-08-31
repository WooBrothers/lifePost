package net.lifepost.service.domain.letter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.lifepost.service.model.letter.Letter;
import net.lifepost.service.model.letter.LetterMapper;
import net.lifepost.service.model.letter.LetterMapperImpl;
import net.lifepost.service.model.letter.LetterTagEnum;
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
            Letter.builder().writer("tester").title("test1").tag(LetterTagEnum.TEST)
                .contents("test1").build(),
            Letter.builder().writer("tester").title("test2").tag(LetterTagEnum.TEST)
                .contents("test2").build(),
            Letter.builder().writer("tester").title("test3").tag(LetterTagEnum.TEST)
                .contents("test3").build(),
            Letter.builder().writer("tester").title("test4").tag(LetterTagEnum.TEST)
                .contents("test4").build()
        ).collect(Collectors.toList());

        // given from test data
        Page<Letter> given = new PageImpl<>(letters);

        // Page<Entity> to Page<Dto> convert logic result
        Page<LetterDto.ReadResponse> actual = new PageImpl<>(given.stream()
            .map(letterMapper::toResponseDto).collect(Collectors.toList()));

        // List<Entity> to Page<Dto> convert logic result
        Page<LetterDto.ReadResponse> expected = new PageImpl<>(
            letters.stream().map(letterMapper::toResponseDto)
                .collect(Collectors.toList()));

        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }
}