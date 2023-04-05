package com.woobros.member.hub;

import com.woobros.member.hub.common.exception.MemberErrorEnum;
import com.woobros.member.hub.common.exception.MemberErrorResponse;
import com.woobros.member.hub.common.exception.MemberException;
import com.woobros.member.hub.domain.login.exception.LoginMemberErrorEnum;
import com.woobros.member.hub.domain.logout.exception.LogoutMemberErrorEnum;
import com.woobros.member.hub.domain.modify.exception.ModifyMemberErrorEnum;
import com.woobros.member.hub.domain.signup.exception.SignUpMemberErrorEnum;
import com.woobros.member.hub.domain.withdrawal.exception.WithdrawMemberErrorEnum;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class ControllerExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);
    private static final String INVALID_PARAM_EXCEPTION = "Invalid parameter Exception occurred. ";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<MemberErrorResponse> handleInvalidParameterException(
        MethodArgumentNotValidException e) {
        /* DTO @Valid 예외 핸들러 */

        logger.error(ControllerExceptionHandler.INVALID_PARAM_EXCEPTION, e);

        Map<String, String> errorCodeMsgMap = new HashMap<>();
        List<String> errorCodeList = new ArrayList<>();

        BindingResult bindResult = e.getBindingResult();
        for (FieldError fieldError : bindResult.getFieldErrors()) {
            /* 예외 발생 message 양식 : [열거형 에러 이름].[에러 이름] */
            String errorMsg = Optional.ofNullable(fieldError.getDefaultMessage())
                .orElse("NotDefineEnumClass.NOT_DEFINE_MSG");

            List<String> errorMsgLit = Arrays.asList(errorMsg.split("\\."));
            String enumName = errorMsgLit.get(0);
            String errorName = errorMsgLit.get(1);

            MemberErrorEnum memberErrorEnum = switch (enumName) {
                case "LoginErrorEnum" -> LoginMemberErrorEnum.valueOf(errorName);
                case "LogoutErrorEnum" -> LogoutMemberErrorEnum.valueOf(errorName);
                case "ModifyErrorEnum" -> ModifyMemberErrorEnum.valueOf(errorName);
                case "SignUpErrorEnum" -> SignUpMemberErrorEnum.valueOf(errorName);
                case "WithdrawErrorEnum" -> WithdrawMemberErrorEnum.valueOf(errorName);
                default -> throw new RuntimeException("error enum is not defined.");
            };

            errorCodeMsgMap.put(memberErrorEnum.getErrorCode(), memberErrorEnum.getMessage());
            errorCodeList.add(memberErrorEnum.getErrorCode());
        }

        /* 파라미터 에러가 다수 발생 했을 경우 -> 첫번째 에러 메시지와 코드를 리턴한다. */
        MemberErrorResponse memberErrorResponse = MemberErrorResponse.builder()
            .errorMessage(errorCodeMsgMap.get(errorCodeList.get(0)))
            .errorCode(errorCodeList.get(0))
            .fieldErrorCodeMsgMap(errorCodeMsgMap)
            .fieldErrorCodes(errorCodeList)
            .build();

        return ResponseEntity.badRequest().body(memberErrorResponse);
    }

    @ExceptionHandler(MemberException.class)
    protected ResponseEntity<MemberErrorResponse> handleMemberException(MemberException e) {
        /* MemberException 인터페이스 구현 예외 핸들러 */

        logger.error(e.getErrorMessage());

        MemberErrorResponse memberErrorResponse = MemberErrorResponse.builder()
            .errorMessage(e.getMessage())
            .errorCode(e.getErrorCode())
            .build();

        HttpStatus status = HttpStatus.valueOf(e.getHttpStatusCode());

        return ResponseEntity.status(status).body(memberErrorResponse);
    }
}
