export function bindEventToLetterStampUsePage() {
    const letterReadBtn = document.getElementById("letter-read-btn");
    letterReadBtn.addEventListener("click", useStampAndReadLetter);

    const closeBtn = document.getElementById("modal-close-btn");
    closeBtn.addEventListener("click", closeModalBtn);

    const goToCardListBtn = document.getElementById("go-to-card-list-btn");
    goToCardListBtn.addEventListener("click", clickGoToCardListPage);
}

async function useStampAndReadLetter() {
    window.location = "/letter/read/page";
}

function closeModalBtn() {
    const modalParent = document.getElementById("modal-parent");
    modalParent.remove();
}


function clickGoToCardListPage() {
    window.location = "/card/list/page";
}