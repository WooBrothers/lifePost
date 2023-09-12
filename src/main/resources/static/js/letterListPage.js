import {checkAndRefreshToken} from "./common/apiUtil.js";
import {createLetterListSpace} from "./letter/list/letterListGrid.js";
import {bindEventToLetterListGrid} from "./letter/list/letterListEvent.js";
import {createPagination} from "./pagination/pagination.js";
import {bindPaginationBtnEvent} from "./pagination/paginationEvent.js";

window.onload = async () => {
    /* view 생성 */
    await checkAndRefreshToken();
    const letterListSpace = document.getElementById("letter-list-space");
    const response = await createLetterListSpace(letterListSpace, 1, bindEventToLetterListGrid);

    const letterListPaginationSpace = document.getElementById("pagination-space");
    await createPagination(response, letterListPaginationSpace);

    bindPaginationBtnEvent("letter-list-space", "letter-space", createLetterListSpace, bindEventToLetterListGrid);
}