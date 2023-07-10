import {CardListApi} from "./cardListApi.js";
import {ButtonTag, DivTag, PTag} from "../../common/tagUtil.js";
import {bindEventToCardListGrid} from "./cardListEvent.js";

export class CardListGrid {

    static cardListBtnClass = "card-list-btn";
    static filterClass = "filter";
    static cardSpaceClass = "card-space";
    static cardSpaceBtnClass = "card-space-btn";
    static paginationBtnClass = "pagination-btn"

    static async createCardListSpace(cardListSpace) {
        // api call -> 카드 리스트 조회
        // card 생성
        // card space에 카드 추

        await CardListApi.getCardList().then(response => {
            CardListGrid.createCard(response, cardListSpace);
        });
        bindEventToCardListGrid();
    }

    static createCard(response, cardListSpace) {

        response.content.forEach(content => {
            const cardSpace = new DivTag()
                .setClassName(CardListGrid.cardSpaceClass)
                .setId(`card-space-${content.cardId}}`)
                .setDataset([{
                    memberCardId: content.memberCardId,
                    cardId: content.cardId,
                    type: content.type,
                }])
                .getTag();

            // focus 여부에 따라 북마크 이미지 변경 처리
            let focusImgUrl = CardListGrid.getFocusImgByContent(content);
            // 카드 리스트 생성 메서드
            CardListGrid.setCardGridToCardSpaceByContent(content, focusImgUrl, cardSpace);
            CardListGrid.setCardListPagination(content, cardSpace);

            cardListSpace.appendChild(cardSpace);
        });

    }

    static getFocusImgByContent(content) {
        if (content.focus === "ATTENTION") {
            return "/img/focus-mark-on.png";
        } else if (content.focus === "NON") {
            return "/img/focus-mark-off.png";
        } else {
            throw new Error("content focus type 에러.");
        }
    }

    static setCardGridToCardSpaceByContent(content, focusImgUrl, cardSpace) {
        const focusBtn = new ButtonTag()
            .setClassName(CardListGrid.cardSpaceClass + " focus-btn")
            .setBackground(focusImgUrl)
            .getTag();
        const cardContent = new PTag()
            .setClassName(CardListGrid.cardSpaceClass)
            .setInnerHTML(content.contents)
            .getTag();
        const letterInfo = new DivTag()
            .setInnerHTML("letter-info")
            .setClassName(CardListGrid.cardSpaceClass)
            .setInnerHTML([
                new PTag()
                    .setClassName(CardListGrid.cardSpaceClass)
                    .setInnerHTML(content.createdDate)
                ,
                new PTag()
                    .setClassName(CardListGrid.cardSpaceClass)
                    .setInnerHTML(content.previewLetterContent)
            ])
            .getTag();

        const readLetterBtn = new ButtonTag()
            .setClassName("card-list-btn card-action-btn card-space-read-letter-btn")
            .setInnerHTML("편지 읽기")
            .getTag();
        const writeAffirmationCardBtn = new ButtonTag()
            .setClassName("card-list-btn card-action-btn card-space-write-card-btn")
            .setInnerHTML("확언 쓰기")
            .getTag();

        cardSpace.appendChild(focusBtn);
        cardSpace.appendChild(cardContent);
        cardSpace.appendChild(letterInfo);
        cardSpace.appendChild(readLetterBtn);
        cardSpace.appendChild(writeAffirmationCardBtn);
    }

    static setCardListPagination(content, cardSpace) {
        
    }
}