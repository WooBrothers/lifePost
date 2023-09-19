import {
    createCardApiCall,
    deleteCardApiCall,
    increaseCardWriteCount,
    rewardStampToUser
} from "./cardModalApi.js";
import {
    animateCSS,
    isImageUrlValid,
    removeHTMLAndWhitespace,
    TodayCardWriteHistory
} from "../../common/utilTool.js";
import {DivTag, SpanTag} from "../../common/tagUtil.js";
import {deleteCardModalBackgroundImg, setCardWriteProgressBar} from "../cardList/cardListEvent.js";

bindEventToCardCreatePage();

export function bindEventToCardCreatePage() {
    const cardCreateBtn = document.getElementById("card-submit-btn");
    cardCreateBtn.addEventListener("click", createCardClick);

    const cardDeleteBtn = document.querySelector("#card-delete-btn");
    cardDeleteBtn.addEventListener("click", deleteCardBtnClick);

    const cardWriteContent = document.getElementById("card-write-editor");
    // cardWriteContent.addEventListener("input", inputCardWriteText);
    cardWriteContent.addEventListener("input", inputCardText);

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

async function inputCardText() {
    const goalElement = document.querySelector("#goal-content");
    const goal = removeHTMLAndWhitespace(goalElement.innerHTML);
    const input = document.querySelector("#card-write-editor").value;

    let result = "";
    let beforeTag = null;
    let isWrong = false;

    if (goal.length === input.length && goal === input) {
        goalElement.innerHTML = goal;
        await inputCardWriteText(this);
    } else if (input.length <= goal.length) {
        for (let idx = 0; idx < input.length; idx++) {
            // 맞을때
            if (goal[idx] === input[idx]) {
                if (!beforeTag) {
                    // 첫글자
                    beforeTag = new SpanTag()
                        .setClassName("text-primary");
                    beforeTag.getTag().innerHTML = goal[idx];
                } else if (beforeTag.getTag().className === "text-primary") {
                    // 지속해서 맞음
                    beforeTag.getTag().innerHTML += goal[idx];
                } else if (beforeTag.getTag().className === "text-danger") {
                    // 틀리다가 맞음
                    result += beforeTag.getTag().outerHTML;
                    beforeTag = new SpanTag()
                        .setClassName("text-primary");
                    beforeTag.getTag().innerHTML = goal[idx];
                } else {
                    throw new Error("class name 에러");
                }
            } else {
                // 틀릴때
                if (!beforeTag) {
                    // 첫글자
                    beforeTag = new SpanTag()
                        .setClassName("text-danger");
                    beforeTag.getTag().innerHTML = goal[idx];
                } else if (beforeTag.getTag().className === "text-danger") {
                    // 지속해서 틀림
                    beforeTag.getTag().innerHTML += goal[idx];
                } else if (beforeTag.getTag().className === "text-primary") {
                    // 맞다가 틀림
                    result += beforeTag.getTag().outerHTML;
                    beforeTag = new SpanTag()
                        .setClassName("text-danger");
                    beforeTag.getTag().innerHTML = goal[idx];
                } else {
                    throw new Error("class name 에러");
                }
                // 하나라도 틀렸다면
                isWrong = true;
            }
        }
        // result에 추가되지 않은 마지막 태그 추가
        result += beforeTag.getTag().outerHTML;
        goalElement.innerHTML = result + goal.substr(input.length);

        // isWrong에 따라 이모지 및 팝오버 세팅
        setEmojiAndPopover(isWrong);
    }
}

async function inputCardWriteText(target) {
    const goal = document.querySelector("#goal-content").innerHTML;
    const input = document.querySelector("#card-write-editor").value;

    if (goal === input) {
        await increaseWriteCount(target.dataset.memberCardId);

        setCardWriteProgressBar();

        const popover = document.querySelector(".popover-body");
        popover.innerHTML = "훌륭해요! +1";
        popover.style.color = "#0d6efd";

        const emoji = document.querySelector("#write-correct-emoji")
        emoji.style.backgroundColor = "#0d6efd";
        emoji.style.color = "white";
        emoji.style.border = "solid 1px #0d6efd";

        animateCSS("#write-correct-emoji", "shakeY").then((message) => {
            setEmojiDefault(emoji);

            popover.innerHTML = "위 카드 본문을 아래에 써보세요!";
            popover.style.color = "black";
        });

        target.value = "";

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

function setEmojiDefault(emoji) {
    emoji.style.border = "solid 1px black";
    emoji.style.color = "black";
    emoji.style.backgroundColor = "white";
}

function setEmojiAndPopover(isWrong) {
    const emoji = document.querySelector("#write-correct-emoji");
    const popover = document.querySelector(".popover-body");

    if (isWrong) {
        emoji.style.backgroundColor = "#dc3545"; // danger
        emoji.style.color = "white";
        emoji.style.border = "solid 1px #dc3545";
        popover.innerHTML = "오타가 있어요!";
        popover.style.color = "#dc3545"

        animateCSS("#write-correct-emoji", "shakeX").then((message) => {
            setEmojiDefault(emoji);
        });
    } else {
        emoji.style.backgroundColor = "#0d6efd"; // primary
        emoji.style.color = "white";
        emoji.style.border = "solid 1px #0d6efd";
        popover.innerHTML = "좋아요! 힘내요!";
        popover.style.color = "#0d6efd";

        animateCSS("#write-correct-emoji", "shakeY").then((message) => {
            setEmojiDefault(emoji);
        });
    }
}