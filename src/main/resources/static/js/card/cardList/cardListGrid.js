import {CardListApi} from "./cardListApi.js";
import {ButtonTag, DivTag, PTag} from "../../common/tagUtil.js";
import {getColorHexInFiveTypeList} from "../../common/utilTool.js";

export class CardListGrid {

    static cardSpaceClass = "card-space";

    static async createCardListSpace(cardListSpace, page, event) {

        let result = null;
        await CardListApi.getCardList(page).then(response => {
            CardListGrid.createCard(response, cardListSpace);
            result = response;
        });

        event();
        return result
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
                    .setInnerHTML(content.contents)
                    .setStyle([{
                        fontSize: "20px",
                        fontWeight: "bold",
                        alignSelf: "flex-start"
                    }])
            ])

        const spacer = new DivTag()
            .setClassName("spacer");

        const letterInfoSpace = new DivTag()
            .setStyle([{
                display: "flex",
                flexDirection: "column",
                alignSelf: "stretch",
            }])
            .setInnerHTML([
                new DivTag()
                    .setStyle([{borderTop: "1px solid gray"}]),
                new PTag()
                    .setInnerHTML(content.createdDate + "<br/><b>Letter</b> " + content.letterTitle)
            ]);

        const actionBtnSpace = new DivTag()
            .setStyle([{display: "flex", alignSelf: "stretch", margin: "auto"}])
            .setInnerHTML([
                new ButtonTag()
                    .setClassName("card-list-btn card-action-btn")
                    .setId("card-space-read-letter-btn")
                    .setInnerHTML("편지 읽기"),
                new ButtonTag()
                    .setClassName("card-list-btn card-action-btn")
                    .setStyle([{marginLeft: "10px"}])
                    .setId("card-space-write-card-btn")
                    .setInnerHTML("확언 쓰기")
            ]);

        cardSpace.appendChild(headerSpace.getTag());
        cardSpace.appendChild(spacer.getTag());
        cardSpace.appendChild(letterInfoSpace.getTag());
        cardSpace.appendChild(actionBtnSpace.getTag());
    }

    static setCardListPagination(response, cardListPaginationSpace, event) {
        const currentPageNo = response.pageable.pageNumber + 1;
        const totalPageCount = response.totalPages;

        const pageSection = Math.floor(currentPageNo / 5);
        const pageBtnClassName = "card-list-page-btn";
        let lastPageNo = 0;
        let firstPageNo = pageSection * 5 + 1;

        const pageBtnSpace = new DivTag()
            .setId("page-btn-space")
            .getTag();

        for (let pageNo = pageSection * 5 + 1; pageNo <= totalPageCount && pageNo < pageSection * 5 + 6; pageNo++) {
            let selectedPage = pageNo === currentPageNo ? " current-page" : "";

            const pageBtn = new ButtonTag()
                .setClassName(pageBtnClassName + selectedPage)
                .setDataset([{pageNo: pageNo}])
                .setInnerHTML(pageNo)
                .getTag();

            pageBtnSpace.appendChild(pageBtn);
            lastPageNo = pageNo;
        }

        // 즉 현재 페이지 섹션의 마지막 페이지 번호가 페이지 토탈보다 작으면 넥스트 버튼 출력
        if (lastPageNo < totalPageCount) {
            const nextPageBtn = new ButtonTag()
                .setClassName(pageBtnClassName + " next-btn")
                .setDataset([{pageNo: lastPageNo + 1}])
                .setInnerHTML(">")
                .getTag();
            pageBtnSpace.appendChild(nextPageBtn);
        }

        // 5보다 큰 섹션이라면 이전 버튼 추가
        if (pageSection > 0) {
            const nextPageBtn = new ButtonTag()
                .setClassName(pageBtnClassName + " before-btn")
                .setDataset([{pageNo: firstPageNo - 5}])
                .setInnerHTML("<")
                .getTag();
            pageBtnSpace.insertBefore(nextPageBtn, pageBtnSpace.firstChild);
        }

        cardListPaginationSpace.appendChild(pageBtnSpace);
        event();
    }
}