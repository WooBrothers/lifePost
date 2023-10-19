package net.lifepost.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import net.lifepost.service.common.exception.CommonException;
import net.lifepost.service.common.exception.ErrorConst;
import net.lifepost.service.common.exception.ErrorResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleInvalidParameterException(
        MethodArgumentNotValidException e) throws IllegalAccessException {
        /* dto 인자들이 매핑시에 문제가 발생했을 때 에러를 핸들하는 메소드, */

        log.error(e.getMessage());
        e.printStackTrace();

        FieldError fieldError = e.getBindingResult().getFieldError();

        List<String> errorInfos = Arrays.asList(
            Optional.ofNullable(fieldError)
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .map(errorMsgChunk -> errorMsgChunk.split("@"))
                .orElseThrow(() -> new RuntimeException(
                    "During dto validation exception handler, dto error message empty exception occur.")
                )
        );

        String fieldName = errorInfos.get(0);
        String errorType = errorInfos.get(1);
        String errorMsg = ErrorConst.getErrorMessage(fieldName, errorType);

        ErrorResponse memberErrorResponse = ErrorResponse.builder()
            .errorMessage(errorMsg)
            .errorCode(ErrorConst.DTO_FIELD_ERROR_CODE)
            .build();

        return ResponseEntity.badRequest().body(memberErrorResponse);
    }

    @ExceptionHandler(CommonException.class)
    public ResponseEntity<ErrorResponse> handleCommonException(CommonException e) {
        /* domain 로직중 발생하는 예외를 처리하는 공통 에러 핸들러 */

        log.error("[Error Msg] " + e.getMessage());
        log.error("[Error Code] " + e.getErrorCode());
        e.printStackTrace();

        ErrorResponse errorResponse = ErrorResponse.builder()
            .errorMessage(e.getMessage())
            .errorCode(e.getErrorCode())
            .build();

        return ResponseEntity.status((e.getHttpStatusCode())).body(errorResponse);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolationException(
        /* JPA 데이터 중복 생성 실패 에러 처리 */
        DataIntegrityViolationException ex) {

        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Duplicate data exists");
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleAllException(Exception e) {
        Map<String, String> errorMap = new HashMap<>();

        e.printStackTrace();
        errorMap.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMap);
    }
}
