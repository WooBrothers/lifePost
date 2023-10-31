import {bindEventToLetterListGrid} from "./letterListEvent.js";

import {createOpenLetterListSpace} from "./letterList.js";

await createIndexLetterList()

async function createIndexLetterList() {

    // 편지 추가 대상 찾기
    const indexLetterSpace = document.querySelector("#letter-list-space");
    // 편지 조회하기

    // 편지 그리기
    await createOpenLetterListSpace(indexLetterSpace, 1, bindEventToLetterListGrid);
}

