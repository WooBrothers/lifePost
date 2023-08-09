import {CardListApi} from "./cardListApi.js";
import {ButtonTag, DivTag, PTag} from "../../common/tagUtil.js";
import {getColorHexInFiveTypeList} from "../../common/utilTool.js";

export class CardListGrid {

    static cardSpaceClass = "card-space";

    static async createCardListSpace(cardListSpace, page, event) {

        const type = CardListGrid.getCardType();
        const focus = CardListGrid.getIsFocus();

        let resultResponse = null;
        await CardListApi.getCardList(page, type, focus).then(response => {
            CardListGrid.createCard(response, cardListSpace);
            resultResponse = response;
        });

        event();
        return resultResponse
    }

    static getCardType() {
        const isAffirmationOn = document.getElementById("affirmation-filter-btn").dataset.onOff;
        const isCustomBtnOn = document.getElementById("custom-filter-btn").dataset.onOff;

        let resultType = [];
        if (isAffirmationOn === "true") {
            resultType.push("AFFIRMATION");
        }
        if (isCustomBtnOn === "true") {
            resultType.push("CUSTOM");
        }

        return resultType ? resultType : null;
    }

    static getIsFocus() {
        const isFocusBtnOn = document.getElementById("focus-filter-btn").dataset.onOff;

        return isFocusBtnOn === "true" ? "ATTENTION" : null;
    }

    static createCard(response, cardListSpace) {

        const preEmptyContentDiv = document.querySelector(".empty-content");
        if (preEmptyContentDiv) {
            preEmptyContentDiv.remove();
        }

        if (response.content.length === 0) {
            const emptyContentDiv = new DivTag()
                .setClassName("empty-content")
                .setStyle()
                .setInnerHTML("카드가 없습니다. 편지을 읽거나 직접 만들어 보세요!")
            cardListSpace.appendChild(emptyContentDiv.getTag());
            return;
        }

        response.content.forEach(content => {
            const cardSpace = new DivTag()
                .setClassName(CardListGrid.cardSpaceClass)
                .setId(`card-space-${content.cardId}`)
                .setStyle([{"background-color": getColorHexInFiveTypeList()}])
                .setDataset([{
                    memberCardId: content.memberCardId,
                    cardId: content.cardId,
                    type: content.type,
                }])
                .getTag();

            // focus 여부에 따라 북마크 이미지 변경 처리
            const focusInfo = CardListGrid.getFocusImgByContent(content);
            // 카드 리스트 생성 메서드
            CardListGrid.setCardGridToCardSpaceByContent(content, focusInfo, cardSpace);

            cardListSpace.appendChild(cardSpace);
        });
    }

    static getFocusImgByContent(content) {
        let result = {};
        if (content.focus === "ATTENTION") {
            result.focus = true;
            result.focusImgUrl = "/img/focus-mark-on.png";
            return result
        } else if (content.focus === "NON") {
            result.focus = false;
            result.focusImgUrl = "/img/focus-mark-off.png";
            return result;
        } else {
            throw new Error("content focus type 에러.");
        }
    }

    static setCardGridToCardSpaceByContent(content, focusInfo, cardSpace) {

        const headerSpace = new DivTag()
            .setStyle([{alignSelf: "stretch"}])
            .setInnerHTML([
                new DivTag()
                    .setClassName("focus-space")
                    .setInnerHTML([
                        new ButtonTag()
                            .setClassName("focus-btn")
                            .setDataset([{focus: focusInfo.focus}])
                            .setBackground(focusInfo.focusImgUrl)
                    ]),
                new PTag()
                    .setId("card-content-" + content.cardId)
                    .setInnerHTML(content.contents)
                    .setStyle([{
                        fontSize: "20px",
                        fontWeight: "bold",
                        alignSelf: "flex-start"
                    }])
            ])
        cardSpace.appendChild(headerSpace.getTag());

        const spacer = new DivTag()
            .setClassName("spacer");
        cardSpace.appendChild(spacer.getTag());

        const letterInfoSpace = CardListGrid.createLetterInfoSpace(content);
        cardSpace.appendChild(letterInfoSpace.getTag());

        const actionBtnSpace = CardListGrid.createActionBtnSpace(content);
        cardSpace.appendChild(actionBtnSpace.getTag());
    }

    static createLetterInfoSpace(content) {

        let innerHtmlList = [
            new DivTag()
                .setStyle([{borderTop: "1px solid gray"}]),
        ]

        if (content.type !== "CUSTOM") {
            innerHtmlList.push(new PTag()
                .setInnerHTML(content.postDate + "<br/><b>Letter</b> " + content.letterTitle)
            );
        } else {
            innerHtmlList.push(new PTag()
                .setInnerHTML(content.postDate + "<br/><b>Custom</b> " + "내가 만든 카드")
            );
        }

        return new DivTag()
            .setStyle([{
                display: "flex",
                flexDirection: "column",
                alignSelf: "stretch",
            }]).setInnerHTML(innerHtmlList);
    }

    static createActionBtnSpace(content) {
        let cardActionBtnList = [new ButtonTag()
            .setClassName("card-list-btn card-action-btn card-write-btn")
            .setId("card-space-write-card-btn")
            .setInnerHTML("확언 쓰기")
        ];

        if (content.type !== "CUSTOM") {
            cardActionBtnList.push(new ButtonTag()
                .setClassName("card-list-btn card-action-btn letter-read-btn")
                .setDataset([{letterId: content.letterId}])
                .setId("card-space-read-letter-btn")
                .setInnerHTML("편지 읽기"));
        }

        return new DivTag()
            .setStyle([{display: "flex", alignSelf: "stretch", margin: "auto"}])
            .setInnerHTML(cardActionBtnList);
    }
}