import {LetterGrid} from "./letter/letterGrid.js";
import {CardGrid} from "./card/cardGrid.js";


/* page load 시 이벤트 바인딩 + html 생성 */
window.onload = async () => {
    // 그리드 생성 관리 클래스 객체 생성

    /* view 생성 */
    const todayLetterGrid = document.getElementById("today-letter-grid");
    await LetterGrid.createTodayLetterGrid(todayLetterGrid);

    const todayCardGrid = document.getElementById("today-card-grid");
    // CardGrid.createCardGridByPageIndex(todayCardGrid);
    await CardGrid.createFocusCardGrid(todayCardGrid);

    // todayLetterGrid 생성후에 letter id 참조
    const todayLetterId = todayLetterGrid.children[0].dataset.id;
    const letterGridContainers = document.getElementById("letter-grid-containers");
    await LetterGrid.createGridByLetterId(todayLetterId, letterGridContainers);
}
