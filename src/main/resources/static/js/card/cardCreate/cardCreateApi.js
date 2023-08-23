import {authFetch} from "../../common/apiUtil.js";

export async function createCardApiCall(cardContents, memberCardId, type) {
    const body = JSON.stringify({
        "contents": cardContents,
        "cardId": memberCardId,
        "type": type
    });
    const options = {method: "POST", body: body};

    const url = "/api/v1/card/auth/member/custom";

    await authFetch(url, options).then(res => {
        return res.text;
    });
}