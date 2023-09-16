import {animateCSS, TodayCardWriteHistory} from "../../common/utilTool.js";
import {increaseCardWriteCount, rewardStampToUser} from "./cardWriteApi.js";

export function bindInputEventTextarea() {
    const cardWriteContent = document.getElementById("card-write-editor");
    cardWriteContent.addEventListener("input", inputText);

    const closeBtn = document.getElementById("modal-close-btn");
    closeBtn.addEventListener("click", closeModalBtn);

    const myTextArea = document.getElementById("card-write-content");
    blockAbusingEvent(myTextArea);

    const modalParent = document.getElementById("modal-parent");
    modalParent.addEventListener("keydown", escKeyDown);
}

async function inputText() {
    const goal = document.querySelector("#goal-content").value
    const input = document.querySelector("#card-write-editor").value

    if (goal === input) {
        await increaseWriteCount();

        const emoji = document.querySelector("#write-correct-emoji")
        emoji.classList.remove("bi-emoji-frown");
        emoji.classList.add("bi-emoji-smile");

        animateCSS("#write-correct-emoji", "tada").then((message) => {
            emoji.classList.remove("bi-emoji-smile");
            emoji.classList.add("bi-emoji-frown");
        });

    } else {
        animateCSS("#write-correct-emoji", "shakeX").then();
    }
}

async function inputTextarea() {

    const displayOutput = document.getElementById("display-output");

    const input = this.value;

    for (let i = 0; i < input.length; i++) {
        const target = displayOutput.querySelector(`#content-text-${i}`);
        if (input[i] === target.innerHTML) {
            target.classList.add("valid");
            target.classList.remove("invalid");
        } else {
            target.classList.add("invalid");
            target.classList.remove("valid");
        }
        if (target.innerHTML === " ") {
            target.classList.add("empty-space");
        }

        const totalTextLength = displayOutput.childNodes.length;
        if (totalTextLength === i + 1 && isAllTextRight(displayOutput)) {
            displayOutput.childNodes.forEach(node => {
                deleteTextColorClass(node);
            })
            this.value = "";
            await increaseWriteCount();
        }
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

async function increaseWriteCount() {

    const todayCardWriteHistory = new TodayCardWriteHistory();

    const memberCardId = getMemberCardId();

    todayCardWriteHistory.increaseWriteCount(memberCardId).save()

    document.querySelector("#today-total-write-count")
        .innerHTML = "오늘 쓴 총 횟수:" + todayCardWriteHistory.totalCount;

    await increaseCardWriteCount(memberCardId, 1);

    document.querySelector("#card-write-count").innerHTML
        = "이 카드를 쓴 횟수: " + todayCardWriteHistory.memberCards[memberCardId];

    if (todayCardWriteHistory.isTotalCountMoreThanHundred()) {
        await rewardStampToUser();
    }
}

function escKeyDown(event) {
    if (event.keyCode === 27) {
        this.remove();
    }
}