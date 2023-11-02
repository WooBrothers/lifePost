bindEventToOnBoardingModal();

function bindEventToOnBoardingModal() {
    const previousBtn = document.querySelector("#previous-btn");
    previousBtn.addEventListener("click", previousClick);

    const nextBtn = document.querySelector("#next-btn");
    nextBtn.addEventListener("click", nextBtnClick);
}

/* event */
function nextBtnClick() {
    let pageId = parseInt(document.querySelector(".dot.active").dataset.dotId);
    const maxPageId = document.querySelectorAll(".on-boarding").length;

    if (pageId < maxPageId) {
        handleDot(pageId, pageId + 1);
        handleModalPage(pageId, pageId + 1);
        ablePreviousBtn();

        pageId++;
    }

    if (pageId === maxPageId) {
        this.classList.add("end-btn");
        this.innerHTML = "시작하기";

        const endBtn = document.querySelector(".end-btn");
        endBtn.addEventListener("click", endBtnClick);
    }
}

function previousClick() {
    let pageId = parseInt(document.querySelector(".dot.active").dataset.dotId);
    const minPageId = 1;

    if (pageId > minPageId) {
        handleDot(pageId, pageId - 1);
        handleModalPage(pageId, pageId - 1);
        ableNextBtn();
        rollbackEndBtn();

        pageId--;
    }
    if (pageId === minPageId) {
        disablePreviousBtn();
    }
}

function endBtnClick() {
    this.innerHTML = "다음";
    this.removeEventListener("click", endBtnClick);

    handleDot(6, 1);
    handleModalPage(6, 1);
    ableNextBtn();
    disablePreviousBtn();

    const modalCloseBtn = document.querySelector("#modal-close-btn");
    modalCloseBtn.click();
}


/* function */
function rollbackEndBtn() {
    const endBtn = document.querySelector(".end-btn");

    if (endBtn) {
        endBtn.classList.remove("end-btn");
        endBtn.removeEventListener("click", endBtnClick);
        endBtn.innerHTML = "다음";
    }
}

function ablePreviousBtn() {
    const previousBtn = document.querySelector("#previous-btn");
    previousBtn.classList.remove("disabled");
}

function disablePreviousBtn() {
    const previousBtn = document.querySelector("#previous-btn");
    previousBtn.classList.add("disabled");
}

function ableNextBtn() {
    const nextBtn = document.querySelector("#next-btn");
    nextBtn.classList.remove("disabled");
}

function handleDot(pageId, resultId) {
    const activeDot = document.querySelector(`#dot-${pageId}`);
    activeDot.classList.remove("active");

    const nextDot = document.querySelector(`#dot-${resultId}`);
    nextDot.classList.add("active");
}

function handleModalPage(pageId, resultId) {
    const activeModal = document.querySelector(`#modal-content-body-${pageId}`);
    const nextModal = document.querySelector(`#modal-content-body-${resultId}`);

    activeModal.classList.remove("active");

    setTimeout(() => {
        activeModal.classList.add("d-none");
        nextModal.classList.remove("d-none");

        setTimeout(() => {
            nextModal.classList.add("active");
        }, 150);
    }, 150);
}
