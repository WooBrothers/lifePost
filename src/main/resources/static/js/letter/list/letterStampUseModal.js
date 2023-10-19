import {readLetterPage} from "../../common/utilTool.js";

export function bindEventToLetterStampUsePage(letterId) {
    const letterReadBtn = document.getElementById("letter-read-btn");
    letterReadBtn.addEventListener("click", (event) =>
        useStampAndReadLetter.call(event, letterId));

    const goToCardListBtn = document.getElementById("go-to-card-list-btn");
    goToCardListBtn.addEventListener("click", clickGoToCardListPage);
}

async function useStampAndReadLetter(letterId) {
    readLetterPage(letterId);
}

function clickGoToCardListPage() {
    window.location = "/card/list/page";
}