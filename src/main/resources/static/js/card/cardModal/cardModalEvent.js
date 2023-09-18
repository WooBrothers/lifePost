import {
    createCardApiCall,
    deleteCardApiCall,
    increaseCardWriteCount,
    rewardStampToUser
} from "./cardModalApi.js";
import {animateCSS, isImageUrlValid, TodayCardWriteHistory} from "../../common/utilTool.js";
import {DivTag} from "../../common/tagUtil.js";
import {deleteCardModalBackgroundImg, setCardWriteProgressBar} from "../cardList/cardListEvent.js";

bindEventToCardCreatePage();

export function bindEventToCardCreatePage() {
    const cardCreateBtn = document.getElementById("card-submit-btn");
    cardCreateBtn.addEventListener("click", createCardClick);

    const cardDeleteBtn = document.querySelector("#card-delete-btn");
    cardDeleteBtn.addEventListener("click", deleteCardBtnClick);

    const cardWriteContent = document.getElementById("card-write-editor");
    cardWriteContent.addEventListener("input", inputCardWriteText);

    const cardImageInput = document.querySelector("#card-img");
    cardImageInput.addEventListener("input", inputImageUrl);
}

async function createCardClick() {
    const dataset = this.dataset;
    const cardImgUrl = document.querySelector("#card-img").value;
    const cardTitle = document.querySelector("#card-title").value;
    const cardContent = document.querySelector("#card-content").value;

    if (!cardTitle) {
        alert("card title 값은 필수입니다.");
        return;
    }

    if (!cardContent) {
        alert("card content 값은 필수입니다.");
        return;
    }

    if (cardContent.length <= 250) {
        await createCardApiCall(cardTitle, cardImgUrl, cardContent, dataset.memberCardId, dataset.type);
        location.reload();
        alert("카드 수정/생성 성공했습니다.");

    } else {
        alert("card content는 길이가 250 이하로 작성이 가능합니다.")
    }
}

async function deleteCardBtnClick() {
    const cardId = this.dataset.cardId;
    const type = this.dataset.type;

    await deleteCardApiCall(cardId, type);
    location.reload();

    alert("카드를 삭제했습니다.");
}

async function inputCardWriteText() {
    const goal = document.querySelector("#goal-content").value
    const input = document.querySelector("#card-write-editor").value

    if (goal === input) {
        await increaseWriteCount(this.dataset.memberCardId);

        setCardWriteProgressBar()

        const popover = document.querySelector(".popover-body");
        popover.innerHTML = "훌륭해요! +1";
        popover.style.color = "#0d6efd";

        const emoji = document.querySelector("#write-correct-emoji")
        emoji.style.backgroundColor = "#0d6efd";
        emoji.style.color = "white";
        emoji.style.border = "solid 1px #0d6efd";

        animateCSS("#write-correct-emoji", "shakeY").then((message) => {
            emoji.style.border = "solid 1px black";
            emoji.style.color = "black";
            emoji.style.backgroundColor = "white";

            popover.innerHTML = "위 카드 본문을 아래에 써보세요!";
            popover.style.color = "black";
        });

        this.value = "";

        if (isGoalAchieved() && !isGetTodayStamp()) {
            getTodayStamp();
            
            const closeBtn = document.querySelector("#write-modal-close-btn");
            closeBtn.click();

            const achieveModal = document.querySelector("#card-goal-achieve-modal");
            new bootstrap.Modal(achieveModal).show();
        }

    } else {
        animateCSS("#write-correct-emoji", "shakeX").then();
    }
}

async function increaseWriteCount(cardId) {

    const todayCardWriteHistory = new TodayCardWriteHistory();

    const memberCardId = cardId;

    todayCardWriteHistory.increaseWriteCount(memberCardId).save()

    await increaseCardWriteCount(memberCardId, 1);

    if (isGoalAchieved() && !isGetTodayStamp()) {
        await rewardStampToUser();
    }

    document.querySelector("#card-write-count").innerHTML
        = todayCardWriteHistory.memberCards[memberCardId];
}

function inputImageUrl() {

    // 기존에 추가한 이미지 삭제
    deleteCardModalBackgroundImg();

    const imgUrl = this.value;
    isImageUrlValid(imgUrl, function (isValid) {
        if (isValid) {
            const virtualImageBox = new DivTag()
                .setClassName("card-modal-background");

            virtualImageBox.setStyle([{
                backgroundImage: `url(${imgUrl})`,
                objectFit: "cover",
                position: "absolute",
                top: "0px",
                left: "0px",
                right: "0px",
                bottom: "0px",
                opacity: 0.3
            }]);

            document.querySelector("#modal-img-content")
                .appendChild(virtualImageBox.getTag());

        } else if (!imgUrl) {

        } else {
            alert('이미지 URL이 유효하지 않거나 이미지가 존재하지 않습니다.');
        }
    });
}

function isGoalAchieved() {
    const todayCardWriteHistory = new TodayCardWriteHistory();

    return todayCardWriteHistory.isTotalCountMoreThanGoal();
}

function isGetTodayStamp() {
    const todayCardWriteHistory = new TodayCardWriteHistory();

    return todayCardWriteHistory.isGetTodayStamp;
}

function getTodayStamp() {
    const todayCardWriteHistory = new TodayCardWriteHistory();
    todayCardWriteHistory.achieveTodayStamp();
    todayCardWriteHistory.save();
}