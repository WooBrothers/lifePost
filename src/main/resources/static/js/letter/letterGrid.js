import {LetterApi} from "./letterApi.js";

export class LetterGrid {
    /* letter grid 뷰 생성 클래스 */

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

    static async createTodayLetterGrid(todayLetterGrid) {
        /* 최신 편지 그리드 생성 */

        await LetterApi.getLatestLetter().then(res => {
            this.createGrid(todayLetterGrid, res);
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

