import {createCardApiCall, deleteCardApiCall} from "./cardModalApi.js";

bindEventToCardCreatePage();

export function bindEventToCardCreatePage() {
    const cardCreateBtn = document.getElementById("card-submit-btn");
    cardCreateBtn.addEventListener("click", createCardClick);

    const cardDeleteBtn = document.querySelector("#card-delete-btn");
    cardDeleteBtn.addEventListener("click", deleteCardBtnClick);
}

async function createCardClick() {
    const dataset = this.dataset;
    const cardImgUrl = document.querySelector("#card-img").value;
    const cardTitle = document.querySelector("#card-title").value;
    const cardContent = document.querySelector("#card-content").value;

    if (cardContent && cardContent.length <= 250) {

        await createCardApiCall(cardTitle, cardImgUrl, cardContent, dataset.memberCardId, dataset.type);
        location.reload();

        alert("카드 수정/생성 성공했습니다.");

    } else {
        alert("카드 만들기 시 내용은 비어있지 않아여 하며, 길이가 250 이하로 작성이 가능합니다.")
    }
}

async function deleteCardBtnClick() {
    const cardId = this.dataset.cardId;
    const type = this.dataset.type;

    await deleteCardApiCall(cardId, type);
    location.reload();

    alert("카드를 삭제했습니다.");
}
