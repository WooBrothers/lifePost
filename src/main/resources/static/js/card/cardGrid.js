export class CardGrid {
    /* later grid를 만드는 함수 */

    static createCardGridByPageIndex(todayCardGrid) {
        /* 카드 그리드 조합 함수 */

        CardGrid.createCardImgGrid(todayCardGrid);
        CardGrid.createCardTextGrid(todayCardGrid);
    }

    static createCardTextGrid(todayCardGrid) {
        /* 전달하는 div 객체에 자식으로 card grid를 추가 함수 */

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
}

