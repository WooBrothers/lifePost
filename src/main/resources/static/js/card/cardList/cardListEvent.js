import {authFetch} from "../../common/apiUtil.js";
import {findParentWithClass} from "../../common/utilTool.js";
import {CardListGrid} from "./cardListGrid.js";

export function bindEventToCardListGrid() {
    const filerBtn = document.getElementById("filter-btn");
    filerBtn.addEventListener("click", clickFilterBtn);

    const focusBtnList = document.querySelectorAll(".focus-btn");
    focusBtnList.forEach(btn => {
        btn.addEventListener("click", clickBookmarkBtn);
    });
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
    const currentPage = document.querySelector(".current-page");
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

    currentPage.classList.remove("current-page");
    this.dataset.pageNo = response.pageable.pageNumber + 1;
    this.classList.add("current-page");

    const cardListPaginationSpace = document.getElementById("card-list-pagination-space");

    if (this.classList.contains("next-btn") || this.classList.contains("before-btn")) {
        cardListPaginationSpace.replaceChildren();
        CardListGrid.setCardListPagination(response, cardListPaginationSpace, bindPaginationBtnEvent);
    }
}

