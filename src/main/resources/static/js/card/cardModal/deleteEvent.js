import {deleteCardApiCall} from "./cardModalApi.js";

bindEventToDeleteCardModal();

export function bindEventToDeleteCardModal() {
    const cardDeleteBtn = document.querySelector("#card-delete-btn");
    cardDeleteBtn.addEventListener("click", deleteCardBtnClick);
}

async function deleteCardBtnClick() {
    const cardId = this.dataset.cardId;
    const type = this.dataset.type;

    await deleteCardApiCall(cardId, type);
    location.reload();

    alert("카드를 삭제했습니다.");
}