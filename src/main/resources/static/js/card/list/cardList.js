import {getCardList} from "./cardListApi.js";
import {ButtonTag, DivTag, HTag, ImgTag, PTag} from "../../common/tagUtil.js";
import {bootstrapPopover} from "../../common/utilTool.js";

export async function createCardListSpace(cardListSpace, page, event) {

    // bootstrap popover 사용을 위한 조건
    bootstrapPopover();

    const cardType = getCardTypeFilter();
    const focusType = getIsFocusFilter();

    let resultResponse = null;

    await getCardList(page, 8, cardType, focusType).then(response => {
        createCard(response, cardListSpace);
        resultResponse = response;
        ifNoHaveCardPlayCoachMark(response);
    });

    event();
    return resultResponse
}

function getCardTypeFilter() {
    const isAffirmationOn = document.querySelectorAll(".affirmation-filter")[0].dataset.onOff;
    const isCustomBtnOn = document.querySelectorAll(".custom-filter")[0].dataset.onOff;

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

export function createCard(response, cardListSpace) {

    const preEmptyContentDiv = document.querySelector("#card-empty-info");

    if (preEmptyContentDiv) {
        preEmptyContentDiv.remove();
    }

    // 카드 없을 시 안내문구 출력 및 렌더링 종료
    if ((!response || response.content.length === 0) && cardListSpace.childNodes.length === 0) {
        const emptyTexts = document.querySelectorAll("#card-empty-info")

        if (emptyTexts.length !== 0) {
            emptyTexts.remove();
        }

        const emptyText = new DivTag()
            .setClassName("card-empty-info-text")
            .setId("card-empty-info")
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
                        .setId(`card-title-${content.memberCardId}`)
                        .setInnerHTML(content.cardTitle),
                    new PTag()
                        .setId(`card-content-${content.memberCardId}`)
                        .setClassName("card-content")
                        .setTextContent(content.contents)
                        .setStyle([{whiteSpace: "pre-line"}])
                ]),
            new DivTag()
                .setClassName("card-footer text-center")
                .setInnerHTML(getCardActBtnListByType(content))
        ])

    cardSpace.appendChild(cardColumn.getTag());
}

function getCardActBtnListByType(content) {
    let resultBtnList = [];

    if (content.type === "CUSTOM") {
        resultBtnList.push(
            new ButtonTag()
                .setClassName("btn btn-warning card-list-btn card-action-btn card-modify-btn me-2")
                .setType("button")
                .setDataset([{
                    memberCardId: content.memberCardId,
                    cardId: content.cardId,
                    type: content.type
                }])
                .setInnerHTML("수정")
        )
        resultBtnList.push(
            new ButtonTag()
                .setClassName("btn btn-danger card-list-btn card-action-btn card-delete-btn me-2")
                .setType("button")
                .setDataset([{
                    memberCardId: content.memberCardId,
                    cardId: content.cardId,
                    type: content.type
                }])
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
        .setDataset([{
            memberCardId: content.memberCardId,
            cardId: content.cardId,
            writeCount: content.writeCount
        }])
        .setInnerHTML("카드쓰기"));

    return resultBtnList;
}

function ifNoHaveCardPlayCoachMark(response) {
    if (response.content.length === 0) {
        // 화면 어둡게 하기
        // 기능들을 설명할 순서 정하기
        // 필터 기능 설명
        // 카드 만들기 설명
        // 카드 만들기 url, title, contents 입력 안내
        // 카드 만들기 저장 버튼 클릭 안내
        // 만들어진 내가 만든 카드 수정, 삭제 버튼 안내
        // 카드 쓰기 기능 안내
        // 카드를 받아 쓰고 하루에 10번 입력하면 스탬프 지급 안내
        

    }
}