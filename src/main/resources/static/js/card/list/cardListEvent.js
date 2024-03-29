import {authFetch} from "../../common/apiUtil.js";
import {
    findParentWithClass,
    getScrolling,
    isScrolledToBottom,
    readLetterPage,
    setFilterBtnOnOff,
    setScrollingNotUse,
    setScrollingUse,
    TodayCardWriteHistory
} from "../../common/utilTool.js";
import {createCardListSpace} from "./cardList.js";

export function bindEventToCardListGrid() {
    const focusBtnList = document.querySelectorAll(".focus-btn");
    focusBtnList.forEach(btn => {
        btn.addEventListener("click", clickFocusBtn);
    });

    const filterBtnList = document.querySelectorAll(".filter");
    filterBtnList.forEach(btn => {
        btn.addEventListener("click", filterBtnClick)
    });

    const createCardBtnList = document.querySelectorAll(".create-card-btn");
    createCardBtnList.forEach(createCardBtn => {
        createCardBtn.addEventListener("click", createCardBtnClick);
    });

    const writeCardBtnList = document.querySelectorAll(".card-write-btn");
    writeCardBtnList.forEach(btn => {
        btn.addEventListener("click", writeCardBtnClick);
    });

    const readLetterBtnList = document.querySelectorAll(".letter-read-btn");
    readLetterBtnList.forEach(btn => {
        btn.addEventListener("click", readLetterBtnClick);
    });

    const modifyCustomCardBtnList = document.querySelectorAll(".card-modify-btn");
    modifyCustomCardBtnList.forEach(modifyCustomCardBtn => {
        modifyCustomCardBtn.addEventListener("click", cardModifyBtnClick);
    });

    const deleteCustomCardBtnList = document.querySelectorAll(".card-delete-btn");
    deleteCustomCardBtnList.forEach(deleteCustomCardBtn => {
        deleteCustomCardBtn.addEventListener("click", deleteCardBtnClick);
    });

    window.addEventListener("wheel", scrollCards);
}

async function scrollCards() {

    if (getScrolling()) {
        return;
    }

    setScrollingUse();

    if (event.deltaY > 0 && isScrolledToBottom()) {

        const cardSpace = document.querySelector("#card-list-space");
        const cardId = getLastCardIdFromLetterSpace(cardSpace);

        await createCardListSpace(cardSpace, cardId, bindEventToCardListGrid);
    }

    setScrollingNotUse();
}


function getLastCardIdFromLetterSpace(cardSpace) {

    const lastCard = cardSpace.lastChild;

    let cardId;
    if (lastCard) {
        cardId = lastCard.dataset.memberCardId;
    } else {
        cardId = 0;
    }

    return cardId;
}

async function clickFocusBtn() {

    const parentCardSpace = findParentWithClass(this, "card-space");
    const url = "/api/v1/card/auth/focus";
    const body = {
        cardId: parentCardSpace.dataset.cardId,
        type: parentCardSpace.dataset.type
    };

    let option = {
        body: JSON.stringify(body)
    }

    let imgUrl, focus;

    const cdnUrl = "https://cdn.life-post.net/img/service/card";

    if (this.dataset.focus !== "true") {
        option.method = "POST";
        imgUrl = `url('${cdnUrl}/focus-mark-on.png')`;
        focus = "true";
    } else {
        option.method = "DELETE";
        imgUrl = `url('${cdnUrl}/focus-mark-off.png')`;
        focus = "false";
    }

    await authFetch(url, option).then(response => {
        this.style.backgroundImage = imgUrl;
        this.dataset.focus = focus;
    });
}

async function filterBtnClick() {
    setFilterBtnOnOff(this);

    const cardListSpace = document.getElementById("card-list-space");

    while (cardListSpace.firstChild) {
        cardListSpace.removeChild(cardListSpace.firstChild);
    }

    const cardList = document.getElementsByClassName("card-space");
    Array.from(cardList).forEach(card => cardListSpace.removeChild(card));

    await createCardListSpace(cardListSpace, 0, bindEventToCardListGrid);
}

export function cardModifyBtnClick() {
    deleteCardModalBackgroundImg();

    const modal = document.querySelector("#card-modal");
    new bootstrap.Modal(modal).show();

    const modalParent = findParentWithClass(event.target, "card");
    const cardImgUrl = modalParent.querySelector(".card-img-top").src;

    const cardBody = modalParent.querySelector(".card-body");
    const cardTitle = cardBody.childNodes.item(0).innerHTML;
    const cardContent = cardBody.childNodes.item(1).innerHTML;

    document.querySelector("#card-img").value = cardImgUrl;
    document.querySelector("#card-title").value = cardTitle;
    document.querySelector("#card-content").value = cardContent;
    document.querySelector("#card-submit-btn").dataset.memberCardId
        = event.target.dataset.cardId;
}

export function createCardBtnClick() {
    deleteCardModalBackgroundImg();

    const modal = document.querySelector("#card-modal");
    new bootstrap.Modal(modal).show();

    document.querySelector("#card-img").value = null;
    document.querySelector("#card-title").value = null;
    document.querySelector("#card-content").value = null;
    document.querySelector("#card-submit-btn").dataset.memberCardId = null;
}

export function writeCardBtnClick() {

    // 모달창이 다 뜨고 나서 popover 적용한 버튼 클릭
    const modal = document.querySelector("#card-write-modal");

    setCardWriteProgressBar();

    modal.addEventListener("shown.bs.modal", function () {
        document.querySelector("#write-correct-emoji").click();
    });

    // 모달 보기
    new bootstrap.Modal(modal).show();

    const memberCardId = this.dataset.memberCardId;
    const title = document.querySelector(`#card-title-${memberCardId}`).innerHTML;
    const content = document.querySelector(`#card-content-${memberCardId}`).innerHTML;
    const count = this.dataset.writeCount;

    document.querySelector("#write-card-title").innerHTML = title;
    document.querySelector("#goal-content").innerHTML = content;
    document.querySelector("#card-write-editor").value = "";
    document.querySelector("#card-write-editor").dataset.memberCardId = memberCardId;
    document.querySelector("#card-write-count").innerHTML = count;
}

export function deleteCardBtnClick() {
    const modal = document.querySelector("#card-delete-warning-modal");
    new bootstrap.Modal(modal).show();

    const cardDeleteBtn = document.querySelector("#card-delete-btn");
    cardDeleteBtn.dataset.cardId = this.dataset.cardId;
    cardDeleteBtn.dataset.type = "CUSTOM";
}

export function deleteCardModalBackgroundImg() {
    const backgroundImgList = document.querySelectorAll(".card-modal-background");
    backgroundImgList.forEach(backgroundImg => {
        backgroundImg.remove();
    })
}

export function readLetterBtnClick() {
    const letterId = this.dataset.letterId;
    readLetterPage(letterId);
}

export function setCardWriteProgressBar() {
    const todayCardWriteHistory = new TodayCardWriteHistory();

    const totalCount = todayCardWriteHistory.isTotalCountMoreThanGoal()
        ? todayCardWriteHistory.goal : todayCardWriteHistory.totalCount;

    const progressBar = document.querySelector("#write-progress")
    const percentage = totalCount / todayCardWriteHistory.goal * 100;

    progressBar.ariaValuenow = percentage;
    progressBar.style.width = percentage + "%";
    progressBar.innerHTML = percentage + "%";
}