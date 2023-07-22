import {authFetch} from "../../common/apiUtil.js";

export function increaseWriteCount(memberCardId, count) {
    const url = "/api/v1/card/auth/write/count";

    const body = {
        memberCardId: memberCardId,
        count: count
    }

    let option = {
        method: "PATCH",
        body: JSON.stringify(body)
    };


    authFetch(url, option).then(
        res => {
            console.log(res);
        }
    )
}
