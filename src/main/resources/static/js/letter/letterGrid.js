import {addDivByDivInfosToParent, DivTag, HrTag, ImgTag, PTag} from "../common/tagUtil.js";
import {LetterApi} from "./letterApi.js"
import {bindEventToLetterGrid, bindEventToLetterGridContainer} from "./letterEvent.js";

export class LetterGrid {
    /* letter grid 뷰 생성 클래스 */
    static ID_TODAY_PREFIX = "today-";
    static CLASS_TODAY_PREFIX = LetterGrid.ID_TODAY_PREFIX + "letter-item";
    static CLASS_TODAY_INFO = LetterGrid.ID_TODAY_PREFIX + "letter-item-info";
    static TODAY_LETTER_GRID_CONTAINER_CLASS_NAME = LetterGrid.ID_TODAY_PREFIX + "letter-grid-container";

    static async createTodayLetterGrid(todayLetterGrid) {
        /* 최신 편지 그리드 생성 */

        await LetterApi.getLatestLetter().then(res => {
            LetterGrid.setTodayLetterGridTag(todayLetterGrid, res);
            bindEventToLetterGrid();
        });
    }

    static setTodayLetterGridTag(todayLetterGrid, letter) {

        const letterGridContainerTag = new DivTag()
            .setClassName(LetterGrid.TODAY_LETTER_GRID_CONTAINER_CLASS_NAME)
            .setDataset([{"id": letter.id}])
            .getTag();

        const divInfos = [
            {
                id: LetterGrid.ID_TODAY_PREFIX + "letter-image",
                className: LetterGrid.CLASS_TODAY_PREFIX,
                innerHTML: [new ImgTag().setSrc('/img/letter-img.png')]
            }, {
                id: LetterGrid.ID_TODAY_PREFIX + "text",
                className: LetterGrid.CLASS_TODAY_PREFIX,
                innerHTML: "Today"
            }, {
                id: LetterGrid.ID_TODAY_PREFIX + "empty",
                className: LetterGrid.CLASS_TODAY_PREFIX,
                innerHTML: null
            }, {
                id: LetterGrid.ID_TODAY_PREFIX + "comment",
                className: LetterGrid.CLASS_TODAY_INFO,
                innerHTML: [new PTag().setInnerHTML("이 시간 이후에는 읽을 수 없는 편지")]
            }, {
                id: LetterGrid.ID_TODAY_PREFIX + "timezone",
                className: LetterGrid.CLASS_TODAY_INFO,
                innerHTML: [
                    new ImgTag()
                        .setSrc('/img/sand-time.png')
                        .setStyle([{
                            "height": "30px",
                            "width": "25px"
                        }])
                    , new DivTag()
                        .setId(LetterGrid.ID_TODAY_PREFIX + "timer")
                        .setInnerHTML(LetterGrid.getTime())]
            }, {
                id: LetterGrid.ID_TODAY_PREFIX + "line",
                className: LetterGrid.CLASS_TODAY_INFO,
                innerHTML: [new HrTag()]
            }, {
                id: LetterGrid.ID_TODAY_PREFIX + "letter-title",
                className: LetterGrid.CLASS_TODAY_INFO,
                innerHTML: letter.title
            }, {
                id: LetterGrid.ID_TODAY_PREFIX + "letter-contents-part",
                className: LetterGrid.CLASS_TODAY_INFO,
                innerHTML: letter.contents.slice(0, 75) + "..."
            },
        ];

        addDivByDivInfosToParent(divInfos, letterGridContainerTag);

        todayLetterGrid.appendChild(letterGridContainerTag);

        setInterval(LetterGrid.setTime, 1000);
    }

    static getTime() {

        const time = new Date();
        let hours = 23 - time.getHours();
        let minutes = 59 - time.getMinutes();
        let seconds = 59 - time.getSeconds();

        return `${hours < 10 ? `0${hours}` : hours} : ${minutes < 10 ? `0${minutes}` : minutes} : ${seconds < 10 ? `0${seconds}` : seconds}`;

    }

    static setTime() {
        const timeDiv = document.getElementById(LetterGrid.ID_TODAY_PREFIX + "timer");

        timeDiv.innerHTML = LetterGrid.getTime();
    }

    static async createGridByLetterId(latestLetterId, letterGridContainers) {
        /* latestLetterId 아이디 이후의 편지 조회 및 출력 */

        await LetterApi.getNextLettersByPageId(latestLetterId).then(res => {
            res.content.forEach(letter => {
                this.setLetterGrids(letterGridContainers, letter);
            })
        });

        bindEventToLetterGridContainer();
    }

    static setLetterGrids(letterGridContainers, letter) {
        /* 전달하는 div 객체에 자식으로 letter grid를 생성 및 추가 */

        const divInfos = [
            {className: "letter-item-title bg-primary", innerHTML: letter.title},
            {className: "letter-item-date bg-success", innerHTML: letter.createdDate},
            {className: "letter-item-img1 bg-info", innerHTML: letter.letterImage},
            {className: "letter-item-img2 bg-warning", innerHTML: letter.postStampImage},
        ];

        const letterGridContainer = document.createElement("div");
        letterGridContainer.className = "letter-grid-container";
        letterGridContainer.dataset.id = letter.id;

        addDivByDivInfosToParent(divInfos, letterGridContainer);

        letterGridContainers.appendChild(letterGridContainer);
    }
}

