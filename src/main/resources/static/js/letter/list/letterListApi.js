import {authFetch} from "../../common/apiUtil.js";

export async function getLetterList(pageNo, size, type, focusTypeList) {

    const url = `/api/v1/letter/auth/member/${pageNo}/${size}?type=${type}&focusType=${focusTypeList}`;
    let option = {
        method: "GET",
    }

    return await authFetch(url, option);
}