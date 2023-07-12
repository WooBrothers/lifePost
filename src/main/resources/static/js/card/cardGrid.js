import {bindEventToCardGrid} from "./cardEvent.js"
import {CardApi} from "./cardApi.js";
import {ButtonTag, DivTag} from "../common/tagUtil.js";

export class CardGrid {
    /* later grid를 만드는 함수 */

    static async createFocusCardGrid(focusCardGrid) {

        await CardApi.getFocusCards(1).then(response => {
            CardGrid.setFocusCardsGrid(focusCardGrid, response);
            bindEventToCardGrid();
        });
    }

    static setFocusCardsGrid(focusCardGrid, response) {

        const focusCardGridContainerTag = new DivTag()
            .setClassName("focus-card-container")
            .setId("focus-card-container-div")
            .getTag();

        if (response && response.content.length > 0) {
            CardGrid.getFocusCardDivInfos(response, focusCardGridContainerTag);
        } else {
            CardGrid.getNonFocusCardDivInfos(focusCardGridContainerTag);
        }

        focusCardGrid.appendChild(focusCardGridContainerTag);
    }

    static getFocusCardDivInfos(response, focusCardGridContainerTag) {

        const tile = new DivTag()
            .setClassName("focus-card-container")
            .setId("focus-card-title")
            .setInnerHTML("Focus Card");

        const focusCardDivs = CardGrid.getFocusCardListElementByResponse(response);

        const cardContainer = new DivTag()
            .setClassName("focus-card-container")
            .setId("focus-card-box")
            .setInnerHTML(focusCardDivs);

        const buttonDiv = new DivTag()
            .setClassName("focus-card-container")
            .setId("focus-card-btn-div");

        const nextBtn = new ButtonTag()
            .setId("focus-card-next-btn")
            .setInnerHTML("다음 >");

        const previousBtn = new ButtonTag()
            .setId("focus-card-previous-btn")
            .setInnerHTML("< 이전");

        buttonDiv.getTag().appendChild(previousBtn.getTag());
        buttonDiv.getTag().appendChild(nextBtn.getTag());


        focusCardGridContainerTag.appendChild(tile.getTag());
        focusCardGridContainerTag.appendChild(cardContainer.getTag());
        focusCardGridContainerTag.appendChild(buttonDiv.getTag());

        return focusCardGridContainerTag;
    }

    static getNonFocusCardDivInfos(focusCardGridContainerTag) {

        const title = new DivTag().setId("non-focus-card-title")
            .setClassName("non-focus-card-container")
            .setInnerHTML("Focus Card")
            .getTag();
        const empty = new DivTag().setId("non-focus-card-empty")
            .setClassName("non-focus-card-container")
            .setInnerHTML("")
            .getTag();
        const info = new DivTag().setId("non-focus-card-info")
            .setClassName("non-focus-card-container")
            .setInnerHTML("편지를 읽고 얻은 카드 중<br/>마음에 새기고 싶은 카드를 골라보세요!")
            .getTag();
        const buttonSpace = new DivTag().setId("non-focus-card-button-space")
            .setClassName("non-focus-card-container")
            .setInnerHTML([new ButtonTag()
                .setInnerHTML("+")
                .setId("non-focus-card-focus-button")])
            .getTag();
        const comment = new DivTag().setId("non-focus-card-comment")
            .setClassName("non-focus-card-container")
            .setInnerHTML("자주 일고 되뇌이다 보면<br/>당신이 원하는 모습이 되어 있을 거에요!")
            .getTag();

        focusCardGridContainerTag.appendChild(title);
        focusCardGridContainerTag.appendChild(empty);
        focusCardGridContainerTag.appendChild(info);
        focusCardGridContainerTag.appendChild(buttonSpace);
        focusCardGridContainerTag.appendChild(comment);
    }


    static getFocusCardListElementByResponse(response) {

        const divList = [];
        let zIdx = 1;
        response.content.forEach(content => {
            divList.push(new DivTag()
                .setClassName("focus-card-container-contents")
                .setId("focus-card-content-" + zIdx)
                .setStyle([{"z-index": zIdx}])
                .setInnerHTML(content.contents)
                .setDataset(
                    [{
                        "memberCardId": content.memberCardId,
                        "cardId": content.cardId,
                        "type": content.type,
                        "createdAt": content.createdAt,
                        "updateAt": content.updateAt
                    }]))
            zIdx++;
        });

        return divList;
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

