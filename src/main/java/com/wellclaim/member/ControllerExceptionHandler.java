package com.wellclaim.member;

import com.wellclaim.member.common.MemberException;
import com.wellclaim.member.common.exception.ErrorEnum;
import com.wellclaim.member.common.exception.ErrorResponse;
import com.wellclaim.member.domain.login.exception.LoginErrorEnum;
import com.wellclaim.member.domain.logout.exception.LogoutErrorEnum;
import com.wellclaim.member.domain.modify.exception.ModifyErrorEnum;
import com.wellclaim.member.domain.signup.exception.SignUpErrorEnum;
import com.wellclaim.member.domain.withdraw.exception.WithdrawErrorEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;


@RestControllerAdvice
public class ControllerExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);
    private static final String INVALID_PARAM_EXCEPTION = "Invalid parameter Exception occurred. ";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleInvalidParameterException(MethodArgumentNotValidException e) {
        /* DTO @Valid 예외 핸들러 */

        logger.error(ControllerExceptionHandler.INVALID_PARAM_EXCEPTION, e);

        Map<String,String> errorCodeMsgMap = new HashMap<>();
        List<String> errorCodeList = new ArrayList<>();

        BindingResult bindResult = e.getBindingResult();
        for (FieldError fieldError : bindResult.getFieldErrors()) {
            /* 예외 발생 message 양식 : [열거형 에러 이름].[에러 이름] */
            String errorMsg = Optional.ofNullable(fieldError.getDefaultMessage())
                    .orElse("NotDefineEnumClass.NOT_DEFINE_MSG");

            List<String> errorMsgLit = Arrays.asList(errorMsg.split("\\."));
            String enumName = errorMsgLit.get(0);
            String errorName = errorMsgLit.get(1);

            ErrorEnum errorEnum = switch (enumName) {
                case "LoginErrorEnum" -> LoginErrorEnum.valueOf(errorName);
                case "LogoutErrorEnum" -> LogoutErrorEnum.valueOf(errorName);
                case "ModifyErrorEnum" -> ModifyErrorEnum.valueOf(errorName);
                case "SignUpErrorEnum" -> SignUpErrorEnum.valueOf(errorName);
                case "WithdrawErrorEnum" -> WithdrawErrorEnum.valueOf(errorName);
                default -> throw new RuntimeException("error enum is not defined.");
            };

            errorCodeMsgMap.put(errorEnum.getErrorCode(), errorEnum.getMessage());
            errorCodeList.add(errorEnum.getErrorCode());
        }

        /* 파라미터 에러가 다수 발생 했을 경우 -> 첫번째 에러 메시지와 코드를 리턴한다. */
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorMessage(errorCodeMsgMap.get(errorCodeList.get(0)))
                .errorCode(errorCodeList.get(0))
                .fieldErrorCodeMsgMap(errorCodeMsgMap)
                .fieldErrorCodes(errorCodeList)
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(MemberException.class)
    protected ResponseEntity<ErrorResponse> handleMemberException(MemberException e) {
        /* MemberException 인터페이스 구현 예외 핸들러 */

        logger.error(e.getErrorMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorMessage(e.getMessage())
                .errorCode(e.getErrorCode())
                .build();

        HttpStatus status = HttpStatus.valueOf(e.getHttpStatusCode());

        return ResponseEntity.status(status).body(errorResponse);
    }
}
