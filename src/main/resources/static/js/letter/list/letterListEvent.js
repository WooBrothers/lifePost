import {bindPaginationBtnEvent} from "../../pagination/paginationEvent.js";
import {createLetterListSpace} from "./letterListGrid.js";
import {createPagination} from "../../pagination/pagination.js";
import {findParentWithClass} from "../../common/utilTool.js";
import {authFetch} from "../../common/apiUtil.js";
import {bindEventToLetterStampUsePage} from "./letterStampUseModal.js";

export function bindEventToLetterListGrid() {
    const letterSpaceList = document.querySelectorAll(".letter-space");
    letterSpaceList.forEach(space => {
        space.addEventListener("click", clickLetter);
    })

    const focusBtnList = document.querySelectorAll(".focus-btn");
    focusBtnList.forEach(btn => {
        btn.addEventListener("click", clickFocusBtn);
    })

    const filterBtnList = document.querySelectorAll(".filter")
    filterBtnList.forEach(filterBtn => {
        filterBtn.addEventListener("click", filterBtnClick);
    })
}

async function clickLetter() {
    const modal = $('#letterReadInfoModal');
    modal.modal("show");

    const letterId = this.dataset.letterId;
    localStorage.setItem("letterId", letterId);

    if (this.dataset.memberLetterId === "undefined") {

        const url = "/api/v1/member/auth/info";
        let options = {method: "GET"};

        await authFetch(url, options).then(res => {
            const modalBody = document.querySelector("#modal-content-body");
            modalBody.querySelector("#email").innerHTML = res.email;
            modalBody.querySelector("#stamp").innerHTML = res.stampCount;

            if (res.stampCount === 0) {
                document.querySelector("#letter-read-btn").classList.add("disabled");
            } else {
                document.querySelector("#letter-read-btn").classList.remove("disabled");
            }
            bindEventToLetterStampUsePage();
        });

    } else {
        window.location = "/letter/read/page";
    }
}

async function filterBtnClick() {
    const className = this.id.split("-btn")[0];
    const btnList = document.querySelectorAll(`.${className}`);

    if (this.dataset.onOff === "true") {
        btnList.forEach((btn) => {
            btn.dataset.onOff = "false";
            btn.classList.remove("active");
        });
    } else {
        btnList.forEach((btn) => {
            btn.dataset.onOff = "true"
            btn.classList.add("active");
        });
    }

    const letterListSpace = document.getElementById("letter-list-space");
    const letterList = document.getElementsByClassName("letter-space");
    Array.from(letterList).forEach(letter => letterListSpace.removeChild(letter));

    const response = await createLetterListSpace(letterListSpace, 1, bindEventToLetterListGrid);

    const paginationSpace = document.getElementById("pagination-space");
    paginationSpace.replaceChildren();

    await createPagination(response, paginationSpace);
    bindPaginationBtnEvent("letter-list-space", "letter-space", createLetterListSpace, bindEventToLetterListGrid);
}

async function clickFocusBtn() {
    event.stopPropagation();

    const parentLetterSpace = findParentWithClass(this, "letter-space");
    const url = "/api/v1/letter/auth/focus";

    let focusType, imgUrl, focus;
    if (this.dataset.focus !== "true") {
        imgUrl = "url('/img/focus-mark-on.png')";
        focus = "true";
        focusType = "ATTENTION";
    } else {
        imgUrl = "url('/img/focus-mark-off.png')";
        focus = "false";
        focusType = "NON";
    }

    const body = {
        letterId: parseInt(parentLetterSpace.dataset.letterId),
        memberLetterId: parseInt(parentLetterSpace.dataset.memberLetterId),
        focusType: focusType
    };

    let option = {
        method: "POST",
        body: JSON.stringify(body)
    }

    await authFetch(url, option).then(response => {
        this.style.backgroundImage = imgUrl;
        this.dataset.focus = focus;
    });
}