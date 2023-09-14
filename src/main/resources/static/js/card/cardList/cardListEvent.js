import {authFetch} from "../../common/apiUtil.js";
import {
    findParentWithClass,
    removeHTMLAndWhitespace,
    TodayCardWriteHistory
} from "../../common/utilTool.js";
import {DivTag, ModalTag} from "../../common/tagUtil.js";
import {bindInputEventTextarea} from "../cardWrite/cardWriteEvent.js";
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

    const createCardBtn = document.getElementById("create-card-btn");
    createCardBtn.addEventListener("click", createCardBtnClick);

    const writeCardBtnList = document.querySelectorAll(".card-write-btn");
    writeCardBtnList.forEach(btn => {
        btn.addEventListener("click", (event) => {
            writeCardBtnClick.call(event, "card-list-space")
        });
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

    if (this.dataset.onOff === "true") {
        this.dataset.onOff = "false";
        this.classList.remove("active");
    } else {
        this.dataset.onOff = "true"
        this.classList.add("active");
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
    const modal = $("#card-modal");
    modal.modal("show");

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
    const modal = $("#card-modal");
    modal.modal("show");

    document.querySelector("#card-img").value = null;
    document.querySelector("#card-title").value = null;
    document.querySelector("#card-content").value = null;
    document.querySelector("#card-submit-btn").dataset.memberCardId = null;
}

function deleteCardBtnClick() {
    const modal = $("#card-delete-warning-modal")
    modal.modal("show");

    const cardDeleteBtn = document.querySelector("#card-delete-btn");
    cardDeleteBtn.dataset.cardId = this.dataset.cardId;
    cardDeleteBtn.dataset.type = "CUSTOM";
}

export async function writeCardBtnClick(parentId) {

    const cardParent = findParentWithClass(this.target, "card-space");
    const cardId = cardParent.dataset.cardId;
    const contentText = cardParent.querySelector(
        `#card-content-${cardId}`
    ).innerHTML;

    const parent = document.getElementById(parentId);

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
                    height: "90%",
                    width: "60%",
                    display: "block",
                    backgroundColor: "white",
                    zIndex: 1001,
                    margin: "1% 20% 1% 20%",
                    position: "fixed",
                    top: "5%",
                }])
        ).getTag();


    const url = "/card/write/page";
    let option = {method: "GET"};

    await authFetch(url, option)
        .then(res => {
            modal.querySelector("#modal-content").innerHTML = res;
            createTextEleByCardContent(modal, contentText);

            modal.style.display = "block";
            parent.appendChild(modal);

            // paenrt에 modal을 추가한 이후에 동작
            modal.querySelector("#card-write-content").focus();
            const memberCardId = cardParent.dataset.memberCardId;
            modal.dataset.memberCardId = memberCardId

            const todayCardWriteHistory = new TodayCardWriteHistory();
            document.querySelector("#today-total-write-count").innerHTML += todayCardWriteHistory.totalCount;

            document.querySelector("#card-write-count").innerHTML
                = "이 카드를 쓴 횟수: " + todayCardWriteHistory.getMemberCardCount(memberCardId);

            bindInputEventTextarea();
        });
}

export function clickReadLetter() {

    localStorage.setItem("letterId", this.dataset.letterId);
    window.location = "/letter/read/page";
}

function createTextEleByCardContent(modal, tagText) {
    const displayOutput = modal.querySelector("#display-output");

    let replacedText = removeHTMLAndWhitespace(tagText);
    replacedText = replacedText.replace(/\s{2,}/g, "<br/>");

    const contentTextList = replacedText.split("<br/>");

    let idx = 0;
    contentTextList.forEach(contentText => {
        for (let i = 0; i < contentText.length; i++) {
            const text = new DivTag()
                .setId("content-text-" + (i + idx))
                .setClassName("content-text-class")
                .setInnerHTML(contentText[i])
                .setStyle([{
                        backgroundColor: "transparent",
                        zIndex: 2005
                    }]
                ).getTag();

            if (contentText[i] === " ") {
                text.classList.add("empty-space");
            }

            displayOutput.appendChild(text);
        }
        idx += contentText.length;
        displayOutput.appendChild(new DivTag()
            .setClassName("line-break")
            .setId("content-text-" + idx)
            .setInnerHTML("\n")
            .getTag());
        idx++;
    });
}
