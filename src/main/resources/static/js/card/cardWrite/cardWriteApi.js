import {authFetch} from "../../common/apiUtil.js";
import {TodayCardWriteHistory} from "../../common/utilTool.js";

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

export async function increaseWriteCount(cardId) {

    const todayCardWriteHistory = new TodayCardWriteHistory();

    const memberCardId = cardId;

    todayCardWriteHistory.increaseWriteCount(memberCardId).save()

    document.querySelector("#today-total-write-count")
        .innerHTML = "오늘 쓴 총 횟수:" + todayCardWriteHistory.totalCount;

    await increaseCardWriteCount(memberCardId, 1);

    document.querySelector("#card-write-count").innerHTML
        = "이 카드를 쓴 횟수: " + todayCardWriteHistory.memberCards[memberCardId];

    if (todayCardWriteHistory.isTotalCountMoreThanHundred()) {
        await rewardStampToUser();
    }
}