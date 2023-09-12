export function bindEventToLetterStampUsePage() {
    const letterReadBtn = document.getElementById("letter-read-btn");
    letterReadBtn.addEventListener("click", useStampAndReadLetter);

    const goToCardListBtn = document.getElementById("go-to-card-list-btn");
    goToCardListBtn.addEventListener("click", clickGoToCardListPage);
}

async function useStampAndReadLetter() {
    window.location = "/letter/read/page";
}

function clickGoToCardListPage() {
    window.location = "/card/list/page";
}