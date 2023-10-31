import {getLetterList, getOpenLetterList} from "./letterListApi.js";
import {ButtonTag, DivTag, HTag, ImgTag, PTag} from "../../common/tagUtil.js";
import {getTodayDate, removeImageTags} from "../../common/utilTool.js";

export async function createLetterListSpace(letterListSpace, page, event) {

    const type = getLetterType();
    const focusType = getFocusType();

    let resultResponse = null;
    await getLetterList(page, 8, type, focusType).then(response => {
        createLetter(response, letterListSpace);
        resultResponse = response;
    });

    event();
    return resultResponse
}

export async function createOpenLetterListSpace(letterListSpace, page, event) {
    let resultResponse = null;

    document.querySelectorAll(".filter-group").forEach(filterGroup => {
        filterGroup.remove();
    })

    await getOpenLetterList(page, 7).then(response => {
        createIndexLetter(response, letterListSpace);
        resultResponse = response;
    });

    event();

    return resultResponse
}

function getLetterType() {
    const isMyLetterOn = document.querySelectorAll(".my-letter-filter")[0].dataset.onOff;
    const isMissLetterOn = document.querySelectorAll(".miss-letter-filter")[0].dataset.onOff;

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
    const focusFilterBtn = document.querySelectorAll(".focus-letter-filter")[0];

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
            .setClassName("letter-space col mb-4")
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

function createIndexLetter(response, letterListSpace) {

    response.content.forEach(responseContent => {
        const letterSpace = new DivTag()
            .setClassName("letter-space col mb-4")
            .setId(`letter-space-${responseContent.id}`)
            .setDataset([{
                memberLetterId: responseContent.memberLetterId,
                letterId: responseContent.id,
                type: responseContent.type,
                postDate: responseContent.postDate,
            }])
            .getTag();

        // 카드 리스트 생성 메서드
        createIndexLetterCard(responseContent, letterSpace, getFocusImgByContent(responseContent))
        letterListSpace.appendChild(letterSpace);
    });
}

function createLetterCard(responseContent, letterSpace, focusInfo) {
    const cardDiv = createCardDiv();

    const imgSrc = getImgSrc(responseContent);

    const cardImgDiv = new DivTag()
        .setClassName("col");
    const cardImg = new ImgTag()
        .setSrc(imgSrc)
        .setClassName("card-img-top img-fluid")
        .setAlt("no img")
        .setStyle([{
            objectFit: "cover",
            height: "12rem",
            borderTopLeftRadius: "6px",
            borderTopRightRadius: "6px",
        }]);

    let cardImgChildren = [cardImg];

    if (focusInfo) {
        const letterInfoSpace = new DivTag()
            .setClassName("card-img-overlay p-0 d-flex justify-content-between");

        const myLetterText = createMyLetterText();
        const focusBtn = createFocusBtn(focusInfo);

        letterInfoSpace.setInnerHTML([myLetterText, focusBtn]);
        cardImgChildren.push(letterInfoSpace);
    }
    cardImgDiv.setInnerHTML(cardImgChildren);
    const cardBody = createCardBody(responseContent);

    cardDiv.setInnerHTML([cardImgDiv, cardBody]);
    letterSpace.appendChild(cardDiv.getTag());
}

function createIndexLetterCard(responseContent, letterSpace, focusInfo) {
    const cardDiv = createCardDiv()

    const imgSrc = getImgSrc(responseContent);

    const cardImgDiv = new DivTag()
        .setClassName("col");
    const cardImg = new ImgTag()
        .setSrc(imgSrc)
        .setClassName("card-img-top img-fluid")
        .setAlt("no img")
        .setStyle([{
            objectFit: "cover",
            height: "12rem",
            borderTopLeftRadius: "6px",
            borderTopRightRadius: "6px",
        }]);

    let cardImgChildren = [cardImg];

    if (responseContent.postDate === getTodayDate()) {
        const letterInfoSpace = new DivTag()
            .setClassName("card-img-overlay p-0 d-flex justify-content-between");

        const todayText = createTodayLetterText();

        letterInfoSpace.setInnerHTML([todayText]);
        cardImgChildren.push(letterInfoSpace);
    }
    cardImgDiv.setInnerHTML(cardImgChildren);
    const cardBody = createCardBody(responseContent);

    cardDiv.setInnerHTML([cardImgDiv, cardBody]);
    letterSpace.appendChild(cardDiv.getTag());
}

function createCardDiv() {
    return new DivTag()
        .setClassName("card h-100 p-0");
}

function getImgSrc(responseContent) {
    return responseContent.letterImage ? responseContent.letterImage : '/img/letter-img.png';
}

function createMyLetterText() {
    return new PTag()
        .setClassName("bg-primary text-white h4")
        .setTextContent("My letter")
        .setStyle([{
            width: "108px",
            height: "40px",
            fontFamily: "serif",
            fontSize: "23px",
            textAlign: "center",
            paddingTop: "6px"
        }]);
}

function createTodayLetterText() {
    return new DivTag()
        .setClassName("bg-primary text-white text-center h2 py-1")
        .setStyle([{
            width: "100px",
            height: "45px",
            fontSize: "30px",
            fontFamily: "KoPubWorldBatang",
            borderTopLeftRadius: "6px",
            paddingTop: "5px",
            alignItems: "center",
        }])
        .setInnerHTML("Today");
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

    const content = removeImageTags(responseContent.content);

    const cardText = new PTag()
        .setClassName("card-text")
        .setTextContent(content);

    return cardBody.setInnerHTML([
        cardTitle,
        cardText
    ]);
}
