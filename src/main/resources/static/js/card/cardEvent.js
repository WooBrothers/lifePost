export function bindEventToCardGrid() {
    /* card grid 의 이벤트 바인딩 */

    // 오늘의 카드 그리드 컨테이너를 담을 div -> 카드 보기 클릭 이벤트 바인딩
    const nextBtn = document.getElementById("focus-card-next-btn");
    if (nextBtn) {
        nextBtn.addEventListener("click", clickNextFocusCard);
    }

    const prevBtn = document.getElementById("focus-card-previous-btn");
    if (prevBtn) {
        prevBtn.addEventListener("click", clickPreviousFocusCard);
    }

    const focusAddButton = document.getElementById("non-focus-card-focus-button");
    if (focusAddButton) {
        focusAddButton.addEventListener("click", clickFocusAdd);
    }
}

function clickNextFocusCard() {

    const focusCardBox = document.getElementById("focus-card-box");

    let maxIndex = 0;
    let maxCardId = null;

    // 카드의 순서를 바꾸는 로직
    for (const card of focusCardBox.children) {
        const zIndex = parseInt(card.style.zIndex);
        card.style.zIndex = `${zIndex + 1}`;
        if (maxIndex < zIndex) {
            maxIndex = zIndex;
            maxCardId = card.id;
        }
    }
    // 현재 카드를 뒤로 넘기는 애니메이션
    let targetCard = document.getElementById(maxCardId);
    targetCard.style.animationName = "flip-over-card";
    targetCard.style.animationDuration = "0.5s";
    targetCard.style.animationDirection = "alternate";
    targetCard.style.animationIterationCount = "2";

    setTimeout(function () {
        targetCard.style.zIndex = "1";
    }, 500);

    setTimeout(function () {
        targetCard.style.animationName = null;
        targetCard.style.animationDuration = null;
        targetCard.style.animationDirection = null;
        targetCard.style.animationIterationCount = null;
    }, 1000);
}

function clickPreviousFocusCard() {

    const focusCardBox = document.getElementById("focus-card-box");

    let minIndex = 99999;
    let minCardId = null;

    for (const card of focusCardBox.children) {
        const zIndex = parseInt(card.style.zIndex);
        card.style.zIndex = `${zIndex - 1}`;
        if (minIndex > card.style.zIndex) {
            minIndex = card.style.zIndex;
            minCardId = card.id;
        }
    }

    // 현재 카드를 뒤로 넘기는 애니메이션
    let targetCard = document.getElementById(minCardId);
    targetCard.style.animationName = "flip-over-card";
    targetCard.style.animationDuration = "0.5s";
    targetCard.style.animationDirection = "alternate";
    targetCard.style.animationIterationCount = "2";

    setTimeout(function () {
        document.getElementById(minCardId).style.zIndex = focusCardBox.children.length;
    }, 500);

    setTimeout(function () {
        targetCard.style.animationName = null;
        targetCard.style.animationDuration = null;
        targetCard.style.animationDirection = null;
        targetCard.style.animationIterationCount = null;
    }, 1000);

}

function clickFocusAdd() {
    location.href = "/auth/card/list";
}