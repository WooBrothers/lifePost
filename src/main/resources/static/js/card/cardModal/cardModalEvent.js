import {
    createCardApiCall,
    deleteCardApiCall,
    increaseCardWriteCount,
    rewardStampToUser
} from "./cardModalApi.js";
import {animateCSS, TodayCardWriteHistory} from "../../common/utilTool.js";

bindEventToCardCreatePage();

export function bindEventToCardCreatePage() {
    const cardCreateBtn = document.getElementById("card-submit-btn");
    cardCreateBtn.addEventListener("click", createCardClick);

    const cardDeleteBtn = document.querySelector("#card-delete-btn");
    cardDeleteBtn.addEventListener("click", deleteCardBtnClick);

    const cardWriteContent = document.getElementById("card-write-editor");
    cardWriteContent.addEventListener("input", inputText);
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

async function inputText() {
    const goal = document.querySelector("#goal-content").value
    const input = document.querySelector("#card-write-editor").value

    if (goal === input) {
        await increaseWriteCount(this.dataset.memberCardId);

        const emoji = document.querySelector("#write-correct-emoji")
        emoji.classList.remove("bi-emoji-frown");
        emoji.classList.add("bi-emoji-smile");

        animateCSS("#write-correct-emoji", "shakeY").then((message) => {
            emoji.classList.remove("bi-emoji-smile");
            emoji.classList.add("bi-emoji-frown");
        });

        this.value = "";

    } else {
        animateCSS("#write-correct-emoji", "shakeX").then();
    }
}

async function increaseWriteCount(cardId) {

    const todayCardWriteHistory = new TodayCardWriteHistory();

    const memberCardId = cardId;

    todayCardWriteHistory.increaseWriteCount(memberCardId).save()

    await increaseCardWriteCount(memberCardId, 1);

    if (todayCardWriteHistory.isTotalCountMoreThanHundred()) {
        await rewardStampToUser();
    }

    const progressBar = document.querySelector("#write-progress")

    const totalCount = todayCardWriteHistory.totalCount < 100 ? todayCardWriteHistory.totalCount : 100;

    progressBar.ariaValuenow = totalCount;
    progressBar.style.width = totalCount + "%";
    progressBar.innerHTML = totalCount + "%";

    document.querySelector("#card-write-count").innerHTML = todayCardWriteHistory.memberCards[memberCardId];
}