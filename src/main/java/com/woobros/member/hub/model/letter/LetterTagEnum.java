package com.woobros.member.hub.model.letter;

public enum LetterTagEnum {

    ADVICE("조언"),
    AFFIRMATION("확언"),
    SUCCESS("성공"),
    HAPPINESS("행복"),
    DETERMINATION("마음가짐"),
    TEST("테스트"),
    ;

    private String tag;

    LetterTagEnum(String tag) {
        this.tag = tag;
    }
}
