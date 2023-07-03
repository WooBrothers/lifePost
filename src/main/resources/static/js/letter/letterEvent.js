import {LetterGrid} from "./letterGrid.js"


export function bindEventToLetterGrid() {

    // 오늘의 편지 그리드 컨테이너를 담을 div -> 편지 보기 클릭 이벤트 바인딩
    const todayLetterGrid = document.getElementById("today-letter-grid");
    todayLetterGrid.addEventListener("click", clickTodayLetter);

}

export function bindEventToLetterGridContainer() {

    // 편지 그리드 컨네이너를 담을 div > 무한 스크롤 이벤트 바인딩
    const letterGridContainers = document.getElementById("letter-grid-containers");
    letterGridContainers.addEventListener("scroll", addGridScrollEvent);
}

async function addGridScrollEvent() {
    /* letter-grid-containers -> scroll event 함수
    *
    * this : event가 발생한(클릭된) letter grid container 엘리멘트
    * */

    if (isBottomOfScroll(this)) {
        // 마지막 페이지에 도달했다면 letter 추가

        const latestLetterId = document.getElementById("letter-grid-containers")
            .lastElementChild
            .dataset.id;

        await LetterGrid.createGridByLetterId(latestLetterId, this);
    }
}

function isBottomOfScroll(element) {
    /*
    * 스크롤 중인 그리드의 현재 엘리먼트가 바텀(맨아래)인지 확인
    *
    * 스크롤의 마지막 요소의 위치
    * = 모든 컨텐츠의 총 높이
    * = 현재 그리드의 높이(컨텐츠 자신의 높이 제외) + 고정된 높이(컨텐츠 자신 높이) + 마진
    */

    // 출력되는 스크롤 뷰의 고정된 높이 (변하지 않는 화면 상의 높이)
    const letterGridViewHeight = element.clientHeight;
    // 스크롤을 내렸을 때 0부터 증가하는 현재의 그리드 높이 (점차 증가하는 높이)
    const currentGridHeight = element.scrollTop;
    // 그리드 내의 모든 컨텐츠의 높이를 더한 총 높이 (모든 컨텐츠 끝까지의 높이)
    const letterGridTotalHeight = element.scrollHeight;
    // 그리드 mairgin
    const margin = 5;

    return letterGridViewHeight + currentGridHeight + margin >= letterGridTotalHeight;
}

function clickTodayLetter() {
    // 오늘의 편지 클릭 이벤트
    console.log("click today letter.");
    console.log(window.innerWidth);

}

