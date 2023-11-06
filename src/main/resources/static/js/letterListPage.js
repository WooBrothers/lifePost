import {checkAndRefreshToken} from "./common/apiUtil.js";
import {createLetterListSpace} from "./letter/list/letterList.js";
import {bindEventToLetterListGrid} from "./letter/list/letterListEvent.js";
import {isTokenExpired} from "./common/utilTool.js";

window.onload = async () => {
    /* view 생성 */
    await checkAndRefreshToken();

    const letterListSpace = document.getElementById("letter-list-space");

    if (isTokenExpired()) {
        window.location = "/login/page";
    } else {
        await createLetterListSpace(letterListSpace, 0, bindEventToLetterListGrid);
    }
}