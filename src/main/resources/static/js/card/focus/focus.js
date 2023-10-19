import {
    cardModifyBtnClick,
    deleteCardBtnClick,
    readLetterBtnClick,
    writeCardBtnClick
} from "../list/cardListEvent.js";
import {getCardListAfterCardId} from "../cardApi.js";
import {getAccessToken} from "../../common/apiUtil.js";
import {createCard} from "../list/cardList.js";
import {isTokenExpired} from "../../common/utilTool.js";

const space = document.querySelector("#focus-card-contents-space");
await renderFocusCard(space, 0, bindEventToFocusCard);

async function renderFocusCard(focusSpace, memberCardId, event) {

    let resultResponse = null;

    const height = window.innerHeight * 0.75;
    focusSpace.style.height = `${height}px`;

    if (!isTokenExpired()) {
        document.querySelector("#focus-card-space").style.display = "block";

        await getCardListAfterCardId(memberCardId, 4).then(response => {
            resultResponse = response;
            createCard(response, focusSpace);
        });

        setEmptyFocusCardStyle();

        event();
    }
    return resultResponse;
}

function bindEventToFocusCard() {
    const writeCardBtnList = document.querySelectorAll(".card-write-btn");
    writeCardBtnList.forEach(btn => {
        btn.addEventListener("click", writeCardBtnClick);
    });

    const modifyCustomCardBtnList = document.querySelectorAll(".card-modify-btn");
    modifyCustomCardBtnList.forEach(modifyCustomCardBtn => {
        modifyCustomCardBtn.addEventListener("click", cardModifyBtnClick);
    });

    const deleteCustomCardBtnList = document.querySelectorAll(".card-delete-btn");
    deleteCustomCardBtnList.forEach(deleteCustomCardBtn => {
        deleteCustomCardBtn.addEventListener("click", deleteCardBtnClick);
    });

    const readLetterBtnList = document.querySelectorAll(".letter-read-btn");
    readLetterBtnList.forEach(btn => {
        btn.addEventListener("click", readLetterBtnClick);
    });

    const focusCardContentsSpace = document.querySelector("#focus-card-contents-space");
    focusCardContentsSpace.addEventListener("scroll", scrollFocusCardLoad);

    const goToLoginPageBtn = document.querySelector("#go-to-login-page-btn");
    if (goToLoginPageBtn) {
        goToLoginPageBtn.addEventListener("click", clickGoToLoginPageBtn);
    }
}

async function scrollFocusCardLoad() {
    if (this.scrollTop + this.clientHeight >= this.scrollHeight) {
        const space = document.querySelector("#focus-card-contents-space");
        const memberCardId = space.lastElementChild.dataset.memberCardId;
        if (memberCardId) {
            await renderFocusCard(space, memberCardId, bindEventToFocusCard);
        }
    }
}

function clickGoToLoginPageBtn() {
    if (getAccessToken()) {
        window.location = "/card/list/page";
    } else {
        window.location = "/login/page";
    }
}

function setEmptyFocusCardStyle() {
    if (document.querySelector(".empty-content")) {
        document.querySelector("#focus-card-contents-space").style.justifyContent = "center";
    }
}