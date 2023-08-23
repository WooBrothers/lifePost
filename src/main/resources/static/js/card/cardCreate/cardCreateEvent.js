import {createCardApiCall} from "./cardCreateApi.js";

export function bindEventToCardCreatePage() {
    const cardCreateBtn = document.getElementById("custom-card-create-btn");
    cardCreateBtn.addEventListener("click", createCardClick);

    const closeBtn = document.getElementById("modal-close-btn");
    closeBtn.addEventListener("click", closeModalBtn);

    const modalParent = document.getElementById("modal-parent");
    modalParent.addEventListener("keydown", escKeyDown);
}

async function createCardClick() {
    const dataset = this.dataset;
    const inputElement = document.getElementById("custom-card-content");
    if (inputElement.value && inputElement.value.length <= 250) {

        await createCardApiCall(inputElement.value, dataset.memberCardId, dataset.type);
        document.getElementById("modal-parent").style.display = "none";
        location.reload();

    } else {
        alert("카드 만들기 시 내용은 비어있지 않아여 하며, 길이가 250 이하로 작성이 가능합니다.")
    }
}

function closeModalBtn() {
    const modalParent = document.getElementById("modal-parent");
    modalParent.remove();
}

function escKeyDown(event) {
    if (event.keyCode === 27) {
        this.remove();
    }
}
