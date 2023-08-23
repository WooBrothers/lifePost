import {CardListGrid} from "../cardList/cardListGrid.js";
import {clickReadLetter, writeCardBtnClick} from "../cardList/cardListEvent.js";
import {getCardListAfterCardId} from "../cardApi.js";
import {getAccessToken} from "../../common/apiUtil.js";

const space = document.querySelector("#focus-card-contents-space");
await renderFocusCard(space, 0, bindFocusCard);

async function renderFocusCard(focusSpace, memberCardId, event) {

    let resultResponse = null;

    await getCardListAfterCardId(memberCardId, 5).then(response => {
        resultResponse = response;
        CardListGrid.createCard(response, focusSpace);
    });

    setEmptyFocusCardStyle();

    event();
    return resultResponse;
}

function bindFocusCard() {
    const writeCardBtnList = document.querySelectorAll(".card-write-btn");
    writeCardBtnList.forEach(btn => {
        btn.addEventListener("click", (event) => {
            writeCardBtnClick.call(event, "focus-card-contents-space")
        });
    })

    const readLetterBtnList = document.querySelectorAll(".letter-read-btn");
    readLetterBtnList.forEach(btn => {
        btn.addEventListener("click", clickReadLetter);
    })

    const focusCardContentsSpace = document.querySelector("#focus-card-contents-space");
    focusCardContentsSpace.addEventListener("scroll", scrollFocusCardLoad);

    const goToLoginPageBtn = document.querySelector("#go-to-login-page-btn");
    if (goToLoginPageBtn) {
        goToLoginPageBtn.addEventListener("click", clickGoToLoginPageBtn);
    }
}

async function scrollFocusCardLoad() {

    const maxWidth = window.innerWidth;
    if (maxWidth >= 991) {
        if (this.scrollLeft + this.clientWidth >= this.scrollWidth) {
            const space = document.querySelector("#focus-card-contents-space");
            const memberCardId = space.lastElementChild.dataset.memberCardId;
            if (memberCardId) {
                await renderFocusCard(space, memberCardId, bindFocusCard);
            }

        }
    } else {
        if (this.scrollTop + this.clientHeight >= this.scrollHeight) {
            const space = document.querySelector("#focus-card-contents-space");
            const memberCardId = space.lastElementChild.dataset.memberCardId;
            if (memberCardId) {
                await renderFocusCard(space, memberCardId, bindFocusCard);
            }
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