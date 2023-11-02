bindEventToOnBoardingModal();

function bindEventToOnBoardingModal() {
    const previousBtn = document.querySelector("#previous-btn");
    previousBtn.addEventListener("click", previousClick);

    const nextBtn = document.querySelector("#next-btn");
    nextBtn.addEventListener("click", nextBtnClick);
}

function nextBtnClick() {
    let pageId = parseInt(document.querySelector(".dot.active").dataset.dotId);
    const maxPageId = document.querySelectorAll(".on-boarding").length;

    if (pageId < maxPageId) {
        handleDot(pageId, pageId + 1);
        handleModalPage(pageId, pageId + 1);
        handlePreviousBtn();

        pageId++;
    }

    if (pageId === maxPageId) {
        this.classList.add("disabled");
    }
}

function handlePreviousBtn() {
    const previousBtn = document.querySelector("#previous-btn");
    previousBtn.classList.remove("disabled");
}

function previousClick() {
    let pageId = parseInt(document.querySelector(".dot.active").dataset.dotId);
    const minPageId = 1;

    if (pageId > minPageId) {
        handleDot(pageId, pageId - 1);
        handleModalPage(pageId, pageId - 1);
        handleNextBtn();

        pageId--;
    }
    if (pageId === minPageId) {
        this.classList.add("disabled");
    }
}

function handleNextBtn() {
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