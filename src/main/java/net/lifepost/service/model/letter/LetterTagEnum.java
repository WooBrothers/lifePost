package net.lifepost.service.model.letter;

public enum LetterTagEnum {

    ADVICE("advice"),
    AFFIRMATION("affirmation"),
    SUCCESS("success"),
    HAPPINESS("happiness"),
    DETERMINATION("determination"),
    TEST("test"),
    ;

    private String tag;

    LetterTagEnum(String tag) {
        this.tag = tag;
    }
}
