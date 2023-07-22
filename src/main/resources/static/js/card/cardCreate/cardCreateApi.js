import {authFetch} from "../../common/apiUtil.js";

export async function createCardApiCall(cardContents) {
    const body = JSON.stringify({contents: cardContents});
    const options = {method: "POST", body: body};

    const url = "/api/v1/card/auth/member/custom";

    await authFetch(url, options).then(res => {
        return res.text;
    });
}