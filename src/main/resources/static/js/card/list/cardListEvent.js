import {authFetch} from "../../common/apiUtil.js";
import {findParentWithClass, TodayCardWriteHistory} from "../../common/utilTool.js";
import {createPagination} from "../../pagination/pagination.js";
import {bindPaginationBtnEvent} from "../../pagination/paginationEvent.js";
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
    })


    const writeCardBtnList = document.querySelectorAll(".card-write-btn");
    writeCardBtnList.forEach(btn => {
        btn.addEventListener("click", writeCardBtnClick);
    });

    const readLetterBtnList = document.querySelectorAll(".letter-read-btn");
    readLetterBtnList.forEach(btn => {
        btn.addEventListener("click", clickReadLetter);
    });

    const modifyCustomCardBtnList = document.querySelectorAll(".card-modify-btn");
    modifyCustomCardBtnList.forEach(modifyCustomCardBtn => {
        modifyCustomCardBtn.addEventListener("click", cardModifyBtnClick);
    })

    const deleteCustomCardBtnList = document.querySelectorAll(".card-delete-btn");
    deleteCustomCardBtnList.forEach(deleteCustomCardBtn => {
        deleteCustomCardBtn.addEventListener("click", deleteCardBtnClick);
    })
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
    if (this.dataset.focus !== "true") {
        option.method = "POST";
        imgUrl = "url('/img/focus-mark-on.png')";
        focus = "true";
    } else {
        option.method = "DELETE";
        imgUrl = "url('/img/focus-mark-off.png')";
        focus = "false";
    }

    await authFetch(url, option).then(response => {
        this.style.backgroundImage = imgUrl;
        this.dataset.focus = focus;
    });
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

    const cardListSpace = document.getElementById("card-list-space");
    const cardList = document.getElementsByClassName("card-space");
    Array.from(cardList).forEach(card => cardListSpace.removeChild(card));

    const response = await createCardListSpace(cardListSpace, 1, bindEventToCardListGrid);

    const cardListPaginationSpace = document.getElementById("pagination-space");
    cardListPaginationSpace.replaceChildren();

    await createPagination(response, cardListPaginationSpace);
    bindPaginationBtnEvent("card-list-space", "card-space", createCardListSpace, bindEventToCardListGrid);
}

function cardModifyBtnClick() {
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

function createCardBtnClick() {
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

function deleteCardBtnClick() {
    const modal = document.querySelector("#card-delete-warning-modal");
    new bootstrap.Modal(modal).show();

    const cardDeleteBtn = document.querySelector("#card-delete-btn");
    cardDeleteBtn.dataset.cardId = this.dataset.cardId;
    cardDeleteBtn.dataset.type = "CUSTOM";
}

export function clickReadLetter() {

    localStorage.setItem("letterId", this.dataset.letterId);
    window.location = "/letter/read/page";
}

export function deleteCardModalBackgroundImg() {
    const backgroundImgList = document.querySelectorAll(".card-modal-background");
    backgroundImgList.forEach(backgroundImg => {
        backgroundImg.remove();
    })
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