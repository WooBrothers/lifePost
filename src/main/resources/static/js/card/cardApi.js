import {authFetch, getAccessToken} from "../common/apiUtil.js";

/* card 관련 API class */

const cardSchema = "/api/v1/card";

export async function getFocusCards(pageNo) {
    const url = `${cardSchema}/auth/focus/${pageNo}/10`;

    let option = {
        method: 'GET'
    };

    if (getAccessToken()) {
        return await authFetch(url, option);
    } else {
        return null;
    }
}

export async function getCardListAfterCardId(memberCardId, size) {

    const url = `${cardSchema}/auth/focus/after/${memberCardId}/${size}`

    let option = {
        method: "GET"
    };

    if (getAccessToken()) {
        return await authFetch(url, option);
    } else {
        return null;
    }
}