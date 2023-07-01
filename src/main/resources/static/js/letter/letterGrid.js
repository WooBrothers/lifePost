import {LetterApi} from "./letterApi.js";

export class LetterGrid {
    /* letter grid 뷰 생성 클래스 */
    static ID_TODAY_PREFIX = "today-";
    static CLASS_TODAY_PREFIX = LetterGrid.ID_TODAY_PREFIX + "letter-item";

    static createGrid(letterGridContainers, letter) {
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

        divInfos.forEach(divInfo => {
            const div = document.createElement("div");
            div.className = divInfo.className;
            div.innerHTML = divInfo.innerHTML;
            letterGridContainer.appendChild(div);
        });

        letterGridContainers.appendChild(letterGridContainer);
    }

    static createTodayGrid(todayLetterGrid, letter) {

        const letterGridContainer = document.createElement("div");
        letterGridContainer.className = "today-letter-grid-container";
        letterGridContainer.dataset.id = letter.id;

        const divInfos = [
            {
                id: LetterGrid.ID_TODAY_PREFIX + "letter-image",
                className: LetterGrid.CLASS_TODAY_PREFIX,
                innerHTML: "<img src='/img/letter-img.png'>"
            }, {
                id: LetterGrid.ID_TODAY_PREFIX + "text",
                className: LetterGrid.CLASS_TODAY_PREFIX,
                innerHTML: "Today"
            }, {
                id: LetterGrid.ID_TODAY_PREFIX + "empty",
                className: LetterGrid.CLASS_TODAY_PREFIX,
                innerHTML: ""
            }, {
                id: LetterGrid.ID_TODAY_PREFIX + "info",
                className: LetterGrid.CLASS_TODAY_PREFIX,
                innerHTML: ""
            }
        ];

        LetterGrid.createDivByDivInfosArraysAndParent(divInfos, letterGridContainer);


        const todayLetterInfoDiv = letterGridContainer
            .querySelector("#" + LetterGrid.ID_TODAY_PREFIX + "info");

        const divTodayLetterInfos = [
            {
                id: LetterGrid.ID_TODAY_PREFIX + "comment",
                className: LetterGrid.CLASS_TODAY_PREFIX,
                innerHTML: "이 시간 이후에는 읽을 수 없는 편지"
            }, {
                id: LetterGrid.ID_TODAY_PREFIX + "timer",
                className: LetterGrid.CLASS_TODAY_PREFIX,
                innerHTML: "<img src='/img/sand-time.png'> 23 : 55 : 55"
            }, {
                id: LetterGrid.ID_TODAY_PREFIX + "line",
                className: LetterGrid.CLASS_TODAY_PREFIX,
                innerHTML: "<hr/>"
            }, {
                id: LetterGrid.ID_TODAY_PREFIX + "letter-title",
                className: LetterGrid.CLASS_TODAY_PREFIX,
                innerHTML: letter.title
            }, {
                id: LetterGrid.ID_TODAY_PREFIX + "letter-contents-part",
                className: LetterGrid.CLASS_TODAY_PREFIX,
                innerHTML: letter.contents
            },
        ]

        LetterGrid.createDivByDivInfosArraysAndParent(divTodayLetterInfos, todayLetterInfoDiv);

        todayLetterGrid.appendChild(letterGridContainer);
    }

    static timer() {

    }

    static createDivByDivInfosArraysAndParent(divInfos, parentDiv) {
        divInfos.forEach(divInfo => {
            const div = document.createElement("div");
            div.className = divInfo.className;
            div.id = divInfo.id;
            div.innerHTML = divInfo.innerHTML;
            parentDiv.appendChild(div);
        });
    }

    static async createTodayLetterGrid(todayLetterGrid) {
        /* 최신 편지 그리드 생성 */

        await LetterApi.getLatestLetter().then(res => {
            this.createTodayGrid(todayLetterGrid, res);
        });
    }

    static async createGridByLetterId(latestLetterId, letterGridContainers) {
        /* latestLetterId 아이디 이후의 편지 조회 및 출력 */

        await LetterApi.getNextLettersByPageId(latestLetterId).then(res => {
            res.content.forEach(letter => {
                this.createGrid(letterGridContainers, letter);
            })
        });
    }
}

