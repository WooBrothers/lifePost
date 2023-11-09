import {createLetterListSpace} from "./letterList.js";
import {
    findParentWithClass,
    isScrolledToBottom,
    readLetterPage,
    setFilterBtnOnOff
} from "../../common/utilTool.js";
import {authFetch} from "../../common/apiUtil.js";

export function bindEventToLetterListGrid() {
    const letterSpaceList = document.querySelectorAll(".letter-space");
    letterSpaceList.forEach(space => {
        space.addEventListener("click", clickLetter);
    });

    const focusBtnList = document.querySelectorAll(".focus-btn");
    focusBtnList.forEach(btn => {
        btn.addEventListener("click", clickFocusBtn);
    });

    const filterBtnList = document.querySelectorAll(".filter")
    filterBtnList.forEach(filterBtn => {
        filterBtn.addEventListener("click", filterBtnClick);
    });

    window.addEventListener("wheel", scrollLetters);
}

async function scrollLetters() {
    if (event.deltaY > 0 && isScrolledToBottom()) {

        const letterSpace = document.querySelector("#letter-list-space");
        const letterId = getLastLetterIdFromLetterSpace(letterSpace);

        await createLetterListSpace(letterSpace, letterId, bindEventToLetterListGrid);
    }
}

function getLastLetterIdFromLetterSpace(letterSpace) {

    const lastLetter = letterSpace.lastChild;

    let letterId;
    if (lastLetter) {
        letterId = lastLetter.dataset.letterId;
    } else {
        letterId = 0;
    }

    return letterId;
}

export async function clickLetter() {
    const letterId = this.dataset.letterId;
    readLetterPage(letterId);
}

async function filterBtnClick() {
    setFilterBtnOnOff(this);

    const letterListSpace = document.getElementById("letter-list-space");

    while (letterListSpace.firstChild) {
        letterListSpace.removeChild(letterListSpace.firstChild);
    }

    await createLetterListSpace(letterListSpace, 0, bindEventToLetterListGrid);
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