package com.woobros.member.hub.model.card;

public enum CardTypeEnum {

    AFFIRMATION("affirmation"),
    MEMBER("member"),
    FOCUS("focus"),
    ;

    private String type;

    CardTypeEnum(String type) {
        this.type = type;
    }
}
