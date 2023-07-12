import {authFetch} from "../../common/apiUtil.js";
import {findParentWithClass} from "../../common/utilTool.js";

export function bindEventToCardListGrid() {
    const filerBtn = document.getElementById("filter-btn");
    filerBtn.addEventListener("click", clickFilterBtn);

    const focusBtnList = document.querySelectorAll(".focus-btn");
    focusBtnList.forEach(btn => {
        btn.addEventListener("click", clickBookmarkBtn);
    })
}

function clickFilterBtn() {
    const filterClassBtnList = document.querySelectorAll(".card-list-btn.filter");
    const filterBtnSpace = document.querySelector("#filter-btn-space");

    filterClassBtnList.forEach(filterBtn => {
        if (filterBtn.style.display === "none") {
            filterBtn.style.display = "block";
            filterBtnSpace.style.border = "1px solid black";
        } else {
            filterBtn.style.display = "none";
            filterBtnSpace.style.border = "none";
        }
    });
}

export async function clickBookmarkBtn() {

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
    })
}
