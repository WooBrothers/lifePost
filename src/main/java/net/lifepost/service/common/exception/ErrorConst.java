package net.lifepost.service.common.exception;

import java.lang.reflect.Field;
import java.util.Arrays;

public class ErrorConst {

    private ErrorConst() {
    }

    /* error code */
    public static final String DTO_FIELD_ERROR_CODE = "DTO_FILED_ERROR";

    /* @Valid error msg
     * DTO 검증 @Valid 시 사용하는 어노테이션에 따른 에러 메시지 정의
     * */
    public static final String ASSERT_FALSE = " must be false.";
    public static final String ASSERT_TRUE = " must be true.";
    public static final String DECIMAL_MAX = " out of decimal max.";
    public static final String DECIMAL_MIN = " out of decimal min.";
    public static final String DIGITS = " out of digits type limit.";
    public static final String EMAIL = " invalid email type.";
    public static final String FUTURE = " must be after now.";
    public static final String FUTURE_OR_PRESENT = " must be after now or now.";
    public static final String MAX = "ou t of max.";
    public static final String MIN = "ou t of min.";
    public static final String NEGATIVE = " must be negative(-).";
    public static final String NEGATIVE_OR_ZERO = " must be zero or negative(-).";
    public static final String NOT_BLANK = " must be not null or not blank.";
    public static final String NOT_EMPTY = " must not be null or not empty.";
    public static final String NOT_NULL = " must not be null.";
    public static final String NULL = " must be null.";
    public static final String PAST = " must be before now.";
    public static final String PAST_OR_PRESENT = " must be before now or now.";
    public static final String PATTERN = " must be satisfied regular expression condition.";
    public static final String POSITIVE = " must be positive(+).";
    public static final String POSITIVE_OR_ZERO = " must be positive(+) or zero.";
    public static final String SIZE = " out of size.";

    /* methods */
    public static String getErrorMessage(String fieldName, String errorType)
        throws IllegalAccessException {
        /* dto 매핑시 문제된 필드이름과 에러 타입을 전달하면 에러메시지를 리턴 */

        return fieldName + ErrorConst.getErrorTypeMsgByType(errorType);
    }

    private static String getErrorTypeMsgByType(String errorType) throws IllegalAccessException {
        /* 전달받은 에러 메시지 타입으로 현 클래스의 필드와 매칭되는 에러 메시지 리턴 */

        Field[] fields = ErrorConst.class.getFields();

        int fieldIndex = Arrays.asList(Arrays.stream(fields)
            .map(Field::getName)
            .toArray())
            .indexOf(ErrorConst.convertToUnderscoreCase(errorType));

        String errorTypeMsg = "";

        if (fieldIndex != -1) {
            errorTypeMsg = (String) fields[fieldIndex].get(null);
        }

        return errorTypeMsg;
    }

    private static String convertToUnderscoreCase(String errorType) {
        /* 스네이크 케이스 문자를 대문자 언더스코어 케이스로 변환 NotBlank -> NOT_BLANK */

        return errorType.replaceAll("\\B(?!^)([A-Z])", "_$1")
            .toUpperCase();
    }
}
