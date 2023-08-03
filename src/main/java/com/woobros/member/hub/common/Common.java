package com.woobros.member.hub.common;

import com.woobros.member.hub.common.exception.CommonException;
import com.woobros.member.hub.common.exception.ErrorEnum;

public class Common {

    public static int verifyPageNo(int pageNo) {
        if (pageNo > 0) {
            pageNo--;
        } else if (pageNo < 0) {
            throw new CommonException(ErrorEnum.PAGE_NO_INVALID);
        }
        return pageNo;
    }
    
}
