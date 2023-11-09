import {getIndexLetterList, getLetterList} from "./letterListApi.js";
import {ButtonTag, DivTag, HTag, ImgTag, PTag} from "../../common/tagUtil.js";
import {createCoupangAdBannerInFeed, getTodayDate, removeImageTags} from "../../common/utilTool.js";

export async function createLetterListSpace(letterListSpace, letterId, event) {

    const focusType = getFocusType();

    let resultResponse = null;
    await getLetterList(letterId, 7, focusType).then(response => {
        createLetter(response, letterListSpace);
        resultResponse = response;
    });

    event();
    return resultResponse
}

export async function createOpenLetterListSpace(letterListSpace, postDate, event) {
    let resultResponse = null;

    await getIndexLetterList(postDate, 7).then(response => {
        createIndexLetter(response, letterListSpace);
        resultResponse = response;
    });

    event();

    return resultResponse
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

    const cdnUrl = "https://cdn.life-post.net/img/service/card";
    if (content.focusType === "ATTENTION") {
        result.focus = true;
        result.focusImgUrl = `url('${cdnUrl}/focus-mark-on.png')`;
        return result
    } else if (content.focusType === "NON") {
        result.focus = false;
        result.focusImgUrl = `url('${cdnUrl}/focus-mark-off.png')`;
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

    for (let idx = 0; idx < response.content.length; idx++) {
        if (idx === 2) {
            // 쿠팡 광고 피드 생성

            const letterSpace = new DivTag()
                .setClassName("letter-space col mb-4")
                .setDataset([{}])
                .getTag();

            const targetFeed = letterListSpace.querySelector(`#letter-space-${response.content[0].id}`);
            const coupangPartnerAdSpace = createCoupangAdBannerInFeed(targetFeed);

            letterSpace.appendChild(coupangPartnerAdSpace);
            letterListSpace.appendChild(letterSpace);
        }

        const content = response.content[idx];

        const letterSpace = new DivTag()
            .setClassName("letter-space col mb-4")
            .setId(`letter-space-${content.id}`)
            .setDataset([{
                memberLetterId: content.memberLetterId,
                letterId: content.id,
                type: content.type,
                postDate: content.postDate,
            }])
            .getTag();

        // 카드 리스트 생성 메서드
        createLetterCard(content, letterSpace, getFocusImgByContent(content))
        letterListSpace.appendChild(letterSpace);
    }
}

function createIndexLetter(response, letterListSpace) {

    for (let idx = 0; idx < response.content.length; idx++) {
        if (idx === 2) {
            // 쿠팡 광고 피드 생성

            const letterSpace = new DivTag()
                .setClassName("letter-space col mb-4")
                .getTag();

            const targetFeed = letterListSpace.querySelector(`#letter-space-${response.content[0].id}`);
            const coupangPartnerAdSpace = createCoupangAdBannerInFeed(targetFeed);

            letterSpace.appendChild(coupangPartnerAdSpace);
            letterListSpace.appendChild(letterSpace);
        }

        const letterSpace = new DivTag()
            .setClassName("letter-space col mb-4")
            .setId(`letter-space-${response.content[idx].id}`)
            .setDataset([{
                memberLetterId: response.content[idx].memberLetterId,
                letterId: response.content[idx].id,
                type: response.content[idx].type,
                postDate: response.content[idx].postDate,
            }])
            .getTag();

        // 카드 리스트 생성 메서드
        createIndexLetterCard(response.content[idx], letterSpace, getFocusImgByContent(response.content[idx]));
        letterListSpace.appendChild(letterSpace);
    }
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
            fontSize: "23px",
            fontFamily: "KoPubWorldBatang",
            borderTopLeftRadius: "6px",
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
