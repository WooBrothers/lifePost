import {authFetch} from "../../common/apiUtil.js";

export async function createCardApiCall(title, cardImg, cardContents, memberCardId) {
    const body = JSON.stringify({
        "title": title,
        "cardImg": cardImg,
        "contents": cardContents,
        "cardId": memberCardId,
    });

    const options = {method: "POST", body: body};

    const url = "/api/v1/card/auth/member/custom";

    await authFetch(url, options).then(res => {
        return res.text;
    });
}

export async function deleteCardApiCall(cardId, type) {
    const url = "/api/v1/card/auth/member/custom/delete";

    const body = {
        "cardId": cardId,
        "type": type
    };

    let options = {
        method: "POST",
        body: JSON.stringify(body)
    };

    await authFetch(url, options).then(res => {
        return res;
    });
}

export async function rewardStampToUser() {

    const url = "/api/v1/card/auth/write_count/reward";

    let option = {
        method: "POST",
    };

    await authFetch(url, option);
}

export async function increaseCardWriteCount(memberCardId, count) {
    const url = "/api/v1/card/auth/write_count";

    const body = {"memberCardId": memberCardId, "count": count};

    let option = {
        method: "POST",
        body: JSON.stringify(body)
    };

    await authFetch(url, option);
}
