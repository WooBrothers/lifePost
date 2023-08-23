import {authFetch} from "../../common/apiUtil.js";

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