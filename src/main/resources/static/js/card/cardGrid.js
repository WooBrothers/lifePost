import {bindEventToCardGrid} from "./cardEvent.js"
import {addDivByDivInfosToParent, ButtonTag, DivTag} from "../common.js";

export class CardGrid {
    /* later grid를 만드는 함수 */

    static FOCUS_CARD_PREFIX = "focus-card";
    static FOCUS_CARD_CONTAINER_CLASS_PREFIX = "focus-card-container";

    static async createFocusCardGrid(todayCardGrid) {

        // await CardApi.getFocusCards().then(res => {
        //     CardGrid.setFocusCardsGrid(todayCardGrid, res);
        // });

        CardGrid.setFocusCardsGrid(todayCardGrid, null);
    }

    static setFocusCardsGrid(focusCardGrid, card) {
        const focusCardGridContainerTag = new DivTag()
            .setClassName(CardGrid.FOCUS_CARD_CONTAINER_CLASS_PREFIX)
            .getTag();

        let divInfos = [];
        if (card) {
            divInfos = CardGrid.getNonFocusCardDivInfos();
        } else {
            divInfos = CardGrid.getNonFocusCardDivInfos();
        }

        addDivByDivInfosToParent(divInfos, focusCardGridContainerTag);
        focusCardGrid.appendChild(focusCardGridContainerTag);
    }

    static getNonFocusCardDivInfos() {
        return [
            {
                id: CardGrid.FOCUS_CARD_PREFIX + "title",
                className: CardGrid.FOCUS_CARD_CONTAINER_CLASS_PREFIX,
                innerHTML: "Focus Card"
            }, {
                id: CardGrid.FOCUS_CARD_PREFIX + "info",
                className: CardGrid.FOCUS_CARD_CONTAINER_CLASS_PREFIX,
                innerHTML: "편지를 읽고 얻은 카드 중<br/>마음에 새기고 싶은 카드를 골라보세요!"
            }, {
                id: CardGrid.FOCUS_CARD_PREFIX + "button",
                className: CardGrid.FOCUS_CARD_CONTAINER_CLASS_PREFIX,
                innerHTML: [new ButtonTag()]
            }, {
                id: CardGrid.FOCUS_CARD_PREFIX + "comment",
                className: CardGrid.FOCUS_CARD_CONTAINER_CLASS_PREFIX,
                innerHTML: "자주 일고 되뇌이다 보면<br/>당신이 원하는 모습이 되어 있을 거에요!"
            }
        ];

    }

    static createCardGridByPageIndex(todayCardGrid) {
        /* 카드 그리드 조합 함수 */

        CardGrid.createCardImgGrid(todayCardGrid);
        CardGrid.createCardTextGrid(todayCardGrid);

        /* 이벤트 바인딩 */
        bindEventToCardGrid();
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

