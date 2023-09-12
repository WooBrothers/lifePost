import {getLetterList} from "./letterListApi.js";
import {ButtonTag, DivTag, HTag, ImgTag, PTag} from "../../common/tagUtil.js";

export async function createLetterListSpace(letterListSpace, page, event) {
    const type = getLetterType();

    let resultResponse = null;
    await getLetterList(page, 8, type, getFocusType()).then(response => {
        createLetter(response, letterListSpace);
        resultResponse = response;
    });

    event();

    return resultResponse
}

function getLetterType() {
    const isMyLetterOn = document.getElementById("my-letter-filter-btn").dataset.onOff;
    const isMissLetterOn = document.getElementById("miss-letter-filter-btn").dataset.onOff;

    let resultTypeList = [];

    if (isMyLetterOn === "true") {
        resultTypeList.push("MY_LETTER");
    }
    if (isMissLetterOn === "true") {
        resultTypeList.push("MISS");
    }
    if (resultTypeList.length === 0) {
        resultTypeList.push("MY_LETTER");
    }

    return resultTypeList;
}

function getFocusType() {
    const focusFilterBtn = document.getElementById("focus-letter-filter-btn");

    let result = [];
    if (focusFilterBtn.dataset.onOff === "false") {
        result.push("ATTENTION");
        result.push("NON");
    } else {
        result.push("ATTENTION");
    }
    return result;
}

function getFocusImgByContent(content) {
    let result = {};

    if (content.focusType === "ATTENTION") {
        result.focus = true;
        result.focusImgUrl = "/img/focus-mark-on.png";
        return result
    } else if (content.focusType === "NON") {
        result.focus = false;
        result.focusImgUrl = "/img/focus-mark-off.png";
        return result;
    } else {
        return null;
    }
}

function createLetter(response, letterListSpace) {

    const preEmptyContentDiv = document.querySelector(".empty-content");
    if (preEmptyContentDiv) {
        preEmptyContentDiv.remove();
    }

    if (response.content.length === 0) {
        const emptyContentDiv = new DivTag()
            .setClassName("empty-content mt-5 h3")
            .setInnerHTML("편지가 없습니다. 편지을 읽어 보세요!")
        letterListSpace.appendChild(emptyContentDiv.getTag());
        return;
    }

    response.content.forEach(responseContent => {
        const letterSpace = new DivTag()
            .setClassName("letter-space col")
            .setId(`letter-space-${responseContent.id}`)
            .setDataset([{
                memberLetterId: responseContent.memberLetterId,
                letterId: responseContent.id,
                type: responseContent.type,
                postDate: responseContent.postDate,
            }])
            .getTag();

        // 카드 리스트 생성 메서드
        createLetterCard(responseContent, letterSpace, getFocusImgByContent(responseContent))
        letterListSpace.appendChild(letterSpace);
    });
}

function createLetterCard(responseContent, letterSpace, focusInfo) {
    const cardDiv = new DivTag()
        .setClassName("card h-100 p-0")

    const imgSrc = responseContent.letterImage ? responseContent.letterImage : '/img/letter-img.png';

    const cardImgDiv = new DivTag()
        .setClassName("col")
    const cardImg = new ImgTag()
        .setSrc(imgSrc)
        .setClassName("card-img-top img-fluid")
        .setAlt("no img")
        .setStyle([{objectFit: "cover", height: "12rem"}]);

    if (focusInfo) {
        const letterInfoSpace = new DivTag()
            .setClassName("card-img-overlay p-0 d-flex justify-content-between");

        const myLetterText = createMyLetterText();
        const focusBtn = createFocusBtn(focusInfo);

        letterInfoSpace.setInnerHTML([myLetterText, focusBtn]);
        cardImgDiv.setInnerHTML([cardImg, letterInfoSpace]);
    }

    const cardBody = createCardBody(responseContent);

    cardDiv.setInnerHTML([cardImgDiv, cardBody]);
    letterSpace.appendChild(cardDiv.getTag());
}

function createMyLetterText() {
    return new PTag()
        .setClassName("bg-primary text-white text-center h4 py-1")
        .setTextContent("My Letter")
        .setStyle([{width: "108px", height: "40px"}]);
}

function createFocusBtn(focusInfo) {
    return new DivTag()
        .setClassName("focus-space p-1")
        .setInnerHTML([
            new ButtonTag()
                .setClassName("focus-btn")
                .setDataset([{focus: focusInfo.focus}])
                .setBackground(focusInfo.focusImgUrl)
                .setStyle([{
                    width: "36px",
                    height: "44px",
                    border: "none",
                    backgroundColor: "transparent",
                }])
        ]);
}

function createCardBody(responseContent) {
    const cardBody = new DivTag()
        .setClassName("card-body");
    const cardTitle = new HTag(5)
        .setClassName("card-title")
        .setInnerHTML(responseContent.title);
    const cardText = new PTag()
        .setClassName("card-text")
        .setTextContent(responseContent.content);

    return cardBody.setInnerHTML([
        cardTitle,
        cardText
    ]);
}
