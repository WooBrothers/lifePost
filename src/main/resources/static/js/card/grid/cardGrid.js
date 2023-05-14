export class CardGrid {
    /* later grid를 만드는 함수 */

    static createCardTextGrid(todayCardGrid) {
        /* 전달하는 div 객체에 자식으로 card grid를 추가 함수 */
        // TODO innerHTML은 서버에서 받은 값으로 변경

        const cardTextGrid = document.createElement("div");

        cardTextGrid.className = "card-text-grid bg-primary";
        cardTextGrid.innerHTML = "title";

        todayCardGrid.appendChild(cardTextGrid);
    }

    static createCardImgGrid(todayCardGrid) {
        /* 카드 이미지 그리드 생성 함수 */

        const cardImgGrid = document.createElement("div");

        cardImgGrid.className = "card-grid-image bg-warning";
        cardImgGrid.innerHTML = "card image";

        todayCardGrid.appendChild(cardImgGrid);
    }

    static createCardGridByPageIndex(pageIndex, todayCardGrid) {
        /* 카드 그리드 조합 함수 */

        CardGrid.createCardImgGrid(todayCardGrid);

        CardGrid.createCardTextGrid(todayCardGrid);
    }

    static ClickTodayLetter() {
        // 오늘의 편지 클릭 이벤트
        console.log("click today letter.");
    }

    // 그리드 이벤트 바인딩 함수
    static BindEventToCardGrid() {

    }
}

