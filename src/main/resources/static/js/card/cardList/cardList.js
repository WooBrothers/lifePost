import {getCardList} from "./cardListApi.js";
import {ButtonTag, DivTag, HTag, ImgTag, PTag} from "../../common/tagUtil.js";
import {bindEventToCardListGrid} from "./cardListEvent.js";
import {createPagination} from "../../pagination/pagination.js";
import {bindPaginationBtnEvent} from "../../pagination/paginationEvent.js";

const cardListSpace = document.getElementById("card-list-space");
const response = await createCardListSpace(cardListSpace, 1, bindEventToCardListGrid);
await createPagination(response);
bindPaginationBtnEvent("card-list-space", "card-space", createCardListSpace, bindEventToCardListGrid);

export async function createCardListSpace(cardListSpace, page, event) {

    const cardType = getCardTypeFilter();
    const focusType = getIsFocusFilter();

    let resultResponse = null;

    await getCardList(page, 10, cardType, focusType).then(response => {
        createCard(response, cardListSpace);
        resultResponse = response;
    });

    event();
    return resultResponse
}

function getCardTypeFilter() {
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

function getIsFocusFilter() {
    const isFocusBtnOn = document.getElementById("focus-filter-btn").dataset.onOff;

    return isFocusBtnOn === "true" ? "ATTENTION" : null;
}

function createCard(response, cardListSpace) {
    const preEmptyContentDiv = document.querySelector(".empty-content");

    if (preEmptyContentDiv) {
        preEmptyContentDiv.remove();
    }

    // 카드 없을 시 안내문구 출력 및 렌더링 종료
    if (response.content.length === 0) {
        const emptyTexts = document.querySelectorAll(".card-empty-info-text")
        emptyTexts.forEach(ele => {
            ele.remove();
        })

        const emptyText = new DivTag()
            .setClassName("card-empty-info-text")
            .setInnerHTML("카드가 없습니다. 편지을 읽거나 카드를 만들어 보세요!");
        cardListSpace.appendChild(emptyText.getTag());

        return;
    }

    // API 응답 결과에 따른 카드 렌더링
    response.content.forEach(content => {
        const cardSpace = new DivTag()
            .setClassName("card-space col")
            .setId(`card-space-${content.cardId}`)
            .setDataset([{
                memberCardId: content.memberCardId,
                cardId: content.cardId,
                type: content.type,
            }])
            .getTag();

        // focus 여부에 따라 북마크 이미지 변경 처리
        const focusInfo = getFocusImgByContent(content);

        // 카드 리스트 생성 메서드
        setCardGridToCardSpaceByContent(content, focusInfo, cardSpace);

        cardListSpace.appendChild(cardSpace);
    });
}

function getFocusImgByContent(content) {
    let result = {};
    if (content.focus === "ATTENTION") {
        result.focus = true;
        result.focusImgUrl = "url('/img/focus-mark-on.png')";
        return result
    } else if (content.focus === "NON") {
        result.focus = false;
        result.focusImgUrl = "url('/img/focus-mark-off.png')";
        return result;
    } else {
        throw new Error("content focus type 에러.");
    }
}

function setCardGridToCardSpaceByContent(content, focusInfo, cardSpace) {
    if (!content.cardImg) {
        content.cardImg = "https://mdbcdn.b-cdn.net/img/new/standard/city/042.webp"
    }

    const cardColumn = new DivTag()
        .setClassName("card h-100")
        .setInnerHTML([
            new ImgTag()
                .setClassName("card-img-top")
                .setSrc(content.cardImg)
                .setAlt("..."),
            new DivTag()
                .setClassName("card-img-overlay p-1 d-flex justify-content-between")
                .setStyle([{height: "44px"}])
                .setInnerHTML([
                    new DivTag()
                        .setClassName("flex-fill"),
                    new ButtonTag()
                        .setClassName("focus-btn")
                        .setStyle([{
                            width: "36px",
                            height: "44px",
                            backgroundColor: "transparent",
                            backgroundImage: focusInfo.focusImgUrl,
                            border: "none"
                        }])
                ]),
            new DivTag()
                .setClassName("card-body")
                .setInnerHTML([
                    new HTag(5)
                        .setClassName("card-title")
                        .setInnerHTML(content.title),
                    new PTag()
                        .setId(`card-content-${content.cardId}`)
                        .setTextContent("card-text")
                        .setTextContent(content.contents)
                ]),
            new DivTag()
                .setClassName("card-footer text-center")
                .setInnerHTML(getCardActBtnListByType(content))
        ])

    cardSpace.appendChild(cardColumn.getTag());

    // const headerSpace = new DivTag()
    //     .setInnerHTML([
    //         new DivTag()
    //             .setClassName("focus-space")
    //             .setInnerHTML([
    //                 new ButtonTag()
    //                     .setClassName("focus-btn")
    //                     .setDataset([{focus: focusInfo.focus}])
    //                     .setBackground(focusInfo.focusImgUrl)
    //             ]),
    //         new PTag()
    //             .setId("card-content-" + content.cardId)
    //             .setClassName("p-card-content")
    //             .setTextContent(content.contents)
    //     ])
    // cardSpace.appendChild(headerSpace.getTag());
    //
    // const actionBtnSpace = createActionBtnSpace(content);
    // cardSpace.appendChild(actionBtnSpace.getTag());
}

function getCardActBtnListByType(content) {
    let resultBtnList = [];

    if (content.type === "CUSTOM") {
        resultBtnList.push(
            new ButtonTag()
                .setClassName("btn btn-warning card-list-btn card-action-btn card-modify-btn me-2")
                .setType("button")
                .setDataset([{cardId: content.cardId, type: content.type}])
                .setInnerHTML("수정")
        )
        resultBtnList.push(
            new ButtonTag()
                .setClassName("btn btn-danger card-list-btn card-action-btn card-delete-btn me-2")
                .setType("button")
                .setDataset([{cardId: content.cardId, type: content.type}])
                .setInnerHTML("삭제")
        )
    } else {
        resultBtnList.push(
            new ButtonTag()
                .setClassName("btn btn-primary card-list-btn card-action-btn letter-read-btn me-2")
                .setType("button")
                .setDataset([{letterId: content.letterId}])
                .setInnerHTML("편지읽기")
        )
    }
    resultBtnList.push(new ButtonTag()
        .setClassName("btn btn-primary card-list-btn card-write-btn  me-2")
        .setId("card-space-write-card-btn")
        .setInnerHTML("확언쓰기"));

    return resultBtnList;
}

function createLetterInfoSpace(content) {

    let innerHtmlList = [
        new DivTag()
            .setStyle([{borderTop: "1px solid gray"}]),
    ]

    if (content.type !== "CUSTOM") {
        innerHtmlList.push(new PTag()
            .setTextContent(content.postDate + "<br/><b>Letter</b> " + content.letterTitle)
        );
    } else {
        innerHtmlList.push(new PTag()
            .setTextContent(content.postDate + "<br/><b>Custom</b> " + "내가 만든 카드")
        );
    }

    return new DivTag()
        .setStyle([{
            display: "flex",
            flexDirection: "column",
            alignSelf: "stretch",
        }]).setInnerHTML(innerHtmlList);
}

function createActionBtnSpace(content) {
    let cardActionBtnList = [new ButtonTag()
        .setClassName("card-list-btn card-action-btn card-write-btn")
        .setId("card-space-write-card-btn")
        .setInnerHTML("확언 쓰기")
    ];

    if (content.type === "CUSTOM") {
        cardActionBtnList.unshift(
            new ButtonTag()
                .setClassName("card-list-btn card-action-btn card-modify-btn")
                .setDataset([{cardId: content.cardId, type: content.type}])
                .setInnerHTML("수정 하기"),
            new ButtonTag()
                .setClassName("card-list-btn card-action-btn card-delete-btn")
                .setDataset([{cardId: content.cardId, type: content.type}])
                .setInnerHTML("삭제 하기"),
        );
    } else {
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
