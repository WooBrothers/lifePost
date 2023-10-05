import {increaseCardWriteCount, rewardStampToUser} from "./cardModalApi.js";
import {animateCSS, removeHTMLAndWhitespace, TodayCardWriteHistory} from "../../common/utilTool.js";
import {SpanTag} from "../../common/tagUtil.js";
import {setCardWriteProgressBar} from "../list/cardListEvent.js";

bindEventToCardWriteModal();

export function bindEventToCardWriteModal() {
    const cardWriteContent = document.getElementById("card-write-editor");
    cardWriteContent.addEventListener("input", inputCardText);

    cardWriteContent.addEventListener("paste", function (e) {
        e.preventDefault();
    });
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
        target.value = "";

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