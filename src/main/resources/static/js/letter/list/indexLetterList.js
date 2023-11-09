import {createOpenLetterListSpace} from "./letterList.js";
import {bindEventToIndexLetterPage} from "./indexLetterEvent.js";
import {calculateDateOffset} from "../../common/utilTool.js";

await createIndexLetterList()

async function createIndexLetterList() {

    // 편지 추가 대상 찾기
    const indexLetterSpace = document.querySelector("#letter-list-space");

    // 온보딩 버튼 이벤트 바인딩
    const onboardingBtn = document.querySelector("#service-onboarding-btn");
    onboardingBtn.addEventListener("click", clickOnboardingBtn);

    // 편지 그리기
    await createOpenLetterListSpace(indexLetterSpace, calculateDateOffset(new Date(), 1), bindEventToIndexLetterPage);
}

function clickOnboardingBtn() {
    const modal = document.querySelector("#lifepost-onboarding-modal");
    new bootstrap.Modal(modal).show();
}
