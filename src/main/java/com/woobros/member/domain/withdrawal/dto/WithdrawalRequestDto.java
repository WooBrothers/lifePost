package com.woobros.member.domain.withdrawal.dto;

import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WithdrawalRequestDto {

    private static final String ENUM_NAME = "WithdrawErrorEnum.";

    @NotNull(message = ENUM_NAME + "ID_NOT_NULL")
    private final int id;
}
