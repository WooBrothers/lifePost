package com.wellclaim.member.domain.withdrawal.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@Builder
public class WithdrawalResponseDto {
    private static final String ENUM_NAME = "WithdrawErrorEnum.";

    @NotNull(message = ENUM_NAME + "ID_NOT_NULL")
    private final int id;
}
