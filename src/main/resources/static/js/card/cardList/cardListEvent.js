import {authFetch} from "../../common/apiUtil.js";
import {findParentWithClass} from "../../common/utilTool.js";
import {CardListGrid} from "./cardListGrid.js";
import {DivTag, ModalTag} from "../../common/tagUtil.js";
import {bindEventToCardCreatePage} from "../cardCreate/cardCreateEvent.js";
import {bindInputEventTextarea} from "../cardWrite/cardWriteEvent.js";

export function bindEventToCardListGrid() {
    const filerBtn = document.getElementById("filter-btn");
    filerBtn.addEventListener("click", clickFilterBtn);

    const focusBtnList = document.querySelectorAll(".focus-btn");
    focusBtnList.forEach(btn => {
        btn.addEventListener("click", clickBookmarkBtn);
    });

    const filterBtnList = document.querySelectorAll(".filter");
    filterBtnList.forEach(btn => {
        btn.addEventListener("click", filterBtnClick)
    });

    const createCardBtn = document.getElementById("create-card-btn");
    createCardBtn.addEventListener("click", createCardBtnClick);

    const writeCardBtnList = document.querySelectorAll(".card-write-btn");
    writeCardBtnList.forEach(btn => {
        btn.addEventListener("click", writeCardBtnClick);
    })

    const readLetterBtnList = document.querySelectorAll(".letter-read-btn");
    readLetterBtnList.forEach(btn => {
        btn.addEventListener("click", clickReadLetter);
    })
}

function clickFilterBtn() {
    const filterClassBtnList = document.querySelectorAll(".card-list-btn.filter");
    const filterBtnSpace = document.querySelector("#filter-btn-space");

    filterClassBtnList.forEach(filterBtn => {
        if (filterBtn.style.display === "none") {
            filterBtn.style.display = "block";
        } else {
            filterBtn.style.display = "none";
            filterBtnSpace.style.border = "none";
        }
    });
}

async function clickBookmarkBtn() {

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

export function bindPaginationBtnEvent() {
    const pageBtnSpace = document.getElementById("page-btn-space");
    const paginationBtnList = pageBtnSpace.childNodes;
    paginationBtnList.forEach(btn => {
        btn.addEventListener("click", clickPaginationBtn);
    });
}

async function clickPaginationBtn() {
    const currentPage = document.querySelector(".card-list-page-btn.on");
    if (this === currentPage) {
        return;
    }

    const cardListSpace = document.getElementById("card-list-space");
    const cardSpace = document.querySelectorAll(".card-space");

    cardSpace.forEach(card => {
        cardListSpace.removeChild(card);
    })

    const pageNo = this.dataset.pageNo;
    const response = await CardListGrid.createCardListSpace(cardListSpace, pageNo, bindEventToCardListGrid);

    currentPage.classList.remove("on");
    this.dataset.pageNo = response.pageable.pageNumber + 1;
    this.classList.add("on");

    const cardListPaginationSpace = document.getElementById("card-list-pagination-space");

    if (this.classList.contains("next-btn") || this.classList.contains("before-btn")) {
        cardListPaginationSpace.replaceChildren();
        CardListGrid.createCardListPagination(response, cardListPaginationSpace, bindPaginationBtnEvent);
    }
}

async function filterBtnClick() {

    if (this.dataset.onOff === "true") {
        this.dataset.onOff = "false";
        this.classList.remove("on");
    } else {
        this.dataset.onOff = "true"
        this.classList.add("on");
    }
    const cardListSpace = document.getElementById("card-list-space");
    const cardList = document.getElementsByClassName("card-space");
    Array.from(cardList).forEach(card => cardListSpace.removeChild(card));

    const response = await CardListGrid.createCardListSpace(cardListSpace, 1, bindEventToCardListGrid);

    const cardListPaginationSpace = document.getElementById("card-list-pagination-space");
    cardListPaginationSpace.replaceChildren();
    await CardListGrid.createCardListPagination(response, cardListPaginationSpace, bindPaginationBtnEvent);
}

async function createCardBtnClick() {
    // 카드 만들기 버튼 클릭
    const parent = document.getElementById("card-list-space");

    const modal = new ModalTag()
        .setId("modal-parent")
        .addInnerHTML(
            new DivTag()
                .setId("modal-content")
                .setClassName("modal")
                .setStyle([{
                    height: "96%",
                    width: "60%",
                    display: "block",
                    backgroundColor: "white",
                    zIndex: 1001,
                    margin: "1% 20% 1% 20%"
                }])
        ).getTag();

    const url = "/card/custom/page";
    let option = {method: "GET"};

    await authFetch(url, option)
        .then(res => {
            modal.querySelector("#modal-content").innerHTML = res;
            modal.style.display = "block";
            parent.appendChild(modal);
            bindEventToCardCreatePage();
        })
}

async function writeCardBtnClick() {

    const cardParent = findParentWithClass(this, "card-space");
    const cardId = cardParent.dataset.cardId;
    const contentText = cardParent.querySelector(
        `#card-content-${cardId}`
    ).innerHTML;

    const parent = document.getElementById("card-list-space");

    const modal = new ModalTag()
        .setId("modal-parent")
        .addInnerHTML(
            new DivTag()
                .setId("modal-content")
                .setClassName("modal")
                .setStyle([{
                    height: "96%",
                    width: "60%",
                    display: "block",
                    backgroundColor: "white",
                    zIndex: 1001,
                    margin: "1% 20% 1% 20%"
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
            modal.dataset.memberCardId = cardParent.dataset.memberCardId;

            bindInputEventTextarea();
        })
}

function clickReadLetter() {

    localStorage.setItem("letterId", this.dataset.letterId);
    window.location = "/letter/read/page";
}

function createTextEleByCardContent(modal, contentText) {
    const displayOutput = modal.querySelector("#display-output");

    for (let i = 0; i < contentText.length; i++) {
        const text = new DivTag()
            .setId("content-text-" + i)
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
}