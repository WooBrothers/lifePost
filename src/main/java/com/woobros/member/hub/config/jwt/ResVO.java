package com.woobros.member.hub.config.jwt;

import java.util.ArrayList;
import lombok.Builder;
import lombok.ToString;

@ToString
@Builder
public class ResVO {

    /*공통 응답 처리를 위한 클래스*/

    private final int status;
    private final String code;
    private final String message;
    private final Integer size;
    private final ArrayList<?> items;

}
