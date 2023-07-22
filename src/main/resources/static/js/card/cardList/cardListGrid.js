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
                .setInnerHTML(content.createdDate + "<br/><b>Letter</b> " + content.letterTitle)
            );
        } else {
            innerHtmlList.push(new PTag()
                .setInnerHTML(content.createdDate + "<br/><b>Custom</b> " + "내가 만든 카드")
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

    static createCardListPagination(response, cardListPaginationSpace, event) {
        const currentPageNo = response.pageable.pageNumber + 1;
        const totalPageCount = response.totalPages;

        const pageSection = Math.floor(currentPageNo / 5);
        const pageBtnClassName = "card-list-page-btn";
        let lastPageNo = 0;
        let firstPageNo = pageSection * 5 + 1;

        const pageBtnSpace = new DivTag()
            .setId("page-btn-space")
            .getTag();

        for (let pageNo = pageSection * 5 + 1; CardListGrid.isTurnedTotalPage(pageNo, totalPageCount, pageSection); pageNo++) {
            CardListGrid.createPageBtn(pageNo, currentPageNo, pageBtnClassName, pageBtnSpace);
            lastPageNo = pageNo;
        }

        CardListGrid.createNextPageBtn(lastPageNo, totalPageCount, pageBtnClassName, pageBtnSpace);
        CardListGrid.createPreviousBtn(pageSection, firstPageNo, pageBtnClassName, pageBtnSpace);

        cardListPaginationSpace.appendChild(pageBtnSpace);
        event();
    }

    static isTurnedTotalPage(pageNo, totalPageCount, pageSection) {
        return pageNo <= totalPageCount && pageNo < pageSection * 5 + 6;
    }

    static createPageBtn(pageNo, currentPageNo, pageBtnClassName, pageBtnSpace) {
        let selectedPage = pageNo === currentPageNo ? " on" : "";

        const pageBtn = new ButtonTag()
            .setClassName(pageBtnClassName + selectedPage)
            .setDataset([{pageNo: pageNo}])
            .setInnerHTML(pageNo)
            .getTag();

        pageBtnSpace.appendChild(pageBtn);
    }

    static createNextPageBtn(lastPageNo, totalPageCount, pageBtnClassName, pageBtnSpace) {
        // 즉 현재 페이지 섹션의 마지막 페이지 번호가 페이지 토탈보다 작으면 넥스트 버튼 출력
        if (lastPageNo < totalPageCount) {
            const nextPageBtn = new ButtonTag()
                .setClassName(pageBtnClassName + " next-btn")
                .setDataset([{pageNo: lastPageNo + 1}])
                .setInnerHTML(">")
                .getTag();
            pageBtnSpace.appendChild(nextPageBtn);
        }
    }

    static createPreviousBtn(pageSection, firstPageNo, pageBtnClassName, pageBtnSpace) {
        // 5보다 큰 섹션이라면 이전 버튼 추가
        if (pageSection > 0) {
            const nextPageBtn = new ButtonTag()
                .setClassName(pageBtnClassName + " before-btn")
                .setDataset([{pageNo: firstPageNo - 5}])
                .setInnerHTML("<")
                .getTag();
            pageBtnSpace.insertBefore(nextPageBtn, pageBtnSpace.firstChild);
        }
    }
}