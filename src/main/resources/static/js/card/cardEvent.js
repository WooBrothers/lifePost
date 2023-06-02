export function bindEventToCardGrid() {
    /* card grid 의 이벤트 바인딩 */

    // 오늘의 카드 그리드 컨테이너를 담을 div -> 카드 보기 클릭 이벤트 바인딩
    const todayCardGridContainers = document.getElementById("today-card-grid");
    todayCardGridContainers.addEventListener("click", clickTodayCard);
}

function clickTodayCard() {
    // 오늘의 편지 클릭 이벤트
    console.log("clicked today card.");
}

