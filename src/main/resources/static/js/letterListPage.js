import {checkAndRefreshToken} from "./common/apiUtil.js";
import {createLetterListSpace, createOpenLetterListSpace} from "./letter/list/letterList.js";
import {bindEventToLetterListGrid} from "./letter/list/letterListEvent.js";
import {createPagination} from "./pagination/pagination.js";
import {bindPaginationBtnEvent} from "./pagination/paginationEvent.js";
import {isTokenExpired} from "./common/utilTool.js";

window.onload = async () => {
    /* view 생성 */
    await checkAndRefreshToken();

    const letterListSpace = document.getElementById("letter-list-space");

    let response, renderFunction;
    debugger
    if (isTokenExpired()) {
        // 비로그인 편지 리스트 출력
        response = await createOpenLetterListSpace(letterListSpace, 1, bindEventToLetterListGrid);
        renderFunction = createOpenLetterListSpace;
    } else {
        // 로그인 편지 리스트 출력
        response = await createLetterListSpace(letterListSpace, 1, bindEventToLetterListGrid);
        renderFunction = createLetterListSpace;
    }

    const letterListPaginationSpace = document.getElementById("pagination-space");
    await createPagination(response, letterListPaginationSpace);

    bindPaginationBtnEvent("letter-list-space", "letter-space", renderFunction, bindEventToLetterListGrid);
}