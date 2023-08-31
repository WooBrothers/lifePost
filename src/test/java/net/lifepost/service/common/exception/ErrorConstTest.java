package net.lifepost.service.common.exception;

import org.junit.jupiter.api.Test;

class ErrorConstTest {

    @Test
    void testGetErrorMessage() {

        String givenFieldName = "name";
        String givenErrorTypeName = "NotBlank";

        try {
            String actual = ErrorConst.getErrorMessage(givenFieldName, givenErrorTypeName);
            String expect = givenFieldName + ErrorConst.NOT_BLANK;

            assert expect.equals(actual);

        } catch (Exception e) {
            assert false;
        }
    }
}