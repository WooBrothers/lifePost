package com.woobros.member.hub;

import com.woobros.member.hub.common.exception.MemberErrorEnum;
import com.woobros.member.hub.common.exception.MemberErrorResponse;
import com.woobros.member.hub.common.exception.MemberException;
import com.woobros.member.hub.domain.index.exception.LoginMemberErrorEnum;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {

    private static final String INVALID_PARAM_EXCEPTION = "Invalid parameter Exception occurred. ";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<MemberErrorResponse> handleInvalidParameterException(
        MethodArgumentNotValidException e) {
        /* DTO @Valid 예외 핸들러 */

        log.error(ControllerExceptionHandler.INVALID_PARAM_EXCEPTION, e);

        Map<String, String> errorCodeMsgMap = new HashMap<>();
        List<String> errorCodeList = new ArrayList<>();

        BindingResult bindResult = e.getBindingResult();
        MemberErrorEnum memberErrorEnum;

        for (FieldError fieldError : bindResult.getFieldErrors()) {
            /* 예외 발생 message 양식 : [열거형 에러 이름].[에러 이름] */
            String errorMsg = Optional.ofNullable(fieldError.getDefaultMessage())
                .orElse("NotDefineEnumClass.NOT_DEFINE_MSG");

            List<String> errorMsgLit = Arrays.asList(errorMsg.split("\\."));
            String enumName = errorMsgLit.get(0);
            String errorName = errorMsgLit.get(1);

            switch (enumName) {
                case "LoginErrorEnum":
                    memberErrorEnum = LoginMemberErrorEnum.valueOf(errorName);
                    break;
                case "LogoutErrorEnum":
                    memberErrorEnum = LogoutMemberErrorEnum.valueOf(errorName);
                    break;
                case "ModifyErrorEnum":
                    memberErrorEnum = ModifyMemberErrorEnum.valueOf(errorName);
                    break;
                case "SignUpErrorEnum":
                    memberErrorEnum = SignUpMemberErrorEnum.valueOf(errorName);
                    break;
                case "WithdrawErrorEnum":
                    memberErrorEnum = WithdrawMemberErrorEnum.valueOf(errorName);
                    break;
                default:
                    throw new RuntimeException("error enum is not defined.");
            }

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

        log.error(e.getErrorMessage());
        MemberErrorResponse memberErrorResponse = MemberErrorResponse.builder()
            .errorMessage(e.getMessage())
            .errorCode(e.getErrorCode())
            .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(memberErrorResponse);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolationException(
        /* JPA 데이터 중복 생성 실패 에러 처리 */
        DataIntegrityViolationException ex) {

        return ResponseEntity.status(HttpStatus.CONFLICT).body("Duplicate data exists");
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleAllException(Exception e) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMap);
    }
}
