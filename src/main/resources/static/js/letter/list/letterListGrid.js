import {getLetterList} from "./letterListApi.js";
import {ButtonTag, DivTag} from "../../common/tagUtil.js";
import {getColorHexInFiveTypeList} from "../../common/utilTool.js";

export async function createLetterListSpace(letterListSpace, page, event) {
    const type = getLetterType();

    let resultResponse = null;
    await getLetterList(page, 10, type, getFocusType()).then(response => {
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
            .setClassName("empty-content")
            .setStyle([{
                fontSize: "20px",
            }])
            .setInnerHTML("편지가 없습니다. 편지을 읽어 보세요!")
        letterListSpace.appendChild(emptyContentDiv.getTag());
        return;
    }

    response.content.forEach(responseContent => {
        const letterSpace = new DivTag()
            .setClassName("letter-space")
            .setId(`letter-space-${responseContent.id}`)
            .setStyle([{
                "background-color": getColorHexInFiveTypeList(),
                width: "250px",
                height: "500px",
                margin: "15px 15px 15px 15px",
                display: "flex",
                flexDirection: "column",
            }])
            .setDataset([{
                memberLetterId: responseContent.memberLetterId,
                letterId: responseContent.id,
                type: responseContent.type,
                postDate: responseContent.postDate,
            }])
            .getTag();

        // 카드 리스트 생성 메서드
        setLetterContentGridSpace(responseContent, letterSpace, getFocusImgByContent(responseContent));

        letterListSpace.appendChild(letterSpace);
    });
}

function setLetterContentGridSpace(responseContent, letterSpace, focusInfo) {

    const letterImgSpace = new DivTag()
        .setClassName("letter-img-space")
        .setStyle([
            {
                width: "100%",
                height: "40%",
                position: "relative",
            }
        ]);

    const letterImage = responseContent.letterImage ? responseContent.letterImage : '/img/letter-img.png';
    const letterImgTag = new DivTag()
        .setClassName("letter-img")
        .setStyle([{
            width: "100%",
            height: "100%",
            position: "absolute",
            top: "0px",
            zIndex: "1",
            backgroundImage: `url(${letterImage})`,
            backgroundSize: "cover",
            backgroundPosition: "center",
            backgroundRepeat: "no-repeat",
            marginBottom: "10px",
        }]);

    letterImgSpace.getTag().appendChild(letterImgTag.getTag());

    // 소유한 편지일 경우 > focus btn, 소유여부 태그 추가
    if (focusInfo) {
        const focusBtn = new DivTag()
            .setClassName("focus-space")
            .setStyle([{
                display: "flex",
                flexDirection: "row-reverse"
            }])
            .setInnerHTML([
                new ButtonTag()
                    .setClassName("focus-btn")
                    .setDataset([{focus: focusInfo.focus}])
                    .setBackground(focusInfo.focusImgUrl)
                    .setStyle([{
                        position: "absolute",
                        top: "5px",
                        zIndex: "2",
                        width: "36px",
                        height: "44px",
                        border: "none",
                        backgroundColor: "transparent",
                    }])
            ]);

        const isOwnLetterTag = new DivTag()
            .setClassName("own-letter-space")
            .setStyle([{
                backgroundColor: letterSpace.style.backgroundColor,
                position: "absolute",
                height: "30px",
                width: "100px",
                fontSize: "20px",
                fontWeight: "bold",
                zIndex: "3",
                display: "flex",
                alignItems: "center",
                textAlign: "center",
                justifyContent: "center"

            }])
            .setInnerHTML("My Letter");

        letterImgSpace.getTag().appendChild(focusBtn.getTag());
        letterImgSpace.getTag().appendChild(isOwnLetterTag.getTag());
    }


    const letterTitle = new DivTag()
        .setClassName("letter-title")
        .setInnerHTML(responseContent.title)
        .setStyle([{fontSize: "30px", fontWeight: "bold"}]);

    const line = new DivTag()
        .setClassName("letter-line")
        .setStyle([{
            borderTop: "1px solid black",
            display: "flex",
            marginBottom: "10px"
        }]);

    const letterInfoSpace = new DivTag()
        .setClassName("letter-info-space")
        .setStyle([{
            display: "flex",
            flexDirection: "row"
        }])
        .setInnerHTML([
            new DivTag()
                .setClassName("letter-create-date")
                .setInnerHTML(getKoreanDate(responseContent.postDate)),
            new DivTag()
                .setClassName("spacer")
                .setStyle([{
                    display: "flex",
                    flex: 1
                }]),
            new DivTag()
                .setClassName("letter-author-name")
                .setStyle([{
                    fontWeight: "bold"
                }])
                .setInnerHTML(`author ${responseContent.writer}`),
        ]);
    const limitedContent = new DivTag()
        .setClassName("letter-limited-content")
        .setInnerHTML(responseContent.content);

    letterSpace.appendChild(letterImgSpace.getTag());
    letterSpace.appendChild(letterTitle.getTag());
    letterSpace.appendChild(line.getTag());
    letterSpace.appendChild(letterInfoSpace.getTag());
    letterSpace.appendChild(limitedContent.getTag());
}

function getKoreanDate(date) {
    const dateList = date.split("-");

    return `${dateList[0]}년 ${dateList[1]}월 ${dateList[2]}일`;
}