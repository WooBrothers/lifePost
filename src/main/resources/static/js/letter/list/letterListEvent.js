import {bindPaginationBtnEvent} from "../../pagination/paginationEvent.js";
import {createLetterListSpace} from "./letterListGrid.js";
import {createPagination} from "../../pagination/pagination.js";
import {findParentWithClass} from "../../common/utilTool.js";
import {authFetch} from "../../common/apiUtil.js";
import {DivTag, ModalTag} from "../../common/tagUtil.js";
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

    const openFilterBtn = document.getElementById("filter-btn");
    openFilterBtn.addEventListener("click", clickOpenFilterBtn);

    const filterBtnList = document.querySelectorAll(".act-btn")
    filterBtnList.forEach(filterBtn => {
        filterBtn.addEventListener("click", filterBtnClick);
    })
}

async function clickLetter() {
    const letterId = this.dataset.letterId;
    localStorage.setItem("letterId", letterId);

    const parent = document.getElementById("letter-parent-space");

    if (this.dataset.memberLetterId === "undefined") {
        const modal = new ModalTag()
            .setId("modal-parent")
            .setStyle([{
                position: "fixed",
                top: 0,
                left: 0,
            }])
            .addInnerHTML(
                new DivTag()
                    .setId("modal-content")
                    .setClassName("modal")
                    .setStyle([{
                        width: "20%",
                        height: "32%",
                        display: "block",
                        backgroundColor: "white",
                        zIndex: 1001,
                        margin: "1% 35% 1% 35%",
                        position: "fixed",
                        top: "25%",
                    }])
            ).getTag();

        const url = "/api/v1/letter/auth/stamp/popup";
        let option = {method: "GET"};

        await authFetch(url, option).then(res => {
            modal.querySelector("#modal-content").innerHTML = res;
            modal.style.display = "block";
            parent.appendChild(modal);

            if (modal.querySelector("#info-space").dataset.stamp === "0"
                && getTodayDate() !== this.dataset.createdDate) {

                const letterReadBtn = modal.querySelector("#letter-read-btn");
                letterReadBtn.style.backgroundColor = "gray";
                letterReadBtn.style.color = "lightgray";
                letterReadBtn.disabled = true;
            }
            bindEventToLetterStampUsePage();
        })
    } else {
        window.location = "/letter/read/page";
    }
}

function getTodayDate() {
    const today = new Date();

    const year = today.getFullYear();
    let month = today.getMonth() + 1 > 9 ? today.getMonth() + 1 : "0" + (today.getMonth() + 1);

    let day = today.getDate() > 9 ? today.getDate() : "0" + today.getDate();

    return `${year}-${month}-${day}`
}

function clickOpenFilterBtn() {
    const actBtnSpace = document.querySelector("#act-btn-space");

    if (actBtnSpace.style.display === "" || actBtnSpace.style.display === "none") {
        actBtnSpace.style.display = "flex";
    } else {
        actBtnSpace.style.display = "none";
    }
}

async function filterBtnClick() {

    if (this.dataset.onOff === "true") {
        this.dataset.onOff = "false";
        this.classList.remove("btn-on");
    } else {
        this.dataset.onOff = "true"
        this.classList.add("btn-on");
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