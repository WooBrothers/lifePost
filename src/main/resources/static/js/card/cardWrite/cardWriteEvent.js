import {increaseWriteCount} from "./cardWriteApi.js";

export function bindInputEventTextarea() {
    const cardWriteContent = document.getElementById("card-write-content");
    cardWriteContent.addEventListener("input", isCorrectInputText);

    const closeBtn = document.getElementById("modal-close-btn");
    closeBtn.addEventListener("click", closeModalBtn);

    const myTextArea = document.getElementById("card-write-content");
    blockAbusingEvent(myTextArea);
}

function isCorrectInputText(event) {
    const displayOutput = document.getElementById("display-output");
    const input = this.value;
    const index = input.length - 1;
    const inputType = event.inputType;

    if (inputType === "deleteContentBackward" || inputType === "deleteContentForward") {
        const deleteTarget = displayOutput.querySelector(`#content-text-${index + 1}`);
        deleteTextColorClass(deleteTarget);
        return;
    }

    const target = displayOutput.querySelector(`#content-text-${index}`);

    if (!target) {
        return;
    }
    if (input[index] === target.innerHTML) {
        target.classList.add("valid");
        target.classList.remove("invalid");
    } else {
        target.classList.add("invalid");
        target.classList.remove("valid");
    }
    if (target.innerHTML === " ") {
        target.classList.add("empty-space");
    }

    // 마지막까지 다썻을 경우 쓰기 카운트 증가
    // 텍스트 에어리어 클리어
    // 색상 전부 삭제
    const totalTextLength = displayOutput.childNodes.length;
    if (totalTextLength === index + 1 && isAllTextRight(displayOutput)) {
        displayOutput.childNodes.forEach(node => {
            deleteTextColorClass(node);
        })
        this.value = "";
        increaseWriteCount(getMemberCardId());
    }
}

function closeModalBtn() {
    const modalParent = document.getElementById("modal-parent");
    modalParent.remove();
}

function blockAbusingEvent(myTextArea) {
    myTextArea.addEventListener("copy", function (event) {
        event.preventDefault();
    });

    myTextArea.addEventListener("cut", function (event) {
        event.preventDefault();
    });

    myTextArea.addEventListener("paste", function (event) {
        event.preventDefault();
    });
}

function deleteTextColorClass(deleteTarget) {
    if (deleteTarget) {
        deleteTarget.classList.remove("valid");
        deleteTarget.classList.remove("invalid");
    }
}

function isAllTextRight(displayOutput) {
    if (displayOutput.querySelectorAll(".invalid").length === 0) {
        return true;
    } else {
        return false;
    }
}

function getMemberCardId() {
    return document.getElementById("modal-parent").dataset.memberCardId;
}