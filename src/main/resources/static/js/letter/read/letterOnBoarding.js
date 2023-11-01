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
    console.log('next')
    console.log(pageId)
    console.log(maxPageId)
    if (pageId < maxPageId) {
        dotHandle(pageId, pageId + 1);
        modalPageHandle(pageId, pageId + 1);

        const previousBtn = document.querySelector("#previous-btn");
        previousBtn.classList.remove("disabled");

        pageId++;
    }

    if (pageId === maxPageId) {
        this.classList.add("disabled");
    }
}

function previousClick() {
    let pageId = parseInt(document.querySelector(".dot.active").dataset.dotId);

    const minPageId = 1;
    console.log('pre')
    console.log(pageId)
    console.log(minPageId)
    if (pageId > minPageId) {
        dotHandle(pageId, pageId - 1);
        modalPageHandle(pageId, pageId - 1);

        const previousBtn = document.querySelector("#next-btn");
        previousBtn.classList.remove("disabled");

        pageId--;
    }
    if (pageId === minPageId) {
        this.classList.add("disabled");
    }
}


function dotHandle(pageId, resultId) {
    const activeDot = document.querySelector(`#dot-${pageId}`);
    activeDot.classList.remove("active");

    const nextDot = document.querySelector(`#dot-${resultId}`);
    nextDot.classList.add("active");
}

function modalPageHandle(pageId, resultId) {
    const activeModal = document.querySelector(`#modal-content-body-${pageId}`);
    activeModal.classList.add("d-none");

    const nextModal = document.querySelector(`#modal-content-body-${resultId}`);
    nextModal.classList.remove("d-none");
}